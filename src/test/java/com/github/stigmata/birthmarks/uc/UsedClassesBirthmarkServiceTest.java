package com.github.stigmata.birthmarks.uc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator;

public class UsedClassesBirthmarkServiceTest{
    private UsedClassesBirthmarkService service;

    @Before
    public void setUp(){
        service = new UsedClassesBirthmarkService();
    }

    @Test
    public void testBasic(){
        Assert.assertEquals("uc", service.getType());
        Assert.assertEquals("Used classes birthmark", service.getDescription());
        Assert.assertFalse(service.isExperimental());
        Assert.assertFalse(service.isUserDefined());
        Assert.assertEquals(UsedClassesBirthmarkExtractor.class, service.getExtractor().getClass());
        Assert.assertEquals(LogicalAndBirthmarkComparator.class, service.getComparator().getClass());
        Assert.assertNull(service.getPreprocessor());
    }
}
