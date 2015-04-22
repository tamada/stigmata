package com.github.stigmata.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.stigmata.utils.ArrayIterator;

public class ArrayIteratorTest{
    private ArrayIterator<String> iterator;
    private String[] data = new String[] {
        "one", "two", "three", "four", "five"
    };

    @Before
    public void setup(){
        iterator = new ArrayIterator<String>(data);
    }

    @Test
    public void testBasic(){
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(data[0], iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(data[1], iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(data[2], iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(data[3], iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(data[4], iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
}
