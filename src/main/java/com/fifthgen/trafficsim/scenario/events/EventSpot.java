package com.fifthgen.trafficsim.scenario.events;

import com.fifthgen.trafficsim.gui.Renderer;
import com.fifthgen.trafficsim.localization.Messages;
import com.fifthgen.trafficsim.map.Map;
import com.fifthgen.trafficsim.map.Region;
import com.fifthgen.trafficsim.routing.WayPoint;
import com.fifthgen.trafficsim.scenario.Vehicle;
import com.fifthgen.trafficsim.scenario.messages.PenaltyMessage;
import com.fifthgen.trafficsim.simulation.SimulationMaster;

import java.awt.*;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Random;

public class EventSpot {

    private static final Map MAP = Map.getInstance();

    private static Color hospitalColor = Color.magenta;

    private static Color icyRoadColor = Color.cyan;

    private static Color damagedRoadColor = Color.gray;

    private static Color schoolColor = Color.yellow;

    private static Color fire_stationColor = Color.red;

    private static Color policeColor = Color.blue;

    private static Color kindergartenColor = Color.green;

    private static Region[][] regions_;

    private int frequency_;

    private int radius_;

    private int x_;

    private int y_;

    private String eventSpotType_;

    private Color eventSpotColor_;

    private int eventSpotTiming_;

    private EventSpot next_ = null;

    private long seed_;

    private Random random_ = null;

    private boolean multiplier_ = true;

    public EventSpot(int x, int y, int frequency, int radius, String eventSpotType, long seed) {
        seed_ = seed;
        x_ = x;
        y_ = y;
        frequency_ = frequency;
        if (seed != -1) random_ = new Random(seed);
        eventSpotTiming_ = random_.nextInt(frequency) + 1;
        if (SimulationMaster.getEventSpotCountdown_() > eventSpotTiming_)
            SimulationMaster.setEventSpotCountdown_(eventSpotTiming_);
        radius_ = radius;
        eventSpotType_ = eventSpotType;
        if (eventSpotType.equals("hospital")) eventSpotColor_ = hospitalColor;
        else if (eventSpotType.equals("ice")) eventSpotColor_ = icyRoadColor;
        else if (eventSpotType.equals("streetDamage")) eventSpotColor_ = damagedRoadColor;
        else if (eventSpotType.equals("school")) eventSpotColor_ = schoolColor;
        else if (eventSpotType.equals("fire_station")) eventSpotColor_ = fire_stationColor;
        else if (eventSpotType.equals("police")) eventSpotColor_ = policeColor;
        else if (eventSpotType.equals("kindergarten")) eventSpotColor_ = kindergartenColor;

    }

    public static void setRegions_(Region[][] regions) {
        regions_ = regions;
    }

