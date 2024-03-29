package com.fifthgen.trafficsim.scenario;

public class KnownVehicle {

    private static int amountOfSavedBeacons_ = 10;

    private final Vehicle vehicle_;

    private final long ID_;

    private final int firstContact_;

    protected KnownVehicle previous_;

    protected KnownVehicle next_;

    private int x_;

    private int y_;

    private double speed_;

    private boolean isEncrypted_;

    private int lastUpdate_;

    private int[] savedX_;

    private int[] savedY_;

    private double[] savedSpeed_;

    private int[] savedLastUpdate_;

    private int arrayCounter = -1;

    public KnownVehicle(Vehicle vehicle, long ID, int x, int y, int time, double speed, boolean isEncrypted, int timePassed) {
        vehicle_ = vehicle;
        ID_ = ID;
        x_ = x;
        y_ = y;
        speed_ = speed;
        lastUpdate_ = time;
        isEncrypted_ = isEncrypted;
        firstContact_ = timePassed;
        if (amountOfSavedBeacons_ != -1) {
            arrayCounter = -1;
            savedX_ = new int[amountOfSavedBeacons_];
            savedY_ = new int[amountOfSavedBeacons_];
            savedSpeed_ = new double[amountOfSavedBeacons_];
            for (int i = 0; i < amountOfSavedBeacons_; i++) savedSpeed_[i] = -1;
            savedLastUpdate_ = new int[amountOfSavedBeacons_];
        }

    }

    public static int getAmountOfSavedBeacons_() {
        return amountOfSavedBeacons_;
    }

    public static void setAmountOfSavedBeacons(int amountOfSavedBeacons) {
        amountOfSavedBeacons_ = amountOfSavedBeacons;
    }

    public int getPersistentContactCount() {
        int beaconInterval = Vehicle.getBeaconInterval();
        int counter = 1;
        int savedLastUpdate = savedLastUpdate_[0];
        for (int i = 1; i < amountOfSavedBeacons_; i++) {
            if ((savedLastUpdate_[i] - savedLastUpdate) == beaconInterval) {
                counter++;
            }
            savedLastUpdate = savedLastUpdate_[i];

        }
        if ((savedLastUpdate_[0] - savedLastUpdate_[amountOfSavedBeacons_ - 1]) == beaconInterval) {
            counter++;
        }
        return counter;
    }

    public int[] getTimeStanding() {
        int[] counter = new int[2];

        for (int i = 0; i < amountOfSavedBeacons_; i++) {

            if (savedSpeed_[i] != -1) {
                counter[1]++;
                if (savedSpeed_[i] == 0) {
                    counter[0]++;
                }
            }
        }
        return counter;
    }

    public void showSpeedData() {
        int starter = arrayCounter + 1;
        if (starter == amountOfSavedBeacons_) starter = 0;
        for (int i = starter; i < (amountOfSavedBeacons_ + starter); i++) {
            System.out.println("savedspeed:" + savedSpeed_[i % (amountOfSavedBeacons_)]);
        }
    }

    public double[] getSpecificSpeedDataSet(int index) {
        double[] returnValue = new double[2];
        returnValue[0] = savedSpeed_[(arrayCounter + 1 + index) % amountOfSavedBeacons_];
        returnValue[1] = savedSpeed_[(amountOfSavedBeacons_ + arrayCounter) % (amountOfSavedBeacons_)];
        return returnValue;
    }

    public int getX() {
        return x_;
    }

    public void setX(int x) {
        x_ = x;
    }

    public int getY() {
        return y_;
    }

    public void setY(int y) {
        y_ = y;
    }

    public long getID() {
        return ID_;
    }

    public double getSpeed() {
        return speed_;
    }

    public void setSpeed(double speed) {
        speed_ = speed;
    }

    public Vehicle getVehicle() {
        return vehicle_;
    }

    public int getLastUpdate() {
        return lastUpdate_;
    }

    public void setLastUpdate(int time) {
        lastUpdate_ = time;
    }

    public KnownVehicle getNext() {
        return next_;
    }

    public void setNext(KnownVehicle next) {
        next_ = next;
    }

    public KnownVehicle getPrevious() {
        return previous_;
    }

    public void setPrevious(KnownVehicle previous) {
        previous_ = previous;
    }

    public void setEncrypted_(boolean isEncrypted_) {
        this.isEncrypted_ = isEncrypted_;
    }

    public int[] getSavedX_() {
        return savedX_;
    }

    public int[] getSavedY_() {
        return savedY_;
    }

    public double[] getSavedSpeed_() {
        return savedSpeed_;
    }

    public int[] getSavedLastUpdate_() {
        return savedLastUpdate_;
    }

    public int getArrayCounter() {
        return arrayCounter;
    }

    public void setArrayCounter(int arrayCounter) {
        this.arrayCounter = arrayCounter;
    }

    public int getFirstContact_() {
        return firstContact_;
    }

}