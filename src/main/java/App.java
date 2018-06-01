import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {

        List<Float> cumulativeAverage = new ArrayList<>();
        List<Double> runningOutput = new ArrayList<>();

        for (int simRuns = 1; simRuns <= 10000; simRuns++) {
            RunResultEntity entity = SimController.simSetup(2, 2, true, .75);
            runningOutput.addAll(entity.getListOfDriverMinOnProperty());

            //get average of growing list
            long runningSum = 0;
            for (double val : runningOutput) {
                runningSum = runningSum + ((long) val);
            }

            float mean = 1.0F * runningSum / runningOutput.size();


            RunUtilityEntity utilityEntity = new RunUtilityEntity();
            utilityEntity.setRunNumber(simRuns);
            utilityEntity.setUmulativeAverage(mean);

            cumulativeAverage.add(mean);
        }

        System.out.println(cumulativeAverage);
    }
}
