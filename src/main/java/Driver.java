import java.util.Date;

public class Driver {
    private Date propertyEntranceTime;
    private boolean isEngagedInActivty;
    private int activityTimeMin;

    public Driver() {
    }

    public Driver(boolean isEngagedInActivty) {
        this.isEngagedInActivty = isEngagedInActivty;
    }

    public boolean isEngagedInActivty() {
        return isEngagedInActivty;
    }

    public void setEngagedInActivty(boolean engagedInActivty) {
        isEngagedInActivty = engagedInActivty;
    }

    public int getActivityTimeMin() {
        return activityTimeMin;
    }

    public void setActivityTimeMin(int activityTimeMin) {
        this.activityTimeMin = activityTimeMin;
    }

    public Driver(Date propertyEntranceTime) {
        this.propertyEntranceTime = propertyEntranceTime;
    }

    public Date getPropertyEntranceTime() {
        return propertyEntranceTime;
    }

    public void setPropertyEntranceTime(Date propertyEntranceTime) {
        this.propertyEntranceTime = propertyEntranceTime;
    }
}
