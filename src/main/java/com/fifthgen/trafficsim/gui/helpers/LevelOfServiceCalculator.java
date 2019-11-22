package com.fifthgen.trafficsim.gui.helpers;

import com.fifthgen.trafficsim.gui.Renderer;
import com.fifthgen.trafficsim.gui.controlpanels.LevelOfServiceControlPanel;
import com.fifthgen.trafficsim.map.Map;
import com.fifthgen.trafficsim.map.Region;
import com.fifthgen.trafficsim.map.Street;
import com.fifthgen.trafficsim.scenario.Vehicle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.fifthgen.trafficsim.gui.controlpanels.LevelOfServiceControlPanel.*;

public class LevelOfServiceCalculator extends Thread {
    // The time between two LOS calculation
    public final static int SLEEP_TIME = 1000;

    public final static int[] FIFTY_FIVE = {44, 37, 28, 22, 17, 17};
    public final static int[] FIFTY = {40, 34, 25, 20, 15, 15};
    public final static int[] FORTY_FIVE = {36, 30, 23, 18, 14, 14};
    public final static int[] FORTY = {32, 27, 20, 16, 12, 12};
    public final static int[] THIRTY_FIVE = {28, 23, 18, 14, 11, 11};
    public final static int[] THIRTY = {24, 20, 15, 12, 9, 9};
    public final static int[] TWENTY_FIVE = {20, 17, 13, 10, 8, 8};

    public static boolean LOS = false;

    public static HashMap<Street, List<Vehicle>> vehiclesInStreets;

    public LevelOfServiceCalculator() {
        vehiclesInStreets = new HashMap<>();
    }