    public void execute(int timePassed) {
        eventSpotTiming_ += frequency_;

        if (eventSpotType_.equals("hospital") || eventSpotType_.equals("police") || eventSpotType_.equals("fire_station")) {
            int maxX = x_ + radius_;
            int maxY = y_ + radius_;
            int minX = x_ - radius_;
            int minY = y_ - radius_;
            long dx = 0;
            long dy = 0;
            long maxCommDistanceSquared = (long) radius_ * (long) radius_;
            int randomX = 0;
            int randomY = 0;

            WayPoint tmpWayPoint = null;

            ArrayDeque<WayPoint> destinations = null;
            Vehicle tmpVehicle;

            int k = 0;
            int j = 0;
            destinations = new ArrayDeque<WayPoint>(2);

            try {
                destinations.add(new WayPoint(x_, y_, 0));
            } catch (ParseException e1) {

                e1.printStackTrace();
            }

            while (j < 1 && k < 99999) {
                try {
                    ++k;

                    while (true) {
                        randomX = minX + random_.nextInt((maxX - minX));
                        randomY = minY + random_.nextInt((maxY - minY));
                        dx = x_ - randomX;
                        dy = y_ - randomY;
                        if ((dx * dx + dy * dy) <= maxCommDistanceSquared) break;

                    }

                    tmpWayPoint = new WayPoint(randomX, randomY, 0);
                    j++;
                    destinations.add(tmpWayPoint);
                } catch (Exception e) {
                }
            }

            try {
                destinations.add(new WayPoint(x_, y_, 0));
            } catch (ParseException e1) {

                e1.printStackTrace();
            }

            if (k < 99999) {
                try {
                    tmpVehicle = new Vehicle(destinations, 600, 4722, 10000, 800, 300, 100, 0, 833, new Color(124, 148, 235));
                    tmpVehicle.setDoNotRecycle_(true);
                    Map.getInstance().addVehicle(tmpVehicle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (eventSpotType_.equals("ice") || eventSpotType_.equals("streetDamage")) {
            int maxX = x_ + radius_;
            int maxY = y_ + radius_;
            int minX = x_ - radius_;
            int minY = y_ - radius_;
            long dx = 0;
            long dy = 0;
            long maxCommDistanceSquared = (long) radius_ * (long) radius_;
            int randomX = 0;
            int randomY = 0;

            int k = 0;
            int j = 0;

            while (j < 1 && k < 99999) {
                try {
                    ++k;

                    while (true) {
                        randomX = minX + random_.nextInt((maxX - minX));
                        randomY = minY + random_.nextInt((maxY - minY));
                        dx = x_ - randomX;
                        dy = y_ - randomY;
                        if ((dx * dx + dy * dy) <= maxCommDistanceSquared) {
                            break;
                        }
                    }
                    try {
                        EventList.getInstance().addEvent(new StartBlocking((timePassed), randomX, randomY, 0, 99, false, "HUANG_RHCN"));
                        EventList.getInstance().addEvent(new StopBlocking((timePassed + frequency_), randomX, randomY));

                        j++;
                    } catch (Exception e2) {

                    }
                } catch (Exception e) {
                }
            }
            Renderer.getInstance().ReRender(false, false);
        } else if (eventSpotType_.equals("school") || eventSpotType_.equals("kindergarten")) {
            int RegionMinX, RegionMinY, RegionMaxX, RegionMaxY;
            long dx, dy, maxDistanceSquared = (long) radius_ * radius_;

            Region tmpregion = MAP.getRegionOfPoint((x_ - radius_), (y_ - radius_));
            RegionMinX = tmpregion.getX();
            RegionMinY = tmpregion.getY();

            tmpregion = MAP.getRegionOfPoint((x_ + radius_), (y_ + radius_));
            RegionMaxX = tmpregion.getX();
            RegionMaxY = tmpregion.getY();

            int i = 0;
            Vehicle tmpVehicle = null;
            while (i < 100) {
                i++;
                Vehicle[] tmpVehicleArray = regions_[(RegionMinX + random_.nextInt(RegionMaxX - RegionMinX + 1))][(RegionMinY + random_.nextInt(RegionMaxY - RegionMinY + 1))].getVehicleArray();
                tmpVehicle = tmpVehicleArray[random_.nextInt(tmpVehicleArray.length)];

                if (tmpVehicle.getCurSpeed() != 0) {
                    dx = tmpVehicle.getX() - x_;
                    dy = tmpVehicle.getY() - y_;

                    if ((dx * dx + dy * dy) <= maxDistanceSquared) {

                        PenaltyMessage message = new PenaltyMessage(tmpVehicle.getX(), tmpVehicle.getY(), tmpVehicle.getX(), tmpVehicle.getY(), 50000, (timePassed + 2000), tmpVehicle.getCurStreet(), tmpVehicle.getCurLane(), (int) tmpVehicle.getCurPosition(), 500, (timePassed + 2000), false, tmpVehicle.getID(), null, "HUANG_EEBL", false, true);
                        tmpVehicle.getKnownMessages().addMessage(message, false, true);
                        message.setFloodingMode(true);

                        tmpVehicle.setEmergencyBraking_(true);
                        tmpVehicle.setEEBLmessageIsCreated_(true);
                        tmpVehicle.setEmergencyBrakingCountdown_(tmpVehicle.getEmergencyBrakingDuration_());
                        if (!multiplier_) i = 100;
                    }
                }
            }
        }
    }

    public int getFrequency_() {
        return frequency_;
    }

    public int getRadius_() {
        return radius_;
    }

    public EventSpot getNext_() {
        return next_;
    }

    public void setNext_(EventSpot next_) {
        this.next_ = next_;
    }

    public int getX_() {
        return x_;
    }

    public int getY_() {
        return y_;
    }

    public String getEventSpotType_() {
        return eventSpotType_;
    }

    public Color getEventSpotColor_() {
        return eventSpotColor_;
    }

    public int getEventSpotTiming_() {
        return eventSpotTiming_;
    }

    public long getSeed_() {
        return seed_;
    }
}