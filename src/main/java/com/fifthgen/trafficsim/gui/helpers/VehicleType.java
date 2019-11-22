package com.fifthgen.trafficsim.gui.helpers;

public class VehicleType {

    private final String name_;

    private int vehicleLength_;

    private int maxSpeed_;

    private int minSpeed_;

    private int maxCommDist_;

    private int minCommDist_;

    private int maxBrakingRate_;

    private int minBrakingRate_;

    private int maxAccelerationRate_;

    private int minAccelerationRate_;

    private int minTimeDistance_;

    private int maxTimeDistance_;

    private int minPoliteness_;

    private int maxPoliteness_;

    private int vehiclesDeviatingMaxSpeed_;

    private int deviationFromSpeedLimit_;

    private int maxWaittime_;

    private int minWaittime_;

    private boolean wifi_;

    private boolean emergencyVehicle_;

    private int color_;

    public VehicleType(String name, int vehicleLength, int maxSpeed, int minSpeed, int maxCommDist, int minCommDist, int maxBrakingRate, int minBrakingRate, int maxAccelerationRate, int minAccelerationRate, int minTimeDistance, int maxTimeDistance, int minPoliteness, int maxPoliteness, int vehiclesDeviatingMaxSpeed, int deviationFromSpeedLimit, int maxWaittime, int minWaittime, boolean wifi, boolean emergencyVehicle, int color) {
        name_ = name;
        vehicleLength_ = vehicleLength;
        maxSpeed_ = maxSpeed;
        minSpeed_ = minSpeed;
        maxCommDist_ = maxCommDist;
        minCommDist_ = minCommDist;
        maxWaittime_ = maxWaittime;
        minWaittime_ = minWaittime;
        maxBrakingRate_ = maxBrakingRate;
        minBrakingRate_ = minBrakingRate;
        maxAccelerationRate_ = maxAccelerationRate;
        minAccelerationRate_ = minAccelerationRate;
        minTimeDistance_ = minTimeDistance;
        maxTimeDistance_ = maxTimeDistance;
        minPoliteness_ = minPoliteness;
        maxPoliteness_ = maxPoliteness;
        vehiclesDeviatingMaxSpeed_ = vehiclesDeviatingMaxSpeed;
        deviationFromSpeedLimit_ = deviationFromSpeedLimit;
        wifi_ = wifi;
        emergencyVehicle_ = emergencyVehicle;
        color_ = color;
    }

    public String getName() {
        return name_;
    }

    public int getMaxSpeed() {
        return maxSpeed_;
    }

    public int getMinSpeed() {
        return minSpeed_;
    }

    public int getMaxCommDist() {
        return maxCommDist_;
    }

    public int getMinCommDist() {
        return minCommDist_;
    }

    public int getMaxWaittime() {
        return maxWaittime_;
    }

    public int getMinWaittime() {
        return minWaittime_;
    }

    public int getColor() {
        return color_;
    }

    public void setColor(int color) {
        color_ = color;
    }

    public int getMaxBrakingRate() {
        return maxBrakingRate_;
    }

    public int getMinBrakingRate() {
        return minBrakingRate_;
    }

    public int getMaxAccelerationRate() {
        return maxAccelerationRate_;
    }

    public int getMinAccelerationRate() {
        return minAccelerationRate_;
    }

    public boolean isWifi() {
        return wifi_;
    }

    public boolean isEmergencyVehicle() {
        return emergencyVehicle_;
    }

    public int getVehicleLength() {
        return vehicleLength_;
    }

    public int getMinTimeDistance() {
        return minTimeDistance_;
    }

    public int getMaxTimeDistance() {
        return maxTimeDistance_;
    }

    public int getMinPoliteness() {
        return minPoliteness_;
    }

    public int getMaxPoliteness() {
        return maxPoliteness_;
    }

    public String toString() {
        return name_;
    }

    public int getVehiclesDeviatingMaxSpeed_() {
        return vehiclesDeviatingMaxSpeed_;
    }

    public int getDeviationFromSpeedLimit_() {
        return deviationFromSpeedLimit_;
    }
}
