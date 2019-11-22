package com.fifthgen.trafficsim.scenario.events;

import com.fifthgen.trafficsim.Application;
import com.fifthgen.trafficsim.gui.Renderer;
import com.fifthgen.trafficsim.map.Map;

import java.io.*;
import java.util.ArrayList;

public final class EventSpotList {

    private static final EventSpotList INSTANCE = new EventSpotList();

    int[][] gridEEBL_ = null;
    int[][] gridPCN_ = null;
    int[][] gridPCNFORWARD_ = null;
    int[][] gridRHCN_ = null;
    int[][] gridEVAFORWARD_ = null;
    int[][] gridEVA_ = null;

    private EventSpot head_ = null;

    private EventSpotList() {
    }

    public static EventSpotList getInstance() {
        return INSTANCE;
    }

    public void addEventSpot(EventSpot event) {
        if (head_ == null) head_ = event;
        else {

            EventSpot tmpEvent = head_;
            while (tmpEvent.getNext_() != null) {
                if (tmpEvent.getX_() == event.getX_() && tmpEvent.getY_() == event.getY_()) return;
                tmpEvent = tmpEvent.getNext_();
            }

            tmpEvent = head_;
            while (tmpEvent.getNext_() != null) tmpEvent = tmpEvent.getNext_();
            tmpEvent.setNext_(event);
        }
    }

    public int findEventSpotTiming() {
        int returnValue = 999999999;
        EventSpot tmpSpot = head_;
        while (tmpSpot != null) {
            if (returnValue > tmpSpot.getEventSpotTiming_()) returnValue = tmpSpot.getEventSpotTiming_();
            tmpSpot = tmpSpot.getNext_();
        }
        return returnValue;
    }

    public int doStep(int time) {
        if (head_ != null) {
            EventSpot tmpSpot = head_;
            while (tmpSpot != null) {
                if (tmpSpot.getEventSpotTiming_() <= time) {

                    tmpSpot.execute(Renderer.getInstance().getTimePassed());
                }
                tmpSpot = tmpSpot.getNext_();
            }
            return findEventSpotTiming();
        }
        return 999999999;
    }

    public EventSpot getHead_() {
        return head_;
    }

}