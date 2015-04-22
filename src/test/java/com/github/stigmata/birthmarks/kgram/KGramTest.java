package com.github.stigmata.birthmarks.kgram;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.stigmata.birthmarks.kgram.KGram;
import com.github.stigmata.birthmarks.kgram.KGramBuilder;

/**
 * 
 * @author Haruaki Tamada
 */
public class KGramTest{
    private String[] plainValues;
    private String[] complexValues;

    @Before
    public void buildKGrams(){
        plainValues = new String[] { "a", "b", "c", "d", "e", "f", "g", };
        complexValues = new String[] { "a", "b", "r", "a", "c", "a", "d", "a", "b", "r", "a", };
    }

    @Test
    public void testString(){
        KGram<String>[] kgrams = KGramBuilder.getInstance().buildKGram(plainValues, 4);
        Assert.assertEquals("a b c d", kgrams[0].toString());
        Assert.assertEquals("b c d e", kgrams[1].toString());
        Assert.assertEquals("c d e f", kgrams[2].toString());
        Assert.assertEquals("d e f g", kgrams[3].toString());
    }

    @Test
    public void testPlainKGram(){
        KGram<String>[] kgrams = KGramBuilder.getInstance().buildKGram(plainValues, 4);
        Assert.assertEquals(4, kgrams.length);

        Assert.assertEquals(4, kgrams[0].getKValue());
        Assert.assertEquals(4, kgrams[1].getKValue());
        Assert.assertEquals(4, kgrams[2].getKValue());
        Assert.assertEquals(4, kgrams[3].getKValue());

        Assert.assertArrayEquals(new String[] { "a", "b", "c", "d", }, kgrams[0].toArray());
        Assert.assertArrayEquals(new String[] { "b", "c", "d", "e", }, kgrams[1].toArray());
        Assert.assertArrayEquals(new String[] { "c", "d", "e", "f", }, kgrams[2].toArray());
        Assert.assertArrayEquals(new String[] { "d", "e", "f", "g", }, kgrams[3].toArray());
    }

    @Test
    public void testPlainKGram2(){
        KGram<String>[] kgrams = KGramBuilder.getInstance().buildKGram(plainValues, 3);
        Assert.assertEquals(5, kgrams.length);

        Assert.assertEquals(3, kgrams[0].getKValue());
        Assert.assertEquals(3, kgrams[1].getKValue());
        Assert.assertEquals(3, kgrams[2].getKValue());
        Assert.assertEquals(3, kgrams[3].getKValue());
        Assert.assertEquals(3, kgrams[4].getKValue());

        Assert.assertArrayEquals(new String[] { "a", "b", "c", }, kgrams[0].toArray());
        Assert.assertArrayEquals(new String[] { "b", "c", "d", }, kgrams[1].toArray());
        Assert.assertArrayEquals(new String[] { "c", "d", "e", }, kgrams[2].toArray());
        Assert.assertArrayEquals(new String[] { "d", "e", "f", }, kgrams[3].toArray());
        Assert.assertArrayEquals(new String[] { "e", "f", "g", }, kgrams[4].toArray());
    }

    @Test
    public void testComplexKGram(){
        KGram<String>[] kgrams = KGramBuilder.getInstance().buildKGram(complexValues, 3);

        Assert.assertEquals(7, kgrams.length);

        Assert.assertArrayEquals(new String[] { "a", "b", "r", }, kgrams[0].toArray());
        Assert.assertArrayEquals(new String[] { "b", "r", "a", }, kgrams[1].toArray());
        Assert.assertArrayEquals(new String[] { "r", "a", "c", }, kgrams[2].toArray());
        Assert.assertArrayEquals(new String[] { "a", "c", "a", }, kgrams[3].toArray());
        Assert.assertArrayEquals(new String[] { "c", "a", "d", }, kgrams[4].toArray());
        Assert.assertArrayEquals(new String[] { "a", "d", "a", }, kgrams[5].toArray());
        Assert.assertArrayEquals(new String[] { "d", "a", "b", }, kgrams[6].toArray());
        // following kgram is appeared in above.
        // assertEquals(new String[] { "a", "b", "r", }, kgrams[0].toArray());
        // assertEquals(new String[] { "b", "r", "a", }, kgrams[1].toArray());
    }
}
