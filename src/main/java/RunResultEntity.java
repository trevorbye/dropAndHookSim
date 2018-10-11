import java.util.ArrayList;
import java.util.List;

public class RunResultEntity {

    //driver summary stats
    private long totalDriverPropertyTimeHours;
    private double averageDriverPropertyTimeMin;

    //hostler summary stats
    private long countOfHostlerUnavailableToMoveTruckIntoBayFromQueue;
    private long countOfHostlerUnavailableToMoveTruckAfterFinishedInBay;

    private int maxYardSize;

    //iteratively add to these lists during the sim, then run calculations at end to set summary stat variables
    private List<Double> listOfDriverMinOnProperty = new ArrayList<>();

    private List<Double> listOfScaleInToBayOutMin = new ArrayList<>();

    private List<Double> listOfBayInToBayOutMin = new ArrayList<>();

    private List<Integer> listOfDriverId = new ArrayList<>();

    private List<Double> listOfDriverWaitMin = new ArrayList<>();

    private int occurencesOf3OrMoreTrucksInBay;

    public int getOccurencesOf3OrMoreTrucksInBay() {
        return occurencesOf3OrMoreTrucksInBay;
    }

    public void setOccurencesOf3OrMoreTrucksInBay(int occurencesOf3OrMoreTrucksInBay) {
        this.occurencesOf3OrMoreTrucksInBay = occurencesOf3OrMoreTrucksInBay;
    }

    public long getCountOfHostlerUnavailableToMoveTruckIntoBayFromQueue() {
        return countOfHostlerUnavailableToMoveTruckIntoBayFromQueue;
    }

    public int getMaxYardSize() {
        return maxYardSize;
    }

    public void setMaxYardSize(int maxYardSize) {
        this.maxYardSize = maxYardSize;
    }

    public void setCountOfHostlerUnavailableToMoveTruckIntoBayFromQueue(long countOfHostlerUnavailableToMoveTruckIntoBayFromQueue) {
        this.countOfHostlerUnavailableToMoveTruckIntoBayFromQueue = countOfHostlerUnavailableToMoveTruckIntoBayFromQueue;
    }

    public long getCountOfHostlerUnavailableToMoveTruckAfterFinishedInBay() {
        return countOfHostlerUnavailableToMoveTruckAfterFinishedInBay;
    }

    public void setCountOfHostlerUnavailableToMoveTruckAfterFinishedInBay(long countOfHostlerUnavailableToMoveTruckAfterFinishedInBay) {
        this.countOfHostlerUnavailableToMoveTruckAfterFinishedInBay = countOfHostlerUnavailableToMoveTruckAfterFinishedInBay;
    }

    public List<Double> getListOfDriverWaitMin() {
        return listOfDriverWaitMin;
    }

    public void setListOfDriverWaitMin(List<Double> listOfDriverWaitMin) {
        this.listOfDriverWaitMin = listOfDriverWaitMin;
    }

    public List<Integer> getListOfDriverId() {
        return listOfDriverId;
    }

    public void setListOfDriverId(List<Integer> listOfDriverId) {
        this.listOfDriverId = listOfDriverId;
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
