package com.tms.persistence;

import com.tms.constant.TaxiStatus;
import com.tms.persistence.entity.Taxi;

import java.util.ArrayList;
import java.util.List;

public class TaxiRepositoryTestHelper {

    public static Taxi createTaxi(Double xPosition, Double yPosition) {
        Taxi taxi = new Taxi();
        taxi.setCurrXPos(xPosition);
        taxi.setCurrYPos(yPosition);
        taxi.setTaxiStatus(TaxiStatus.AVAILABLE);
        return taxi;
    }

    public static List<Taxi> createTaxis(int taxiCount) {
        List<Taxi> taxiList = new ArrayList<>();
        for (int index = 0; index < taxiCount; index++) {
            Taxi taxi = new Taxi();
            taxi.setCurrXPos((double) index);
            taxi.setCurrYPos((double) index);
            taxiList.add(taxi);
        }
        return taxiList;
    }

    public static List<Taxi> createTaxis(int taxiCount, TaxiStatus taxiStatus) {
        List<Taxi> taxiList = new ArrayList<>();
        for (int index = 0; index < taxiCount; index++) {
            Taxi taxi = new Taxi();
            taxi.setCurrXPos((double) index);
            taxi.setCurrYPos((double) index);
            taxi.setTaxiStatus(taxiStatus);
            taxiList.add(taxi);
        }
        return taxiList;
    }
}
