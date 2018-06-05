import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {

        List<Double> cumulativeResults = new ArrayList<>();
        List<Double> runningOutput = new ArrayList<>();

        for (int simRuns = 1; simRuns <= 1000; simRuns++) {
            RunResultEntity entity = SimController.simSetup(2, 2, true, .75);
            /*
            runningOutput.addAll(entity.getListOfDriverMinOnProperty());
            */
            runningOutput.addAll(entity.getListOfBayInToBayOutMin());

            /*
            //get std dev of growing list
            long runningSum = 0;
            for (double val : runningOutput) {
                runningSum = runningSum + ((long) val);
            }

            float mean = 1.0F * runningSum / runningOutput.size();

            //sum of squared distance
            double sumOfSquares = 0;
            for (double val : runningOutput) {
                double distanceToMean = val - mean;
                double squaredDis = Math.pow(distanceToMean, 2);
                sumOfSquares += squaredDis;
            }

            double meanOfDiffs = (double) sumOfSquares / (double) (runningOutput.size());
            cumulativeResults.add(Math.sqrt(meanOfDiffs));
            */
        }

        System.out.println(runningOutput);
    }
}
