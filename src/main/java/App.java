import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {

        List<ParamCombinationUtility> runParamCombinations = new ArrayList<>();
        for (int paramTrucks = 1; paramTrucks <= 8; paramTrucks++) {
            for (int paramHostlers = 1; paramHostlers <= 8; paramHostlers++) {
                runParamCombinations.add(new ParamCombinationUtility(paramTrucks, paramHostlers));
            }
        }

        List<ParamCombinationUtility> ouputList = new ArrayList<>();

        for (ParamCombinationUtility utility : runParamCombinations) {

            ParamCombinationUtility paramOutputObject = new ParamCombinationUtility(utility.getParamTrucks(), utility.getParamHostlers());

            List<Double> runningOutput = new ArrayList<>();
            List<Long> countOfHostlerUnavailableToMoveTruckIntoBayFromQueue = new ArrayList<>();
            List<Long> countOfHostlerUnavailableToMoveTruckAfterFinishedInBay = new ArrayList<>();

            for (int simRuns = 1; simRuns <= 100; simRuns++) {
                RunResultEntity entity = SimController.simSetup(utility.getParamTrucks(), utility.getParamHostlers(), false, .75);
                runningOutput.addAll(entity.getListOfDriverMinOnProperty());
            /*
            countOfHostlerUnavailableToMoveTruckIntoBayFromQueue.add(entity.getCountOfHostlerUnavailableToMoveTruckIntoBayFromQueue());
            countOfHostlerUnavailableToMoveTruckAfterFinishedInBay.add(entity.getCountOfHostlerUnavailableToMoveTruckAfterFinishedInBay());
            */
            }

            double sumOfTimes = 0;
            for (Double time : runningOutput) {
                sumOfTimes = sumOfTimes + time;
            }

            double average = sumOfTimes / runningOutput.size();
            paramOutputObject.setAverageTimeOnProperty(average);

            ouputList.add(paramOutputObject);
        }

        //print output
        for (ParamCombinationUtility paramCombinationUtility : ouputList) {
            System.out.println(paramCombinationUtility.getParamTrucks() + "," + paramCombinationUtility.getParamHostlers() + "," + paramCombinationUtility.getAverageTimeOnProperty());
        }

        /*
        System.out.println(runningOutput);
        System.out.println(countOfHostlerUnavailableToMoveTruckIntoBayFromQueue);
        System.out.println(countOfHostlerUnavailableToMoveTruckAfterFinishedInBay);
        */
    }
}
