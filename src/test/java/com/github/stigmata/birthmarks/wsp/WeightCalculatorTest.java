package com.github.stigmata.birthmarks.wsp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.stigmata.birthmarks.wsp.WeightCalculator;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$
 */
public class WeightCalculatorTest{
    private WeightCalculator calculator;

    @Before
    public void setup(){
        calculator = new WeightCalculator();
    }

    @Test
    public void testCalculateWeightOfWcs(){
        int[][] wcs = new int[][] {
            {  6,  6,  9,  3, },
            { 21, 18, 18,  6, },
            {  9,  6, 12,  3, },
            {  6,  9, 30,  9, },
        };
        Assert.assertEquals(60, calculator.calculateWeight(wcs));
    }

    @Test
    public void testCalculateWeightOfWcs2(){
        int[][] wcs = new int[][] {
            { 16,  5,  0,  6, },
            {  3,  0, 15,  6, },
            {  2, 10,  0,  3, },
            {  0,  2,  3,  9, },
        };
        Assert.assertEquals(50, calculator.calculateWeight(wcs));
    }
}
