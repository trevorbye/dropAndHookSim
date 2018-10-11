import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {

/*
        RandomSampleService service = new RandomSampleService();
        service.init();
        service.arrivalSampleLoadCountTest(140);

*/


        List<Double> runningOutput = new ArrayList<>();

        //baseline runner
        for (int simRuns = 1; simRuns <= 30; simRuns++) {

            RunResultEntity entity = SimController.simSetup(0, 0, true, 0.5);
            runningOutput.addAll(entity.getListOfDriverMinOnProperty());

        }

        double sum = 0;
        for (double val : runningOutput) {
            sum = sum + val;
        }
        double mean = sum / runningOutput.size();

        System.out.print(runningOutput);

        /*

        List<ParamCombinationUtility> runParamCombinations = new ArrayList<>();
        for (int paramTrucks = 1; paramTrucks <= 8; paramTrucks++) {
            for (int paramHostlers = 1; paramHostlers <= 8; paramHostlers++) {
                runParamCombinations.add(new ParamCombinationUtility(paramTrucks, paramHostlers));
            }
        }

        List<ParamCombinationUtility> outputList = new ArrayList<>();

        for (ParamCombinationUtility utility : runParamCombinations) {

            ParamCombinationUtility paramOutputObject = new ParamCombinationUtility(utility.getParamTrucks(), utility.getParamHostlers());

            List<Double> listOfYearlyMinutes = new ArrayList<>();

            for (int simRuns = 1; simRuns <= 30; simRuns++) {
                List<Double> runningOutput = new ArrayList<>();

                RunResultEntity entity = SimController.simSetup(utility.getParamTrucks(), utility.getParamHostlers(), true, .75);
                runningOutput.addAll(entity.getListOfDriverMinOnProperty());

                double runningSum = 0;
                for (Double driverTime : runningOutput) {
                    runningSum = runningSum + driverTime;
                }

                listOfYearlyMinutes.add(runningSum);
            }

            double sumOfTimes = 0;
            for (Double time : listOfYearlyMinutes) {
                sumOfTimes = sumOfTimes + time;
            }

            double average = sumOfTimes / listOfYearlyMinutes.size();

            paramOutputObject.setAverageTimeOnProperty(average);
            outputList.add(paramOutputObject);
        }

        //print output
        for (ParamCombinationUtility paramCombinationUtility : outputList) {
            System.out.println(paramCombinationUtility.getParamTrucks() + "," + paramCombinationUtility.getParamHostlers() + "," + paramCombinationUtility.getAverageTimeOnProperty());
        }
        */

    }
}