    @Override
    public void run() {
        LOS = true;
        int[] levelOfServices = new int[6];
        while (true) {
            double allSpeed = 0; // Speed of all the vehicules
            int allVehicules = 0; // Number of vehicles
            Region[][] regions = Map.getInstance().getRegions();
            // we clear the list of each key of the map
            clearVehicules();
            Arrays.fill(levelOfServices, 0);
            for (int i = 0; i < regions.length; i++) {
                for (int j = 0; j < regions[i].length; j++) {
                    Street[] streets = regions[i][j].getStreets();
                    for (Street street : streets) {
                        // clear all streets color
                        street.setDisplayColor(DEFAULT_COLOR);
                    }
                    // we add each vehicle of the map in the corresponding street
                    Vehicle[] vehicles = regions[i][j].getVehicleArray();
                    for (int k = 0; k < vehicles.length; k++) {
                        Street currentStreet = vehicles[k].getCurStreet();
                        List<Vehicle> vehiculesList = vehiclesInStreets.get(currentStreet);
                        if (vehiculesList == null) {
                            vehiculesList = new ArrayList<>(4); //
                            vehiculesList.add(vehicles[k]);
                            vehiclesInStreets.put(currentStreet, vehiculesList);
                        } else {
                            vehiclesInStreets.get(currentStreet).add(vehicles[k]);
                        }
//                        System.out.println("Number of vehicles in " +currentStreet+ " " +vehiculesList);
                    }
//                    for (Street street : streets) {
//                        if (vehiclesInStreets.get(street) != null)
//                            TOTAL_VEHICLES.setText("<html><b>" + vehiclesInStreets.get(street) + "</b></html>");
//                    }
                }
            }
            // for each street containing at least a vehicle, we calculate its average speed in km/h (totalSpeed / noVehicle)
            // we then set the street's color to the appropriate level of service
            for (Street street : vehiclesInStreets.keySet()) {
                if (vehiclesInStreets.get(street).size() > 0) {
                    double averageSpeed = 0.0d;
                    for (Vehicle vehicle : vehiclesInStreets.get(street)) {
                        averageSpeed += vehicle.getCurSpeed();
                        allSpeed += vehicle.getCurSpeed();
                        allVehicules++;
                    }
                    averageSpeed /= vehiclesInStreets.get(street).size();
                    int los = setColorFromAverageSpeed(street, averageSpeed / 100000 * 3600);
                    levelOfServices[los] += vehiclesInStreets.get(street).size();;
                }
            }

//            TOTAL_VEHICLES.setText("<html><b>" + vehiclesInStreets + "</b></html><br>");

            LOS_A_COUNT.setText("<html><b>" + levelOfServices[0] + "</b></html>");
            LOS_B_COUNT.setText("<html><b>" + levelOfServices[1] + "</b></html>");
            LOS_C_COUNT.setText("<html><b>" + levelOfServices[2] + "</b></html>");
            LOS_D_COUNT.setText("<html><b>" + levelOfServices[3] + "</b></html>");
            LOS_E_COUNT.setText("<html><b>" + levelOfServices[4] + "</b></html>");
            LOS_F_COUNT.setText("<html><b>" + levelOfServices[5] + "</b></html>");


            if (allVehicules > 0) {
                AVERAGE_SPEED.setText("<html><b>" + (int) ((allSpeed / allVehicules) / 100000 * 3600) + "km/h</b></html>");
//                STREET_ID.setText("<html><b>" + (int) (allSpeed) + "</b></html>");
//                TOTAL_VEHICLES.setText("<html><b>" + (int) (allVehicules) + "</b></html>");
            } else {
                AVERAGE_SPEED.setText("<html><b>No vehicles</b></html>");
//                STREET_ID.setText("<html><b>No vehicles</b></html>");
//                TOTAL_VEHICLES.setText("<html><b>No vehicles</b></html>");
            }
            Renderer.getInstance().ReRender(true, false);
            try {
                // let the cpu do other tasks
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearVehicules() {
        for (List<Vehicle> vehicles : vehiclesInStreets.values()) {
            vehicles.clear();
        }
    }

    // set back all streets to normal colors
    public void finish() {
        LOS = false;
        Region[][] regions = Map.getInstance().getRegions();
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[i].length; j++) {
                Street[] streets = regions[i][j].getStreets();
                for (Street street : streets) {
                    // clear all streets color
                    street.setDisplayColor(null);
                }
            }
        }
    }

    /**
     * Change the color of the street depending of the average speed of the road
     * @param averageSpeed the average speed of all the vehicles in the road
     */
    private int setColorFromAverageSpeed(Street street, double averageSpeed) {
        int levelOfService;
        double realSpeed = street.getSpeed() / 100000d * 3600;
        if (realSpeed >= 55) {
            levelOfService = getLevelOfService(FIFTY_FIVE, averageSpeed);
        } else if (realSpeed > 50) {
            levelOfService = getLevelOfService(FIFTY, averageSpeed);
        } else if (realSpeed > 45) {
            levelOfService = getLevelOfService(FORTY_FIVE, averageSpeed);
        } else if (realSpeed > 40) {
            levelOfService = getLevelOfService(FORTY, averageSpeed);
        } else if (realSpeed > 35) {
            levelOfService = getLevelOfService(THIRTY_FIVE, averageSpeed);
        } else if (realSpeed > 30) {
            levelOfService = getLevelOfService(THIRTY, averageSpeed);
        } else {
            levelOfService = getLevelOfService(TWENTY_FIVE, averageSpeed);
        }

        setColorFromLevelOfService(street, levelOfService);
        return levelOfService;
    }

    /**
     * Change the color of the street depending of the level of service
     * @param levelOfService integer from 0 (LOS-A) to 5 (LOS-F)
     */
    private void setColorFromLevelOfService(Street street, int levelOfService) {
        switch (levelOfService) {
            case 0: {
                street.setDisplayColor(LOS_A_COLOR);
                break;
            }
            case 1: {
                street.setDisplayColor(LOS_B_COLOR);
                break;
            }
            case 2: {
                street.setDisplayColor(LOS_C_COLOR);
                break;
            }
            case 3: {
                street.setDisplayColor(LOS_D_COLOR);
                break;
            }
            case 4: {
                street.setDisplayColor(LOS_E_COLOR);
                break;
            }
            case 5: {
                street.setDisplayColor(LOS_F_COLOR);
                break;
            }
            default: {
                street.setDisplayColor(DEFAULT_COLOR);
                break;
            }
        }
    }

    private int getLevelOfService(int[] flowSpeed, double average) {
        for (int i = 0; i < flowSpeed.length - 1; i++) {
            if (average > flowSpeed[i]) {
                return i;
            }
        }
        return flowSpeed.length - 1;
    }
}
