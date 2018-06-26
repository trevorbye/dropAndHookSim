public class ParamCombinationUtility {
    private int paramTrucks;
    private int paramHostlers;
    private double averageTimeOnProperty;

    public ParamCombinationUtility(int paramTrucks, int paramHostlers) {
        this.paramTrucks = paramTrucks;
        this.paramHostlers = paramHostlers;
    }

    public double getAverageTimeOnProperty() {
        return averageTimeOnProperty;
    }

    public void setAverageTimeOnProperty(double averageTimeOnProperty) {
        this.averageTimeOnProperty = averageTimeOnProperty;
    }

    public int getParamTrucks() {
        return paramTrucks;
    }

    public void setParamTrucks(int paramTrucks) {
        this.paramTrucks = paramTrucks;
    }

    public int getParamHostlers() {
        return paramHostlers;
    }

    public void setParamHostlers(int paramHostlers) {
        this.paramHostlers = paramHostlers;
    }
}
