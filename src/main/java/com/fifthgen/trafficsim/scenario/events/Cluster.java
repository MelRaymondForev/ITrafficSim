package com.fifthgen.trafficsim.scenario.events;

import java.awt.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Cluster {
    private String clusterID_;

    private String eventType_;
    private int minX_ = Integer.MAX_VALUE;
    private int minY_ = Integer.MAX_VALUE;
    private int maxX_ = 0;
    private int maxY_ = 0;
    private int size_ = 0;
    private Color clusterColor = Color.black;

    private ArrayList<Integer> xCoords_ = new ArrayList<Integer>();

    private ArrayList<Integer> yCoords_ = new ArrayList<Integer>();

    public String toString() {
        return "cluster - " + clusterID_;
    }

    public int getMinX_() {
        return minX_;
    }

    public int getMinY_() {
        return minY_;
    }

    public int getMaxX_() {
        return maxX_;
    }

    public int getMaxY_() {
        return maxY_;
    }

    public Color getClusterColor() {
        return clusterColor;
    }

    public ArrayList<Integer> getxCoords_() {
        return xCoords_;
    }

    public ArrayList<Integer> getyCoords_() {
        return yCoords_;
    }

    public String getClusterID_() {
        return clusterID_;
    }
}
