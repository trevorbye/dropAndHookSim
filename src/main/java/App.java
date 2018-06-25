import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {

        List<Double> runningOutput = new ArrayList<>();
        List<Long> countOfHostlerUnavailableToMoveTruckIntoBayFromQueue = new ArrayList<>();
        List<Long> countOfHostlerUnavailableToMoveTruckAfterFinishedInBay = new ArrayList<>();

        for (int simRuns = 1; simRuns <= 500; simRuns++) {
            RunResultEntity entity = SimController.simSetup(2, 2, true, .75);
            runningOutput.addAll(entity.getListOfDriverMinOnProperty());
            countOfHostlerUnavailableToMoveTruckIntoBayFromQueue.add(entity.getCountOfHostlerUnavailableToMoveTruckIntoBayFromQueue());
            countOfHostlerUnavailableToMoveTruckAfterFinishedInBay.add(entity.getCountOfHostlerUnavailableToMoveTruckAfterFinishedInBay());
        }

        System.out.println(runningOutput);
        System.out.println(countOfHostlerUnavailableToMoveTruckIntoBayFromQueue);
        System.out.println(countOfHostlerUnavailableToMoveTruckAfterFinishedInBay);
    }
}
