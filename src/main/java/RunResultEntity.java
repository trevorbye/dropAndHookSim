import java.util.ArrayList;
import java.util.List;

public class RunResultEntity {

    //driver summary stats
    private long totalDriverPropertyTimeHours;
    private double averageDriverPropertyTimePerDayMin;
    private int countDaysWithDriverWaitTime;

    //hostler summary stats
    private long totalHostlerIdleTimeHours;
    private double averageHostlerIdleTimePerDayMin;
    private int countOfHostlerNotAvailableToMoveFullLoad;

    //bay summary stats
    private double averageBayDowntimeMinPerDay;

    //iteratively add to these lists during the sim, then run calculations at end to set summary stat variables
    private List<Integer> totalDriverWaitMinPerDay = new ArrayList<>();

    public RunResultEntity() {
    }

    public List<Integer> getTotalDriverWaitMinPerDay() {
        return totalDriverWaitMinPerDay;
    }

    public void setTotalDriverWaitMinPerDay(List<Integer> totalDriverWaitMinPerDay) {
        this.totalDriverWaitMinPerDay = totalDriverWaitMinPerDay;
    }

    //other methods to calculate summary stats
}
