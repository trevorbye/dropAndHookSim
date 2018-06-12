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

    private List<Double> listOfScaleInToBayOutMin = new ArrayList<>();

    private List<Double> listOfBayInToBayOutMin = new ArrayList<>();

    private List<Integer> listOfArrivalHour = new ArrayList<>();

    private List<Double> listOfBayToScaleMin = new ArrayList<>();

    public List<Double> getListOfBayToScaleMin() {
        return listOfBayToScaleMin;
    }

    public void setListOfBayToScaleMin(List<Double> listOfBayToScaleMin) {
        this.listOfBayToScaleMin = listOfBayToScaleMin;
    }

    public List<Integer> getListOfArrivalHour() {
        return listOfArrivalHour;
    }

    public void setListOfArrivalHour(List<Integer> listOfArrivalHour) {
        this.listOfArrivalHour = listOfArrivalHour;
    }

    public List<Double> getListOfBayInToBayOutMin() {
        return listOfBayInToBayOutMin;
    }

    public void setListOfBayInToBayOutMin(List<Double> listOfBayInToBayOutMin) {
        this.listOfBayInToBayOutMin = listOfBayInToBayOutMin;
    }

    public List<Double> getListOfScaleInToBayOutMin() {
        return listOfScaleInToBayOutMin;
    }

    public void setListOfScaleInToBayOutMin(List<Double> listOfScaleInToBayOutMin) {
        this.listOfScaleInToBayOutMin = listOfScaleInToBayOutMin;
    }

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
