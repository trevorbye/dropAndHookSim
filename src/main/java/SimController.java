import org.apache.commons.lang3.time.DateUtils;

import java.util.*;

public class SimController {

    /**
     *
     * @param assetCount **Number of empty trucks (assets) to introduce into the simulation at start time
     * @param hostlerCount **Number of Hostler employees to introduce into the simulation
     * @param isBaselineRun **Switch to allow you to run a baseline sim vs. future state sim
     */
    public static RunResultEntity simSetup(int assetCount, int hostlerCount, boolean isBaselineRun, double bay5UtilizationPercent) {

        //initialize sim clock
        Date simDateTime = new Date("01/01/2018 00:00:00");

        /*
        * bin setup
        * */
        int maxHostlers = hostlerCount;
        int hostlerBin = maxHostlers;
        List<Integer> hostlerToYardWalkTimeMin = new ArrayList<>();
        List<Integer> yardToHostlerWalkTimeMin = new ArrayList<>();

        int maxYardCapacity = 6;
        List<Truck> yardBin = new ArrayList<>();

        //fill yard with empties equal to @param assetCount
        for (int x = 1; x <= assetCount; x++) {
            Truck emptyTruck = new Truck(true);
            yardBin.add(emptyTruck);
        }

        //create bay container and fill with BayPlaceHolderEntity objects with null trucks
        List<BayPlaceholderEntity> bayBin = new ArrayList<>();
        for (int spot = 1; spot <=5; spot++) {
            BayPlaceholderEntity entity = new BayPlaceholderEntity(spot);
            bayBin.add(entity);
        }

        /*
        *  queue setup
        * */
        //max=1truck
        Queue<Truck> truckArrivalQueue = new LinkedList<>();
        //add truck to queue with 9 min arrival countdown to start sim (median arrival time)
        Truck truck = new Truck(false);
        truck.setQueueDurationMin(9);
        truck.setPilotedByDriver(true);
        truck.setDriver(new Driver(false));
        truck.setHasPassedInitialScale(false);
        truck.randomizeDeliveryType();
        truckArrivalQueue.add(truck);

        //queue for waiting for the scale. Necessary because trucks coming from farms and bay out can both compete for the scale resource
        //NOT INCLUDED IN DISCRETE EVENT TIME STEP CHECK, just a FIFO queue
        Queue<Truck> scaleWaitQueue = new LinkedList<>();

        //actual scale process, max=1truck
        Queue<Truck> scaleProcessQueue = new LinkedList<>();

        //should usually only have one truck, but can potentially have >1 if trucks exit scale in quick succession
        Queue<Truck> scaleToYardTravelQueue = new LinkedList<>();

        //used for BASELINE_ assessment. Not used in future state runs.
        Queue<Truck> scaleToBayTravelQueue = new LinkedList<>();
        Queue<Truck> waitForOpenBayQueue = new LinkedList<>();

        //similar queues: truck gets added to YardCapQueue if there is no room to drop a full truck
        //or added to EmptyTruckQueue if there is not an empty truck available to leave with
        //NOT INCLUDED IN DISCRETE EVENT TIME STEP CHECK
        Queue<Driver> driverWaitForEmptyTruckQueue = new LinkedList<>();

        //time for driver to drop full truck in yard
        Queue<Truck> fullTruckDropQueue = new LinkedList<>();

        //time for hostler to drop empty truck in yard
        Queue<Truck> emptyTruckDropQueue = new LinkedList<>();

        //at this point, truck can be input into yardBin

        Queue<Truck> yardToBayTravelQueue = new LinkedList<>();

        //after this, truck can be input into bay

        //this queue loops back into scaleWaitQueue
        Queue<Truck> bayToScaleTravelQueue = new LinkedList<>();

        //all these queues/lists are entered as params to the runSimulation()
        return runSimulation(hostlerToYardWalkTimeMin, yardToHostlerWalkTimeMin, yardBin, bayBin, truckArrivalQueue, scaleWaitQueue, scaleProcessQueue, scaleToYardTravelQueue, driverWaitForEmptyTruckQueue,fullTruckDropQueue,emptyTruckDropQueue, yardToBayTravelQueue, bayToScaleTravelQueue,maxHostlers, hostlerBin,maxYardCapacity,
                simDateTime, isBaselineRun, scaleToBayTravelQueue, waitForOpenBayQueue, bay5UtilizationPercent);
    }

