package com.openclassrooms.realestatemanager;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void conversionEuroToDollar_isCorrect() {
        int euro = 93;
        int dollar = 107;
        assertEquals(dollar, Utils.convertEuroToDollar(euro));
    }

    @Test
    public void conversionDollarsToEuro_isCorrect() {
        int euro = 87;
        int dollar = 107;
        assertEquals(euro, Utils.convertDollarToEuro(dollar));
    }

}