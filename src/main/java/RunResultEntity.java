import java.util.ArrayList;
import java.util.List;

public class RunResultEntity {

    //driver summary stats
    private long totalDriverPropertyTimeHours;
    private double averageDriverPropertyTimeMin;

    //hostler summary stats
    private long totalHostlerIdleTimeHours;
    private double averageHostlerIdleTimePerDayMin;
    private int countOfHostlerNotAvailableToMoveFullLoad;

    //bay summary stats
    private double averageBayDowntimeMinPerDay;

    //iteratively add to these lists during the sim, then run calculations at end to set summary stat variables
    private List<Double> listOfDriverMinOnProperty = new ArrayList<>();

    public List<Double> getListOfDriverMinOnProperty() {
        return listOfDriverMinOnProperty;
    }

    public void setListOfDriverMinOnProperty(List<Double> listOfDriverMinOnProperty) {
        this.listOfDriverMinOnProperty = listOfDriverMinOnProperty;
    }

    public RunResultEntity() {
    }


    //other methods to calculate summary stats
}