    public static RunResultEntity runSimulation(List<Integer> hostlerToYardWalkTimeMin, List<Integer> yardToHostlerWalkTimeMin, List<Truck> yardBin, List<BayPlaceholderEntity> bayBin
    ,Queue<Truck> truckArrivalQueue, Queue<Truck> scaleWaitQueue, Queue<Truck> scaleProcessQueue, Queue<Truck> scaleToYardTravelQueue, Queue<Driver> driverWaitForEmptyTruckQueue, Queue<Truck> fullTruckDropQueue, Queue<Truck> emptyTruckDropQueue, Queue<Truck> yardToBayTravelQueue, Queue<Truck> bayToScaleTravelQueue
    , int maxHostlers, int hostlerBin, int maxYardCapacity, Date simDateTime, boolean isBaselineRun, Queue<Truck> scaleToBayTravelQueue, Queue<Truck> waitForOpenBayQueue, double bayFiveUtilizationPercent) {

        //insert all event-based queues into List<Queue<Truck>> to pass into evaluateEventTimeSteps(). **not all queues are included in this if they aren't events that need to be stepped.
        List<Queue<Truck>> listOfEventQueues = new ArrayList<>();

        //different queues are used for BASELINE vs. non-BASELINE
        if (isBaselineRun) {
            listOfEventQueues.add(truckArrivalQueue);
            listOfEventQueues.add(scaleProcessQueue);
            listOfEventQueues.add(scaleToBayTravelQueue);
            listOfEventQueues.add(bayToScaleTravelQueue);
        } else {
            listOfEventQueues.add(truckArrivalQueue);
            listOfEventQueues.add(scaleProcessQueue);
            listOfEventQueues.add(scaleToBayTravelQueue);
            listOfEventQueues.add(scaleToYardTravelQueue);
            listOfEventQueues.add(bayToScaleTravelQueue);
        }

        //build run results object to return at end of sim run
        RunResultEntity runResultEntity = new RunResultEntity();
        //define end of sim
        Date simEndDateTime = new Date("01/30/2018 00:00:00");

        //define downtime hours list for bay #5 for BASELINE runs. This gets re-randomized for each new day in the sim
        List<Integer> bay5Downtimes = randomizeBay5Downtime(bayFiveUtilizationPercent);

        //initialize sample service object to avoid calling bloated constructor more than once
        RandomSampleService sampleService = new RandomSampleService();
        sampleService.init();

        //main sim loop
        while (simDateTime.before(simEndDateTime)) {
            Date priorDate = simDateTime;

            //step all events, step sim clock by same amount
            int stepValMin = evaluateEventTimeSteps(listOfEventQueues, bayBin, hostlerToYardWalkTimeMin, yardToHostlerWalkTimeMin);
            simDateTime = DateUtils.addMinutes(simDateTime, stepValMin);

            //compare priorDate and newly stepped simDateTime and re-randomize bay 5 downtimes if necessary
            Calendar t1 = Calendar.getInstance();
            t1.setTime(priorDate);
            int t1DayOfWeek = t1.get(Calendar.DAY_OF_WEEK);

            Calendar t2 = Calendar.getInstance();
            t2.setTime(simDateTime);
            int t2DayOfWeek = t2.get(Calendar.DAY_OF_WEEK);
            int currentSimHour = t2.get(Calendar.HOUR_OF_DAY);

            if (t1DayOfWeek != t2DayOfWeek) {
                bay5Downtimes.clear();
                bay5Downtimes = randomizeBay5Downtime(bayFiveUtilizationPercent);
            }

            /**
             * process all queues
             */

            //push truck arrival into scaleWaitQueue
            if (truckArrivalQueue.peek().getQueueDurationMin() == 0) {

                //remove truck from arrival queue, set property entrance time, and push to scaleWaitQueue
                Truck arrivingTruck = truckArrivalQueue.remove();
                arrivingTruck.setPropertyEntranceTime(simDateTime);
                arrivingTruck.getDriver().setPropertyEntranceTime(simDateTime);

                if (scaleWaitQueue.size() == 0) {
                    arrivingTruck.setScaleWaitPenalty(false);
                } else {
                    arrivingTruck.setScaleWaitPenalty(true);
                }

                scaleWaitQueue.add(arrivingTruck);

                //add new random truck to arrival queue
                Truck truck = new Truck(false);
                truck.setPilotedByDriver(true);
                truck.setDriver(new Driver(false));
                truck.setPilotedByHostler(false);
                truck.setQueueDurationMin(sampleService.getRandomArrivalSample(currentSimHour));
                truck.setHasPassedInitialScale(false);
                truck.randomizeDeliveryType();
                truckArrivalQueue.add(truck);
            }

            //if scaleProcessQueue is empty, or if current resource has a queueDuration=0, push next truck from scaleWaitQueue
            if (scaleProcessQueue.size() == 0 || scaleProcessQueue.peek().getQueueDurationMin() == 0) {
                //remove truck from scaleWaitQueue, randomize scale process duration, and add to queue
                if (scaleWaitQueue.peek() != null) {
                    Truck truckLeavingScaleWaitQueue = scaleWaitQueue.remove();

                    int queuePenalty = 0;
                    if (truckLeavingScaleWaitQueue.isScaleWaitPenalty()) {
                        queuePenalty = 0;
                    }

                    truckLeavingScaleWaitQueue.setQueueDurationMin(RandomSampleService.getRandomScaleProcessSample() + queuePenalty);
                    scaleProcessQueue.add(truckLeavingScaleWaitQueue);
                }
            }

            //check for Truck that is finished on the scale. if BASELINE_, push to scaleToBayTravelQueue
            //else, push to scaleToYardTravelQueue
            if (isBaselineRun) {
                if (scaleProcessQueue.size() > 0) {
                    if (scaleProcessQueue.peek().getQueueDurationMin() == 0) {
                        //first check if truck has already scaled once. if so, driver will be leaving (calculate property time and trash object)
                        if (scaleProcessQueue.peek().isHasPassedInitialScale()) {
                            Truck truckToTrash = scaleProcessQueue.remove();

                            long millisecondsOnProperty = simDateTime.getTime() - truckToTrash.getPropertyEntranceTime().getTime();
                            double minOnProperty = (millisecondsOnProperty / 1000) / 60;
                            runResultEntity.getListOfDriverMinOnProperty().add(minOnProperty);

                            /*
                            long millisecondsBayToScale = simDateTime.getTime() - truckToTrash.getQueueEntranceTime().getTime();
                            double min = (millisecondsBayToScale / 1000) / 60;
                            runResultEntity.getListOfBayToScaleMin().add(min);
                            */

                            //trash object
                            truckToTrash = null;

                        } else {
                            //get Truck and randomize travel time
                            Truck truckToMoveToBay = scaleProcessQueue.remove();
                            truckToMoveToBay.setHasPassedInitialScale(true);
                            truckToMoveToBay.setQueueDurationMin(RandomSampleService.getRandomBaselineScaleToBayTime());

                            //add truck to queue
                            scaleToBayTravelQueue.add(truckToMoveToBay);
                        }
                    }
                }
            } else {
                if (scaleProcessQueue.size() > 0) {
                    if (scaleProcessQueue.peek().getQueueDurationMin() == 0) {
                        Truck truckComingOffScale = scaleProcessQueue.remove();

                        if (truckComingOffScale.isLocalDelivery()) {
                            //if piloted by driver, he moves truck to bayqueue. But if piloted by hostler he moves truck to empty truck staging
                            if (truckComingOffScale.isPilotedByDriver()) {
                                truckComingOffScale.setQueueDurationMin(RandomSampleService.getRandomBaselineScaleToBayTime());
                                scaleToBayTravelQueue.add(truckComingOffScale);
                            } else {
                                truckComingOffScale.setQueueDurationMin(RandomSampleService.getRandomScaleToYardTravelTime());
                                scaleToYardTravelQueue.add(truckComingOffScale);
                            }
                        } else {
                            if (truckComingOffScale.isHasPassedInitialScale()) {
                                //truck leaves property
                                truckComingOffScale = null;
                            } else {
                                truckComingOffScale.setHasPassedInitialScale(true);
                                truckComingOffScale.setQueueDurationMin(RandomSampleService.getRandomBaselineScaleToBayTime());
                                scaleToBayTravelQueue.add(truckComingOffScale);
                            }
                        }

                    }
                }
            }

            //this logic only applies to future state.
            //Check scale to yard travel queue, if duration is zero then empty truck is ready to be dropped
            if (!isBaselineRun) {
                boolean keepScanning = true;

                while (keepScanning) {
                    if (scaleToYardTravelQueue.peek() != null && scaleToYardTravelQueue.peek().getQueueDurationMin() == 0) {
                        Truck truckToBeDroppedInYard = scaleToYardTravelQueue.remove();
                        truckToBeDroppedInYard.setPilotedByHostler(false);
                        yardBin.add(truckToBeDroppedInYard);
                    } else {
                        keepScanning = false;
                    }
                }

            }

            //if BASELINE_ push any trucks with queueDuration=0 from scaleToBayTravelQueue to waitForOpenBayQueue
            //else, push any trucks with queueDuration=0 from scaleToYardTravelQueue
            if (isBaselineRun) {
                if (scaleToBayTravelQueue.size() > 0) {
                    boolean keepScanning = true;

                    while (keepScanning) {
                        if (scaleToBayTravelQueue.peek() != null && scaleToBayTravelQueue.peek().getQueueDurationMin() == 0) {
                            Truck truckWaitingForBay = scaleToBayTravelQueue.remove();

                            //used to track queue penalty
                            truckWaitingForBay.setWaitForBayMarkerTime(simDateTime);
                            waitForOpenBayQueue.add(truckWaitingForBay);
                        } else {
                            keepScanning = false;
                        }
                    }
                }
            }

            //if BASELINE_, attempt to push trucks from waitForOpenBayQueue to bayBin
            if (isBaselineRun) {

                //first, for each bay, if current truck's duration is 0, move to truckReadyToExit slot
                for (BayPlaceholderEntity entity : bayBin) {
                    if (entity.getTruck() != null && entity.getTruck().getQueueDurationMin() == 0) {
                        entity.setTruckReadyToExitBay(entity.getTruck());
                        entity.setTruck(null);
                    }
                }

                //check bay 5 availability
                boolean bay5Available = false;

                //check bay 5 availability (index 4 in list) and then check against randomized downtime blocks
                if (bayBin.get(4).getTruck() == null) {
                    //check if current sim time is during a bay 5 randomized downtime interval
                    Calendar curr = Calendar.getInstance();
                    curr.setTime(simDateTime);
                    int currentHour = curr.get(Calendar.HOUR_OF_DAY);

                    boolean eligibleBay5Hour = true;
                    for (int downtimeHour : bay5Downtimes) {
                        if (downtimeHour == currentHour) {
                            eligibleBay5Hour = false;
                            break;
                        }
                    }

                    if (eligibleBay5Hour) {
                        bay5Available = true;
                    }
                }

                //push all trucks into bay if there is an available spot. Randomize bay duration if truck is pushed in

                //push truck to bay 5 if available
                if (bay5Available) {
                    if (waitForOpenBayQueue.size() > 0) {
                        Truck truckMovingToBay = waitForOpenBayQueue.remove();

                        int queuePenalty = 0;
                        if (truckMovingToBay.getWaitForBayMarkerTime() != simDateTime) {
                            //baseline calibration queuePenalty=3
                            queuePenalty = 3;
                        }

                        truckMovingToBay.setQueueDurationMin(RandomSampleService.getRandomBayUnloadTime() + queuePenalty);
                        bayBin.get(4).setTruck(truckMovingToBay);

                    }
                }

                //check other bays' availability and push trucks
                for (BayPlaceholderEntity entity : bayBin) {

                    if (entity.getBayNumber() != 5) {
                        if (entity.getTruck() == null) {
                            if (waitForOpenBayQueue.peek() != null) {
                                Truck truckToMove = waitForOpenBayQueue.remove();

                                int queuePenalty = 0;
                                if (truckToMove.getWaitForBayMarkerTime() != simDateTime) {
                                    //baseline calibration queuePenalty=3
                                    queuePenalty = 3;
                                }

                                truckToMove.setQueueDurationMin(RandomSampleService.getRandomBayUnloadTime() + queuePenalty);
                                entity.setTruck(truckToMove);
                            }
                        }
                    }
                }
            }

            //remove trucks from bay if they are finished unloading
            if (isBaselineRun) {
                int minTimeVal = 0;

                if (bayToScaleTravelQueue.size() > 0) {
                    minTimeVal = bayToScaleTravelQueue.peek().getQueueDurationMin();
                }

                for (BayPlaceholderEntity entity : bayBin) {
                    if (entity.getTruckReadyToExitBay() != null) {
                        Truck truckGoingToScale = new Truck();

                        truckGoingToScale.setPropertyEntranceTime(entity.getTruckReadyToExitBay().getPropertyEntranceTime());
                        truckGoingToScale.setHasPassedInitialScale(true);
                        truckGoingToScale.setQueueEntranceTime(simDateTime);

                        entity.setTruckReadyToExitBay(null);

                        if (bayToScaleTravelQueue.size() > 0) {
                            int newRandomSample = RandomSampleService.getRandomBayToScaleTime();

                            //if sample is less than current min time, set new sample time to current minimum
                            //else, new sample now becomes the minimum to check against
                            if (newRandomSample < minTimeVal) {
                                newRandomSample = minTimeVal;
                            } else {
                                minTimeVal = newRandomSample;
                            }

                            truckGoingToScale.setQueueDurationMin(newRandomSample);

                        } else {
                            int randomSample = RandomSampleService.getRandomBayToScaleTime();
                            truckGoingToScale.setQueueDurationMin(randomSample);
                            minTimeVal = randomSample;
                        }

                        bayToScaleTravelQueue.add(truckGoingToScale);
                    }
                }
            }

            //check all trucks in bayToScaleTravelQueue, add to scaleWaitQueue
            if (bayToScaleTravelQueue.size() > 0) {
                boolean continueScanning = true;

                while (continueScanning) {
                    if (bayToScaleTravelQueue.peek() != null && bayToScaleTravelQueue.peek().getQueueDurationMin() == 0) {
                        Truck truckToMoveToScaleWaitQueue = bayToScaleTravelQueue.remove();

                        if (scaleWaitQueue.size() > 0) {
                            truckToMoveToScaleWaitQueue.setScaleWaitPenalty(true);
                        } else {
                            truckToMoveToScaleWaitQueue.setScaleWaitPenalty(false);
                        }

                        scaleWaitQueue.add(truckToMoveToScaleWaitQueue);

                    } else {
                        continueScanning = false;
                    }
                }
            }

            //if scaleProcessQueue is empty, push next truck from scaleWaitQueue
            if (scaleProcessQueue.size() == 0) {
                if (scaleWaitQueue.peek() != null) {
                    Truck truckLeavingScaleWaitQueue = scaleWaitQueue.remove();

                    int queuePenalty = 0;
                    if (truckLeavingScaleWaitQueue.isScaleWaitPenalty()) {
                        //baseline calibration queuePenalty=2
                        queuePenalty = 2;
                    }

                    truckLeavingScaleWaitQueue.setQueueDurationMin(RandomSampleService.getRandomScaleProcessSample() + queuePenalty);
                    scaleProcessQueue.add(truckLeavingScaleWaitQueue);
                }
            }


            //check queue of drivers waiting for an empty truck
            if (!isBaselineRun) {
                int driverWaitQueueSize = driverWaitForEmptyTruckQueue.size();
                int yardBinSize = yardBin.size();

                if (yardBinSize > 0 && driverWaitQueueSize > 0) {
                    for (Truck truck : yardBin) {
                        if (driverWaitForEmptyTruckQueue.peek() == null) {
                            break;
                        } else {
                            Driver driver = driverWaitForEmptyTruckQueue.remove();
                            driver.setEngagedInActivty(true);
                            driver.setActivityTimeMin(RandomSampleService.getRandomPreTripTime());

                            truck.setDriver(driver);
                            truck.setPilotedByDriver(true);
                        }
                    }
                }

            }

            //check all trucks in yard. If driver pre trip is finished he can leave the property
            if (!isBaselineRun) {
                if (yardBin.size() > 0) {
                    List<Truck> trucksToRemove = new ArrayList<>();

                    for (Truck truck : yardBin) {
                        if (truck.getDriver().getActivityTimeMin() == 0) {
                            trucksToRemove.add(truck);

                            long millisecondsOnProperty = simDateTime.getTime() - truck.getDriver().getPropertyEntranceTime().getTime();
                            double minOnProperty = (millisecondsOnProperty / 1000) / 60;
                            runResultEntity.getListOfDriverMinOnProperty().add(minOnProperty);
                        }
                    }

                    yardBin.removeAll(trucksToRemove);
                }
            }
            
        }

        return runResultEntity;
    }

