public class BayPlaceholderEntity {
    private Truck truck;
    private int bayNumber;
    private Truck truckReadyToExitBay;

    public BayPlaceholderEntity(int bayNumber) {
        this.bayNumber = bayNumber;
    }

    //no setter for bay number; remains immutable
    public int getBayNumber() {
        return bayNumber;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Truck getTruckReadyToExitBay() {
        return truckReadyToExitBay;
    }

    public void setTruckReadyToExitBay(Truck truckReadyToExitBay) {
        this.truckReadyToExitBay = truckReadyToExitBay;
    }
}
