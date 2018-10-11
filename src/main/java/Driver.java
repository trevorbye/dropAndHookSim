import java.util.Date;
import java.util.Random;

public class Driver {
    private int id;
    private Date propertyEntranceTime;
    private Date enterWaitForTruckQueueTime;
    private boolean isEngagedInActivity;
    private int activityTimeMin;

    public Driver() {
    }

    public Driver(boolean isEngagedInActivity) {
        this.isEngagedInActivity = isEngagedInActivity;

        Random random = new Random();
        int randomId = random.nextInt(1000000);

        this.id = randomId;
    }

    //no setter to remain immutable
    public int getId() {
        return id;
    }

    public Date getEnterWaitForTruckQueueTime() {
        return enterWaitForTruckQueueTime;
    }

    public void setEnterWaitForTruckQueueTime(Date enterWaitForTruckQueueTime) {
        this.enterWaitForTruckQueueTime = enterWaitForTruckQueueTime;
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
