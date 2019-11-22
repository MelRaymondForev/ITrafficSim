package com.fifthgen.trafficsim.map;

import com.fifthgen.trafficsim.scenario.Vehicle;

public class VehicleActivatedTrafficLight extends TrafficLight {
    // We give priority to the main road, since it's where most of the car should be
    public static final double[] DEFAULT_SWITCH_INTERVALS = new double[]{6666, 1000, 3333};

    // Pressure from other road ?
    private boolean isActivated = false;

    public VehicleActivatedTrafficLight(Junction junction) {
        super(DEFAULT_SWITCH_INTERVALS, junction);
    }

    public VehicleActivatedTrafficLight(double redPhaseLength, double yellowPhaseLength, double greenPhaseLength, Junction junction) {
        super(DEFAULT_SWITCH_INTERVALS, junction);
    }

    @Override
    public void changePhases(int timePerStep) {
        if (isActivated || state > 0) { // pressure found
            super.changePhases(timePerStep);
            if (state > 0) isActivated = false;
        } else {
            Region[][] regions = Map.getInstance().getRegions();
            for (int i = 0; i < regions.length; i++) {
                for (int j = 0; j < regions[i].length; j++) {
                    // get all the vehicles
                    Vehicle[] vehicles = regions[i][j].getVehicleArray();
                    for (Vehicle vehicle : vehicles) {
                        // check if vehicle is in the junction (end or start)
                        if (junction_.getNode().equals(vehicle.getCurStreet().getStartNode()) || junction_.getNode().equals(vehicle.getCurStreet().getEndNode())) {
                            // check for non priority roads
                            if (vehicle.isWaitingForSignal_()) {
                                // vehicle waiting found, we cycle
                                isActivated = true;
                                state = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void calculateTrafficLightPosition(Street tmpStreet) {
        super.calculateTrafficLightPosition(tmpStreet);
    }

    @Override
    public double getGreenPhaseLength() {
        return super.getGreenPhaseLength();
    }

    @Override
    public void setGreenPhaseLength(double greenPhaseLength) {
        super.setGreenPhaseLength(greenPhaseLength);
    }

    @Override
    public double getYellowPhaseLength() {
        return super.getYellowPhaseLength();
    }

    @Override
    public void setYellowPhaseLength(double yellowPhaseLength) {
        super.setYellowPhaseLength(yellowPhaseLength);
    }

    @Override
    public double getRedPhaseLength() {
        return super.getRedPhaseLength();
    }

    @Override
    public void setRedPhaseLength(double redPhaseLength) {
        super.setRedPhaseLength(redPhaseLength);
    }

    @Override
    public int getState() {
        return super.getState();
    }

    @Override
    public void setState(int state) {
        super.setState(state);
    }

    @Override
    public Street[] getStreets_() {
        return super.getStreets_();
    }

    @Override
    public void setStreets_(Street[] streets_) {
        super.setStreets_(streets_);
    }
}
