package com.github.stigmata.digger;

import java.io.File;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.stigmata.digger.ClasspathContext;

/**
 * test for ClasspathContext class.
 * 
 * @author Haruaki Tamada
 */
public class ClasspathContextTest{
    private static final String ASM_FILE = "target/asm-all-5.0.3.jar";
    private ClasspathContext context;

    @Before
    public void setup(){
        context = new ClasspathContext();
    }

    @Test
    public void testBasic() throws ClassNotFoundException{
        Assert.assertEquals(0, context.getClasspathSize());

        Class<?> classFileEntryClass = context.findClass("com.github.stigmata.digger.ClassFileEntry");
        Class<?> classpathContextClass = context.findClass("com.github.stigmata.digger.ClasspathContext");

        Assert.assertNotNull(classFileEntryClass);
        Assert.assertNotNull(classpathContextClass);
        Assert.assertNotNull(context.findEntry("com.github.stigmata.digger.ClassFileEntry"));
        Assert.assertNotNull(context.findEntry("com.github.stigmata.digger.ClasspathContext"));
    }

    @Test
    public void testBasic2() throws Exception{
        context.addClasspath(new File("target/classes").toURI().toURL());
        Assert.assertEquals(1, context.getClasspathSize());
        Assert.assertEquals(new File("target/classes").toURI().toURL(), context.getClasspathList()[0]);

        context.clear();
        Assert.assertEquals(0, context.getClasspathSize());
    }

    @Test
    public void testParent() throws Exception{
        ClasspathContext subContext = new ClasspathContext(context);
        context.addClasspath(new File("target/classes").toURI().toURL());
        subContext.addClasspath(new File(ASM_FILE).toURI().toURL());

        Assert.assertEquals(1, context.getClasspathSize());
        Assert.assertEquals(2, subContext.getClasspathSize());

        URL[] url = subContext.getClasspathList();
        Assert.assertEquals(new File("target/classes").toURI().toURL(), url[0]);
        Assert.assertEquals(new File(ASM_FILE).toURI().toURL(), url[1]);

        subContext.clear();
        Assert.assertEquals(1, subContext.getClasspathSize());

        subContext.clearAll();
        Assert.assertEquals(0, subContext.getClasspathSize());
    }

    @Test
    public void testParent2() throws Exception{
        ClasspathContext subContext = new ClasspathContext(context);
        context.addClasspath(new File("target/classes").toURI().toURL());
        subContext.addClasspath(new File(ASM_FILE).toURI().toURL());

        Assert.assertTrue(context.isIncludeSystemClasses());
        subContext.setIncludeSystemClasses(false);
        Assert.assertFalse(context.isIncludeSystemClasses());
    }

    @Test(expected=ClassNotFoundException.class)
    public void testNotExistedClass() throws Exception{
        context.findClass("not.exists.ClassName");
    }

    @Test
    public void testFindEntryNotExistedClass() throws Exception{
        Assert.assertFalse(context.hasEntry("not.exists.ClassName"));
        Assert.assertNull(context.findEntry("not.exists.ClassName"));
    }

    @Test
    public void testFindEntryNotSearchSystemClasspath() throws Exception{
        context.setIncludeSystemClasses(false);
        Assert.assertFalse(context.hasEntry("com.github.stigmata.digger.ClasspathContext"));
        Assert.assertNull(context.findEntry("com.github.stigmata.digger.ClasspathContext"));
    }

    @Test
    public void testFindEntryNotSearchSystemClasspath2() throws Exception{
        context.addClasspath(new File("target/classes").toURI().toURL());
        context.setIncludeSystemClasses(false);
        // fail because classpath was not set.
        Assert.assertFalse(context.hasEntry("org.objectweb.asm.ClassReader"));
        Assert.assertNull(context.findEntry("org.objectweb.asm.ClassReader"));
    }

    @Test
    public void testFindEntryNotSearchSystemClasspath3() throws Exception{
        context.addClasspath(new File("target/classes").toURI().toURL());
        context.setIncludeSystemClasses(false);
        // always success to load
        Assert.assertTrue(context.hasEntry("java.lang.Object"));
        Assert.assertNotNull(context.findClass("java.lang.Object"));
        // success because addClasspath method was called above.
        Assert.assertTrue(context.hasEntry("com.github.stigmata.digger.ClasspathContext"));
        Assert.assertNotNull(context.findClass("com.github.stigmata.digger.ClasspathContext"));
    }

