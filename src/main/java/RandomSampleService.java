import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomSampleService {

    private List<Integer> zeroToSevenSampler;
    private List<Integer> eightTo15Sampler;
    private List<Integer> sixteenTo23Sampler;

    //build PPS sampling distributions
    public void init() {
        List<Integer> tempList = new ArrayList<>();

        for (String pair : zeroToSevenArrivalDistribution) {
            String [] parts = pair.split(",");

            int value = Integer.parseInt(parts[0]);
            double probability = Double.parseDouble(parts[1]);
            long recordCount = Math.round(probability * 10000);

            //add value to tempList, number of adds=recordCount
            for (int x = 1; x <= recordCount; x++) {
                tempList.add(value);
            }
        }
        setZeroToSevenSampler(tempList);

        List<Integer> tempList2 = new ArrayList<>();
        for (String pair : eightTo15ArrivalDistribution) {
            String [] parts = pair.split(",");

            int value = Integer.parseInt(parts[0]);
            double probability = Double.parseDouble(parts[1]);
            long recordCount = Math.round(probability * 10000);

            //add value to tempList, number of adds=recordCount
            for (int x = 1; x <= recordCount; x++) {
                tempList2.add(value);
            }
        }
        setEightTo15Sampler(tempList2);

        List<Integer> tempList3 = new ArrayList<>();
        for (String pair : sixteenTo23ArrivalDistribution) {
            String [] parts = pair.split(",");

            int value = Integer.parseInt(parts[0]);
            double probability = Double.parseDouble(parts[1]);
            long recordCount = Math.round(probability * 10000);

            //add value to tempList, number of adds=recordCount
            for (int x = 1; x <= recordCount; x++) {
                tempList3.add(value);
            }
        }
        setSixteenTo23Sampler(tempList3);
    }

    public int getRandomArrivalSample(int hourNumber) {
        List<Integer> loaderList = null;

        if (hourNumber >= 0 && hourNumber <= 7) {
            loaderList = zeroToSevenSampler;
        } else if (hourNumber >= 8 && hourNumber <= 15) {
            loaderList = eightTo15Sampler;
        } else if (hourNumber >= 16 && hourNumber <= 23) {
            loaderList = sixteenTo23Sampler;
        }

        int size = loaderList.size();

        Random random = new Random();
        int randomSample = random.nextInt(size);

        return loaderList.get(randomSample);
    }

    public static int getRandomScaleProcessSample() {
        //returning 1 for now, until we have a true scale time. Currently the scale time is included in the scale to bay total time

        return 1;
    }

    public static int getRandomBaselineScaleToBayTime () {
        double mean = 1.804176;
        double stdDev = 0.671778;
        double rand = Math.random();

        NormalDistribution distribution = new NormalDistribution(mean, stdDev);
        double xVal = distribution.inverseCumulativeProbability(rand);

        int returnVal = (int) Math.round(xVal);
        if (returnVal == 0) {
            returnVal = 1;
        }

        return 1;
    }

    public static int getRandomScaleToYardTravelTime() {
        return 2;
    }

    public static int getRandomBayUnloadTime() {
        double distributionTypeProbability = Math.random();

        //probability <= 0.195 falls into the "wash" distribution
        //.195
        if (distributionTypeProbability <= .195) {

            double washMean = 65;
            double washStdDev = 12;
            double rand = Math.random();

            NormalDistribution distribution = new NormalDistribution(washMean, washStdDev);
            double xVal = distribution.inverseCumulativeProbability(rand);

            return (int) Math.round(xVal);
        } else {
            //explanation for this logic:
            //https://math.stackexchange.com/questions/1810257/gamma-functions-mean-and-standard-deviation-through-shape-and-rate

            double nonWashMean = 30;
            double nonWashStdDev = 5;

            //alpha ended up being too high, used custom values instead
            //19.7
            double alpha = 19.7;
            //1.55
            double beta = 1.55;
            double rand = Math.random();

            RealDistribution gammaDistribution = new GammaDistribution(alpha, beta);
            double xVal = gammaDistribution.inverseCumulativeProbability(rand);

            //create custom distribution upper/lower bounds
            if (xVal < 20) {
                xVal = 20;
            }

            if (xVal > 60) {
                xVal = 60;
            }

            return (int) Math.round(xVal);
        }
    }

    public static int getRandomBayToScaleTime() {
        double mean = 2.228515;
        double stdDev = 0.670913;
        double rand = Math.random();

        NormalDistribution distribution = new NormalDistribution(mean, stdDev);
        double xVal = distribution.inverseCumulativeProbability(rand);

        int returnVal = (int) Math.round(xVal);

        if (returnVal == 0) {
            returnVal = 1;
        }

        return 1;
    }

    public static int getRandomPreTripTime() {
        double rand = Math.random();

        NormalDistribution distribution = new NormalDistribution(17,3);
        double xVal = distribution.inverseCumulativeProbability(rand);

        if (xVal < 15) {
            return 15;
        } else if (xVal > 20) {
            return 20;
        } else {
            return (int) Math.round(xVal);
        }

    }

    public List<Integer> getZeroToSevenSampler() {
        return zeroToSevenSampler;
    }

    public void setZeroToSevenSampler(List<Integer> zeroToSevenSampler) {
        this.zeroToSevenSampler = zeroToSevenSampler;
    }

    public List<Integer> getEightTo15Sampler() {
        return eightTo15Sampler;
    }

    public void setEightTo15Sampler(List<Integer> eightTo15Sampler) {
        this.eightTo15Sampler = eightTo15Sampler;
    }

    public List<Integer> getSixteenTo23Sampler() {
        return sixteenTo23Sampler;
    }

    public void setSixteenTo23Sampler(List<Integer> sixteenTo23Sampler) {
        this.sixteenTo23Sampler = sixteenTo23Sampler;
    }

    private static final List<String> zeroToSevenArrivalDistribution = Arrays.asList(
            "1,0.029082774049217",
            "2,0.112527964205817",
            "3,0.0736017897091723",
            "4,0.0642058165548098",
            "5,0.0487695749440716",
            "6,0.0465324384787472",
            "7,0.0420581655480984",
            "8,0.0431767337807606",
            "9,0.0351230425055928",
            "10,0.0302013422818792",
            "11,0.032662192393736",
            "12,0.0322147651006711",
            "13,0.0275167785234899",
            "14,0.0250559284116331",
            "15,0.0225950782997763",
            "16,0.0203579418344519",
            "17,0.0228187919463087",
            "18,0.0196868008948546",
            "19,0.021923937360179",
            "20,0.016331096196868",
            "21,0.0154362416107383",
            "22,0.0145413870246085",
            "23,0.0147651006711409",
            "24,0.0123042505592841",
            "25,0.0136465324384787",
            "26,0.0136465324384787",
            "27,0.00984340044742729",
            "28,0.0100671140939597",
            "29,0.00827740492170022",
            "30,0.00827740492170022",
            "31,0.00894854586129754",
            "32,0.00827740492170022",
            "33,0.00894854586129754",
            "34,0.00805369127516779",
            "35,0.00782997762863535",
            "36,0.00313199105145414",
            "37,0.0058165548098434",
            "38,0.00492170022371365",
            "39,0.00536912751677852",
            "40,0.00402684563758389",
            "41,0.0029082774049217",
            "42,0.00357941834451902",
            "43,0.00425055928411633",
            "44,0.00268456375838926",
            "45,0.00357941834451902",
            "46,0.00111856823266219",
            "47,0.000894854586129754",
            "48,0.00469798657718121",
            "49,0.00178970917225951",
            "50,0.00223713646532438",
            "51,0.00246085011185682",
            "52,0.00178970917225951",
            "53,0.000671140939597315",
            "54,0.00134228187919463",
            "55,0.000894854586129754",
            "56,0.000671140939597315",
            "57,0.000894854586129754",
            "58,0.00134228187919463",
            "59,0.000894854586129754",
            "60,0.00111856823266219",
            "61,0.000671140939597315",
            "62,0.00156599552572707",
            "63,0.000671140939597315",
            "64,0.000447427293064877",
            "65,0",
            "66,0.000447427293064877",
            "67,0.000447427293064877",
            "68,0.000223713646532438",
            "69,0.00156599552572707",
            "70,0.000223713646532438",
            "71,0",
            "72,0.000223713646532438",
            "73,0.000223713646532438",
            "74,0",
            "75,0",
            "76,0.000447427293064877",
            "77,0",
            "78,0",
            "79,0",
            "80,0",
            "81,0",
            "82,0.000223713646532438",
            "83,0",
            "84,0",
            "85,0",
            "86,0.000223713646532438"
    );

    private static final List<String> eightTo15ArrivalDistribution = Arrays.asList(
            "1,0.0215545395166558",
            "2,0.117570215545395",
            "3,0.074678859133464",
            "4,0.0613977792292619",
            "5,0.0579142172871761",
            "6,0.0516002612671457",
            "7,0.0431090790333116",
            "8,0.0435445242760723",
            "9,0.0420204659264098",
            "10,0.0335292836925757",
            "11,0.0344001741780971",
            "12,0.0309166122360113",
            "13,0.0239494883518398",
            "14,0.0269976050511648",
            "15,0.0202482037883736",
            "16,0.0269976050511648",
            "17,0.0217722621380361",
            "18,0.0189418680600914",
            "19,0.0222077073807969",
            "20,0.0187241454387111",
            "21,0.0145874156324842",
            "22,0.0141519703897235",
            "23,0.012627912040061",
            "24,0.0117570215545395",
            "25,0.012627912040061",
            "26,0.0084911822338341",
            "27,0.010232963204877",
            "28,0.0108861310690181",
            "29,0.0067494012627912",
            "30,0.00783801436969301",
            "31,0.00740256912693229",
            "32,0.00522534291312867",
            "33,0.00413672980622687",
            "34,0.00609623339865012",
            "35,0.00631395602003048",
            "36,0.0056607881558894",
            "37,0.00370128456346614",
            "38,0.0028303940779447",
            "39,0.00457217504898759",
            "40,0.00544306553450904",
            "41,0.00544306553450904",
            "42,0.00391900718484651",
            "43,0.00217722621380361",
            "44,0.00348356194208578",
            "45,0.00326583932070542",
            "46,0.00261267145656434",
            "47,0.00108861310690181",
            "48,0.00239494883518398",
            "49,0.00130633572828217",
            "50,0.00152405834966253",
            "51,0.00130633572828217",
            "52,0.00130633572828217",
            "53,0.00108861310690181",
            "54,0.00174178097104289",
            "55,0.000653167864141084",
            "56,0.00152405834966253",
            "57,0.000870890485521446",
            "58,0.00130633572828217",
            "59,0.000653167864141084",
            "60,0.000653167864141084",
            "61,0.000870890485521446",
            "62,0.000435445242760723",
            "63,0",
            "64,0.000653167864141084",
            "65,0.000653167864141084",
            "66,0.000870890485521446",
            "67,0.000217722621380361",
            "68,0.000435445242760723",
            "69,0",
            "70,0",
            "71,0",
            "72,0.000435445242760723",
            "73,0",
            "74,0",
            "75,0.000653167864141084",
            "76,0.000435445242760723",
            "77,0.000435445242760723",
            "78,0.000435445242760723",
            "79,0.000435445242760723",
            "80,0",
            "81,0",
            "82,0",
            "83,0",
            "84,0.000217722621380361",
            "85,0",
            "86,0",
            "87,0.000435445242760723",
            "88,0.000435445242760723",
            "89,0",
            "90,0",
            "91,0",
            "92,0",
            "93,0",
            "94,0",
            "95,0",
            "96,0",
            "97,0",
            "98,0",
            "99,0",
            "100,0",
            "101,0",
            "102,0",
            "103,0.000217722621380361"
    );

    private static final List<String> sixteenTo23ArrivalDistribution = Arrays.asList(
            "1,0.0398074430660989",
            "2,0.131086835771153",
            "3,0.0762821699685243",
            "4,0.0718385484169598",
            "5,0.054989816700611",
            "6,0.0551749675985929",
            "7,0.043510461025736",
            "8,0.0472134789853731",
            "9,0.0401777448620626",
            "10,0.0359192742084799",
            "11,0.0322162562488428",
            "12,0.0288835400851694",
            "13,0.0270320311053509",
            "14,0.0288835400851694",
            "15,0.0246250694315867",
            "16,0.0185150897981855",
            "17,0.0216626550638771",
            "18,0.0187002406961674",
            "19,0.0199962969820404",
            "20,0.0194408442880948",
            "21,0.0192556933901129",
            "22,0.0148120718385484",
            "23,0.012775411960748",
            "24,0.0125902610627662",
            "25,0.0109239029809295",
            "26,0.00962784669505647",
            "27,0.00814663951120163",
            "28,0.00518422514349195",
            "29,0.00666543232734679",
            "30,0.00629513053138308",
            "31,0.00610997963340122",
            "32,0.00425847065358267",
            "33,0.00610997963340122",
            "34,0.00388816885761896",
            "35,0.00314756526569154",
            "36,0.00444362155156452",
            "37,0.00407331975560081",
            "38,0.00240696167376412",
            "39,0.00333271616367339",
            "40,0.0016663580818367",
            "41,0.00240696167376412",
            "42,0.00129605628587299",
            "43,0.00148120718385484",
            "44,0.00148120718385484",
            "45,0.0016663580818367",
            "46,0.00129605628587299",
            "47,0.00185150897981855",
            "48,0.00037030179596371",
            "49,0.00111090538789113",
            "50,0.00111090538789113",
            "51,0.000185150897981855",
            "52,0.000740603591927421",
            "53,0.000185150897981855",
            "54,0.000185150897981855",
            "55,0.00037030179596371",
            "56,0",
            "57,0.000555452693945566",
            "58,0.000740603591927421",
            "59,0",
            "60,0",
            "61,0.000185150897981855",
            "62,0.000185150897981855",
            "63,0",
            "64,0.000185150897981855",
            "65,0.00037030179596371",
            "66,0",
            "67,0",
            "68,0",
            "69,0",
            "70,0",
            "71,0",
            "72,0",
            "73,0",
            "74,0",
            "75,0.000185150897981855",
            "76,0.000185150897981855"
    );
}


