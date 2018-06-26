import java.util.Date;

public class Driver {
    private Date propertyEntranceTime;
    private boolean isEngagedInActivity;
    private int activityTimeMin;

    public Driver() {
    }

    public Driver(boolean isEngagedInActivity) {
        this.isEngagedInActivity = isEngagedInActivity;
    }

    public boolean isEngagedInActivity() {
        return isEngagedInActivity;
    }

    public void setEngagedInActivity(boolean engagedInActivity) {
        isEngagedInActivity = engagedInActivity;
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
