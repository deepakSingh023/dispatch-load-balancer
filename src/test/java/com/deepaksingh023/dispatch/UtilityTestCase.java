package com.deepaksingh023.dispatch;


import com.deepaksingh023.dispatch.util.HaversineFormula;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UtilityTestCase {

    HaversineFormula haversineFormula = new HaversineFormula();

    @Test
    public void checkForCorrectValue(){

        // The lat1 and lon1 are of Delhi and lat2 and lon2 are of gurugram
        double lat1 = 28.635307;
        double lon1 = 77.224960;
        double lat2 = 28.457523;
        double lon2 = 77.026344;

        //Solution found on vCalc Website
        double solFound = 27.7;

        assertEquals(solFound,haversineFormula.calculateDistance(lat1,lon1,lat2,lon2),0.1);

    }

    @Test
    public void checkForSameValue(){

        // Both the values are of delhi
        double lat1 = 28.635307;
        double lon1 = 77.224960;
        double lat2 = 28.635307;
        double lon2 = 77.224960;


        assertEquals(haversineFormula.calculateDistance(lat1,lon1,lat2,lon2),haversineFormula.calculateDistance(lat1,lon1,lat2,lon2));

    }

    @Test
    public void checkForOppositeValues(){

        // The lat2 and lon2 are of Delhi and lat1 and lon1 are of gurugram
        double lat2 = 28.635307;
        double lon2 = 77.224960;
        double lat1 = 28.457523;
        double lon1 = 77.026344;

        //Solution found on vCalc Website
        double solFound = 27.7;

        assertEquals(solFound,haversineFormula.calculateDistance(lat1,lon1,lat2,lon2),0.1);

    }



}