    @Test
    public void testFindEntryNotSearchSystemClasspath4() throws Exception{
        context.addClasspath(new File("target/classes").toURI().toURL());
        context.addClasspath(new File(ASM_FILE).toURI().toURL());
        context.setIncludeSystemClasses(false);

        Assert.assertTrue(context.hasEntry("java.lang.Object"));
        Assert.assertNotNull(context.findClass("java.lang.Object"));
        Assert.assertTrue(context.hasEntry("com.github.stigmata.digger.ClasspathContext"));
        Assert.assertNotNull(context.findClass("com.github.stigmata.digger.ClasspathContext"));
        Assert.assertTrue(context.hasEntry("org.objectweb.asm.ClassReader"));
        Assert.assertNotNull(context.findClass("org.objectweb.asm.ClassReader"));
    }

    @Test(expected=ClassNotFoundException.class)
    public void testNotSearchSystemClasspath() throws Exception{
        context.setIncludeSystemClasses(false);
        context.findClass("com.github.stigmata.digger.ClasspathContext");
    }

    @Test(expected=ClassNotFoundException.class)
    public void testNotSearchSystemClasspath2() throws Exception{
        context.addClasspath(new File("target/classes").toURI().toURL());
        context.setIncludeSystemClasses(false);
        // fail because classpath was not set.
        context.findClass("org.objectweb.asm.ClassReader");
    }

    @Test
    public void testNotSearchSystemClasspath3() throws Exception{
        context.addClasspath(new File("target/classes").toURI().toURL());
        context.setIncludeSystemClasses(false);
        // always success to load
        Assert.assertNotNull(context.findClass("java.lang.Object"));
        // success because addClasspath method was called above.
        Assert.assertNotNull(context.findClass("com.github.stigmata.digger.ClasspathContext"));
    }

    @Test
    public void testNotSearchSystemClasspath4() throws Exception{
        context.addClasspath(new File("target/classes").toURI().toURL());
        context.addClasspath(new File(ASM_FILE).toURI().toURL());
        context.setIncludeSystemClasses(false);
        Assert.assertNotNull(context.findClass("java.lang.Object"));
        Assert.assertNotNull(context.findClass("com.github.stigmata.digger.ClasspathContext"));
        Assert.assertNotNull(context.findClass("org.objectweb.asm.ClassReader"));
    }

    @Test
    public void testSubContextBasic() throws ClassNotFoundException{
        ClasspathContext subContext = new ClasspathContext(context);
        Class<?> classFileEntryClass = subContext.findClass("com.github.stigmata.digger.ClassFileEntry");
        Class<?> classpathContextClass = subContext.findClass("com.github.stigmata.digger.ClasspathContext");

        Assert.assertNotNull(classFileEntryClass);
        Assert.assertNotNull(classpathContextClass);
    }

    @Test(expected=ClassNotFoundException.class)
    public void testSubContextNotExistedClass() throws Exception{
        ClasspathContext subContext = new ClasspathContext(context);
        subContext.findClass("not.exists.class");
    }

    @Test(expected=ClassNotFoundException.class)
    public void testSubContextNotSearchSystemClasspath() throws Exception{
        context.setIncludeSystemClasses(false);

        ClasspathContext subContext = new ClasspathContext(context);
        subContext.findClass("com.github.stigmata.digger.ClasspathContext");
    }

    @Test(expected=ClassNotFoundException.class)
    public void testSubContextNotSearchSystemClasspath2() throws Exception{
        context.addClasspath(new File("target/classes").toURI().toURL());
        context.setIncludeSystemClasses(false);

        ClasspathContext subContext = new ClasspathContext(context);
        // fail because classpath was not set.
        subContext.findClass("org.objectweb.asm.ClassReader");
    }

    public void testSubContextNotSearchSystemClasspath3() throws Exception{
        context.addClasspath(new File("target/classes").toURI().toURL());
        context.setIncludeSystemClasses(false);

        ClasspathContext subContext = new ClasspathContext(context);
        // always success to load
        Assert.assertNotNull(subContext.findClass("java.lang.Object"));
        // success because addClasspath method was called above.
        Assert.assertNotNull(subContext.findClass("com.github.stigmata.digger.ClasspathContext"));
    }

    @Test
    public void testSubContextNotSearchSystemClasspath4() throws Exception{
        context.addClasspath(new File("target/classes").toURI().toURL());
        context.addClasspath(new File(ASM_FILE).toURI().toURL());
        context.setIncludeSystemClasses(false);

        ClasspathContext subContext = new ClasspathContext(context);
        Assert.assertNotNull(subContext.findClass("java.lang.Object"));
        Assert.assertNotNull(subContext.findClass("com.github.stigmata.digger.ClasspathContext"));
        Assert.assertNotNull(subContext.findClass("org.objectweb.asm.ClassReader"));
    }

    @Test
    public void testWarLoader() throws Exception{
        context.addClasspath(new File("target/test-classes/resources/samplewar.war").toURI().toURL());

        Assert.assertNotNull(context.findClass("HelloWorld"));

        ClasspathContext subContext = new ClasspathContext(context);
        Assert.assertNotNull(subContext.findClass("HelloWorld"));
    }
}