    /**
     *
     * @param listOfEventQueues **List of all Queue<Truck> built in queue setup
     * @param bayBin
     * @return **returns the the number of minutes to step sim clock by
     */
    public static int evaluateEventTimeSteps(List<Queue<Truck>> listOfEventQueues, List<BayPlaceholderEntity> bayBin, List<Integer> hostlerToYardWalkTimeMin, List<Integer> yardToHostlerWalkTimeMin) {
        int minTimeValueDeduction = 0;
        List<Integer> listOfTimeVals = new ArrayList<>();

        //add all time values from all param queues
        for (BayPlaceholderEntity bayPlaceholder : bayBin) {
            if (bayPlaceholder.getTruck() != null) {
                listOfTimeVals.add(bayPlaceholder.getTruck().getQueueDurationMin());
            }
        }

        for (Queue<Truck> queue : listOfEventQueues) {
            for (Truck truck : queue) {
                listOfTimeVals.add(truck.getQueueDurationMin());
            }
        }

        listOfTimeVals.addAll(hostlerToYardWalkTimeMin);
        listOfTimeVals.addAll(yardToHostlerWalkTimeMin);

        //sort collection ASC and get first element (min value)
        Collections.sort(listOfTimeVals);
        //this becomes the value you deduct from each random event duration, and also the amount of time you step the sim clock
        minTimeValueDeduction = listOfTimeVals.get(0);

        //subtract min val from each time value in each directory
        for (BayPlaceholderEntity bayPlaceholder : bayBin) {
            if (bayPlaceholder.getTruck() != null) {
                int currentDuration = bayPlaceholder.getTruck().getQueueDurationMin();
                bayPlaceholder.getTruck().setQueueDurationMin(currentDuration - minTimeValueDeduction);
            }
        }

        for (Queue<Truck> queue : listOfEventQueues) {
            for (Truck truck : queue) {
                int currentDuration = truck.getQueueDurationMin();
                truck.setQueueDurationMin(currentDuration - minTimeValueDeduction);
            }
        }

        for (Integer walkMin : hostlerToYardWalkTimeMin) {
            walkMin = walkMin - minTimeValueDeduction;
        }

        for (Integer walkMin : yardToHostlerWalkTimeMin) {
            walkMin = walkMin - minTimeValueDeduction;
        }

        return minTimeValueDeduction;
    }

    /**
     *
     * @param utilizationPercent **uptime utilization % for bay #5
     * @return ** returns list of int, which are hours that bay #5 cannot accept a truck
     */
    public static List<Integer> randomizeBay5Downtime(double utilizationPercent) {
        double percentOfDayAsHours = (1 - utilizationPercent) * 24;
        int totalDowntimeHoursRounded = (int) Math.round(percentOfDayAsHours);

        List<Integer> downtimeHoursList = new ArrayList<>();
        for (int x = 1; x <= totalDowntimeHoursRounded; x++) {
            Random random = new Random();
            int randomDowntime = random.nextInt(24);
            downtimeHoursList.add(randomDowntime);
        }

        return downtimeHoursList;
    }
}