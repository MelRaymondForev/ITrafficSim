package com.fifthgen.trafficsim.simulation;

import com.fifthgen.trafficsim.Application;
import com.fifthgen.trafficsim.gui.Renderer;
import com.fifthgen.trafficsim.gui.helpers.GeneralLogWriter;
import com.fifthgen.trafficsim.gui.helpers.IDSLogWriter;
import com.fifthgen.trafficsim.gui.helpers.PrivacyLogWriter;
import com.fifthgen.trafficsim.localization.Messages;
import com.fifthgen.trafficsim.map.Map;
import com.fifthgen.trafficsim.map.Region;
import com.fifthgen.trafficsim.scenario.*;
import com.fifthgen.trafficsim.scenario.events.EventList;
import com.fifthgen.trafficsim.scenario.events.EventSpotList;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public final class SimulationMaster extends Thread {

    public static final int TIME_PER_STEP = 40;

    private static final EventList eventList_ = EventList.getInstance();

    private static int eventSpotCountdown_ = -1;

    private volatile boolean running_ = false;

    private volatile boolean doOneStep_ = false;

    private volatile int targetStepTime_ = TIME_PER_STEP;

    private volatile boolean jumpTimeMode_ = false;

    private volatile int jumpTimeTarget_ = -1;

    private WorkerThread[] workers_ = null;

    private CyclicBarrier barrierStart_ = null;

    private CyclicBarrier barrierDuringWork_ = null;

    private CyclicBarrier barrierFinish_ = null;

    private boolean guiEnabled = true;

    private boolean logSilentPeriodHeader_ = true;

    public SimulationMaster() {
    }

    public static int getEventSpotCountdown_() {
        return eventSpotCountdown_;
    }

    public static void setEventSpotCountdown_(int eventSpotCountdown) {
        eventSpotCountdown_ = eventSpotCountdown;
    }

    public synchronized void startThread() {

        if (Vehicle.isSilentPeriodsOn() && logSilentPeriodHeader_) {
            logSilentPeriodHeader_ = false;
            PrivacyLogWriter.log("Silent Period:Duration:" + Vehicle.getTIME_OF_SILENT_PERIODS() + ":Frequency:" + Vehicle.getTIME_BETWEEN_SILENT_PERIODS());
        }

        Renderer.getInstance().notifySimulationRunning(true);

        Renderer.getInstance().ReRender(true, false);
        running_ = true;
    }

    public synchronized void stopThread() {
        running_ = false;
        if ((Map.getInstance().getReadyState() == false || Scenario.getInstance().getReadyState() == false) && workers_ != null) {

            while (barrierStart_.getParties() - barrierStart_.getNumberWaiting() != 1) {
                try {
                    sleep(1);
                } catch (Exception e) {
                }
            }

            workers_[0].interrupt();

            workers_ = null;
        }
        Renderer.getInstance().notifySimulationRunning(false);
    }

    public void doOneStep() {
        if (!running_) {
            Renderer.getInstance().notifySimulationRunning(true);
            doOneStep_ = true;
        }
    }

    public WorkerThread[] createWorkers(int timePerStep, int threads) {
        ArrayList<WorkerThread> tmpWorkers = new ArrayList<WorkerThread>();
        WorkerThread tmpWorker = null;
        Region[][] regions = Map.getInstance().getRegions();
        ArrayList<Region> tmpRegions = new ArrayList<Region>();
        long regionCountX = Map.getInstance().getRegionCountX();
        long regionCountY = Map.getInstance().getRegionCountY();
        double regionsPerThread = regionCountX * regionCountY / (double) threads;
        long count = 0;
        double target = regionsPerThread;
        threads = 0;

        for (int i = 0; i < regionCountX; ++i) {
            for (int j = 0; j < regionCountY; ++j) {
                ++count;
                tmpRegions.add(regions[i][j]);
                if (count >= Math.round(target)) {
                    try {
                        tmpWorker = new WorkerThread(tmpRegions.toArray(new Region[0]), timePerStep);
                        ++threads;
                        tmpWorkers.add(tmpWorker);
                        tmpWorker.start();
                    } catch (Exception e) {

                    }
                    tmpRegions = new ArrayList<Region>();
                    target += regionsPerThread;
                }
            }
        }
        if (tmpRegions.size() > 0) {
            try {
                tmpWorker = new WorkerThread(tmpRegions.toArray(new Region[0]), timePerStep);
                ++threads;
                tmpWorkers.add(tmpWorker);
                tmpWorker.start();
            } catch (Exception e) {

            }
        }
        barrierStart_ = new CyclicBarrier(threads + 1);
        barrierDuringWork_ = new CyclicBarrier(threads);
        barrierFinish_ = new CyclicBarrier(threads + 1);
        Iterator<WorkerThread> iterator = tmpWorkers.iterator();
        while (iterator.hasNext()) {
            iterator.next().setBarriers(barrierStart_, barrierDuringWork_, barrierFinish_);
        }
        return tmpWorkers.toArray(new WorkerThread[0]);
    }

    public void run() {
        setName("SimulationMaster");
        int time, threads;
        long renderTime;
        Renderer renderer = Renderer.getInstance();
        CyclicBarrier barrierRender = new CyclicBarrier(2);
        renderer.setBarrierForSimulationMaster(barrierRender);

        long timeOld = 0;
        long timeNew = 0;
        long timeDistance = 0;
        boolean consoleStart = Renderer.getInstance().isConsoleStart();
        KnownVehiclesList.setTimePerStep_(TIME_PER_STEP);

        while (true) {
            try {
                if (running_ || doOneStep_) {
                    renderTime = System.nanoTime();
                    barrierRender.reset();

                    while (workers_ == null) {
                        if (Map.getInstance().getReadyState() && Scenario.getInstance().getReadyState()) {
                            if (Runtime.getRuntime().availableProcessors() < 2)
                                threads = 1;
                            else
                                threads = Runtime.getRuntime().availableProcessors() * 2;
                            long max_heap = Runtime.getRuntime().maxMemory() / 1048576;

                             workers_ = createWorkers(TIME_PER_STEP, threads);
                        } else {
                            sleep(50);
                        }
                    }
                    time = renderer.getTimePassed() + TIME_PER_STEP;

                    eventList_.processEvents(time);

                    barrierStart_.await();

                    barrierFinish_.await();

                    KnownVehiclesList.setTimePassed(time);
                    KnownRSUsList.setTimePassed(time);
                    renderer.setTimePassed(time);
                    KnownEventSourcesList.setTimePassed(time);
                    if (eventSpotCountdown_ < time) eventSpotCountdown_ = EventSpotList.getInstance().doStep(time);

                    if (!jumpTimeMode_) {

                        renderer.ReRender(false, true);

                        Thread.yield();
                        barrierRender.await(3, TimeUnit.SECONDS);

                        renderTime = ((System.nanoTime() - renderTime) / 1000000);
                        if (renderTime > 0) renderTime = targetStepTime_ - renderTime;
                        else renderTime = targetStepTime_ + renderTime;
                        if (renderTime > 0 && renderTime <= targetStepTime_) {
                            sleep(renderTime);
                        }
                    } else {
                        if (consoleStart && time % 5000 == 0) {
                            timeNew = System.currentTimeMillis();
                            timeDistance = timeNew - timeOld;
                            System.out.println("Time:::" + timeDistance);
                            timeOld = timeNew;
                            System.out.println(time);

                        }
                        if (time >= jumpTimeTarget_) {
                            jumpTimeTarget_ = -1;
                            jumpTimeMode_ = false;
                            stopThread();
                            if (consoleStart) {
                                System.out.println("Time:" + new Date());
                                System.out.println(Messages.getString("ConsoleStart.SimulationEnded"));

                                System.exit(0);
                            }
                            if (!consoleStart) {
                                Application.setProgressBar(false);
                                renderer.ReRender(false, true);
                            }
                        }
                    }
                    if (doOneStep_) {
                        doOneStep_ = false;
                        renderer.notifySimulationRunning(false);
                    }
                } else {
                    sleep(50);
                }
            } catch (Exception e) {
            }
        }
    }

    public boolean isSimulationRunning() {
        return running_;
    }
}