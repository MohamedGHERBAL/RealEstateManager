package com.openclassrooms.realestatemanager;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {

    private final int dollar = 87;
    private final int euro = 93;

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    //----------------------------------------------------------------------------------------------
    // Currency Test
    //----------------------------------------------------------------------------------------------

    @Test
    public void convEuroToDollar_isCorrect() {
        int euro = 93;
        int dollar = 99;
        assertEquals(dollar, Utils.convertEuroToDollar(euro));
    }

    @Test
    public void convDollarsToEuro_isCorrect() {
        int euro = 87;
        int dollar = 107;
        assertEquals(euro, Utils.convertDollarToEuro(dollar));
    }

    //----------------------------------------------------------------------------------------------
    // Date Format Test
    //----------------------------------------------------------------------------------------------

    @Test
    public void dateFormat_isCorrect() {
        String date = "03/06/2022";
        assertEquals(date, Utils.getTodayDate());
    }

    @Test
    public void oldDateFormat_isNotCorrect() {
        String date = "2022/06/03";
        assertNotEquals(date, Utils.getTodayDate());
    }


}