package com.github.stigmata.digger;

import java.io.File;
import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.stigmata.digger.ClassFileEntry;
import com.github.stigmata.digger.ClasspathContext;

/**
 * 
 * @author Haruaki Tamada
 */
public class ClassFileEntryTest{
    private ClasspathContext context;

    @Before
    public void setup() throws MalformedURLException{
        context = new ClasspathContext();
    }

    @Test
    public void testBasic() throws Exception{
        Class<?> clazz = context.findClass("com.github.stigmata.digger.ClasspathContext");
        ClassFileEntry entry = context.findEntry("com.github.stigmata.digger.ClasspathContext");

        Assert.assertNotNull(clazz);
        Assert.assertNotNull(entry);

        File file = new File("target/classes/com/github/stigmata/digger/ClasspathContext.class");
        Assert.assertEquals("com.github.stigmata.digger.ClasspathContext", entry.getClassName());
        Assert.assertEquals(file.toURI().toURL(), entry.getLocation());
        Assert.assertNotNull(entry.openStream());
    }
}
