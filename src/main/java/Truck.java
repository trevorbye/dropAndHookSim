import java.util.Date;
import java.util.Random;

public class Truck {
    private Driver driver;

    private Date queueEntranceTime;
    private Date waitForBayMarkerTime;
    private Date propertyEntranceTime;
    private int queueDurationMin;

    private boolean isPilotedByDriver;
    private boolean isPilotedByHostler;
    private boolean isEmpty;
    private boolean scaleWaitPenalty;

    //used during baseline runs, because the driver passes through the scale twice, and on the second time he exits the property
    private boolean hasPassedInitialScale;

    //probability assigns whether a truck is a local vs non-local delivery. Non local deliveries go through the baseline process and do not consume a hostler resource.
    private boolean isLocalDelivery;

    public Truck(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public Truck() {
    }

    //only create getter to ensure immutability
    public boolean isLocalDelivery() {
        return isLocalDelivery;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public boolean isScaleWaitPenalty() {
        return scaleWaitPenalty;
    }

    public void setScaleWaitPenalty(boolean scaleWaitPenalty) {
        this.scaleWaitPenalty = scaleWaitPenalty;
    }

    public Date getWaitForBayMarkerTime() {
        return waitForBayMarkerTime;
    }

    public void setWaitForBayMarkerTime(Date waitForBayMarkerTime) {
        this.waitForBayMarkerTime = waitForBayMarkerTime;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isHasPassedInitialScale() {
        return hasPassedInitialScale;
    }

    public void setHasPassedInitialScale(boolean hasPassedInitialScale) {
        this.hasPassedInitialScale = hasPassedInitialScale;
    }

    public Date getPropertyEntranceTime() {
        return propertyEntranceTime;
    }

    public void setPropertyEntranceTime(Date propertyEntranceTime) {
        this.propertyEntranceTime = propertyEntranceTime;
    }

    public Date getQueueEntranceTime() {
        return queueEntranceTime;
    }

    public void setQueueEntranceTime(Date queueEntranceTime) {
        this.queueEntranceTime = queueEntranceTime;
    }

    public int getQueueDurationMin() {
        return queueDurationMin;
    }

    public void setQueueDurationMin(int queueDurationMin) {
        this.queueDurationMin = queueDurationMin;
    }

    public boolean isPilotedByDriver() {
        return isPilotedByDriver;
    }

    public void setPilotedByDriver(boolean pilotedByDriver) {
        isPilotedByDriver = pilotedByDriver;
    }

    public boolean isPilotedByHostler() {
        return isPilotedByHostler;
    }

    public void setPilotedByHostler(boolean pilotedByHostler) {
        isPilotedByHostler = pilotedByHostler;
    }

    public boolean isEmpty(boolean b) {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void randomizeDeliveryType() {
        double randomProbability = Math.random();

        if (randomProbability < .15) {
            this.isLocalDelivery = false;
        } else {
            this.isLocalDelivery = true;
        }
    }
}