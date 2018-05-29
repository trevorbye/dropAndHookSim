import java.util.Date;

public class Truck {
    private Date queueEntranceTime;
    private Date propertyEntranceTime;
    private int queueDurationMin;
    private boolean isPilotedByDriver;
    private boolean isPilotedByHostler;
    private boolean isEmpty;

    //used during baseline runs, because the driver passes through the scale twice, and on the second time he exits the property
    private boolean hasPassedInitialScale;

    public Truck(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public Truck() {
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
}
