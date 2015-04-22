package com.github.stigmata.cflib;

/*
 * $Id$
 */

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Label;

import com.github.stigmata.cflib.Opcode;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$
 */
public class OpcodeTest{
    private Opcode opcode;

    @Before
    public void setup(){
        opcode = new Opcode(26, "iload_0", 0, 1, Opcode.Category.LOAD);
    }

    @Test
    public void testBasic(){
        Assert.assertEquals(26, opcode.getOpcode());
        Assert.assertEquals("iload_0", opcode.getName());
        Assert.assertEquals(0, opcode.getArgumentCount());
        Assert.assertEquals(1, opcode.getAct());
        Assert.assertEquals(Opcode.Category.LOAD, opcode.getCategory());
    }

    @Test
    public void testSelfConstructor(){
        Opcode o = new Opcode(opcode);
        Assert.assertEquals(26, o.getOpcode());
        Assert.assertEquals("iload_0", o.getName());
        Assert.assertEquals(0, o.getArgumentCount());
        Assert.assertEquals(1, o.getAct());
        Assert.assertEquals(Opcode.Category.LOAD, o.getCategory());
    }

    @Test(expected=IllegalStateException.class)
    public void testSetActThrowException(){
        opcode.setAct(1);
    }

    @Test(expected=IllegalStateException.class)
    public void testAddLabelThrowException(){
        opcode.addLabel(new Label());
    }

    @Test(expected=NullPointerException.class)
    public void testAddLabelNullPointer(){
        opcode.addLabel(null);
    }

    @Test(expected=IllegalStateException.class)
    public void testSetLabelsThrowException(){
        opcode.setLabels(new Label[] { new Label() });
    }

    @Test(expected=NullPointerException.class)
    public void testSetLabelsNullPointer1(){
        opcode.setLabels(null);
    }

    @Test
    public void testAddLabel(){
        opcode = new Opcode(154, "ifne", 2, -1, "BRANCH");
        Label label1 = new Label();
        Label label2 = new Label();
        Label label3 = new Label();

        opcode.addLabel(label1);
        opcode.addLabel(label2);
        opcode.addLabel(label3);

        Assert.assertEquals(label1, opcode.getLabel(0));
        Assert.assertEquals(label2, opcode.getLabel(1));
        Assert.assertEquals(label3, opcode.getLabel(2));

        Iterator<Label> iterator = opcode.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(label1, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(label2, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(label3, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSetLabels(){
        opcode = new Opcode(154, "ifne", 2, -1, "BRANCH");
        Label label1 = new Label();
        Label label2 = new Label();
        Label label3 = new Label();

        opcode.addLabel(label1);
        opcode.setLabels(new Label[] { label1, label2, label3, });

        Assert.assertEquals(label1, opcode.getLabel(0));
        Assert.assertEquals(label2, opcode.getLabel(1));
        Assert.assertEquals(label3, opcode.getLabel(2));

        Iterator<Label> iterator = opcode.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(label1, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(label2, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(label3, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected=NullPointerException.class)
    public void testSetLabelsThrownNullPointerException(){
        opcode = new Opcode(154, "ifne", 2, -1, "BRANCH");

        opcode.setLabels(new Label[] { null, });
    }

    /**
     * this test will be thrown IllegalStateException.
     * Because, IllegalStateException is checked before null check of array elements.
     */
    @Test(expected=IllegalStateException.class)
    public void testSetLabelsNullPointer2(){
        opcode.setLabels(new Label[] { null, });
    }

    @Test
    public void testSetAct() throws Exception{
        opcode = new Opcode(182, "invokevirtual", 2, 0, "INVOKE");
        opcode.setAct(4);
    }
}
