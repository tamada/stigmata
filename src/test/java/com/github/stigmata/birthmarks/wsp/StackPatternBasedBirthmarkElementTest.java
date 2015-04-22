package com.github.stigmata.birthmarks.wsp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.stigmata.birthmarks.wsp.CurrentDepth;
import com.github.stigmata.birthmarks.wsp.StackPatternBasedBirthmarkElement;
import com.github.stigmata.birthmarks.wsp.WSPOpcode;
import com.github.stigmata.cflib.Opcode;

/**
 *
 * @author Haruaki Tamada
 * @version $Revision$
 */
public class StackPatternBasedBirthmarkElementTest{
    private StackPatternBasedBirthmarkElement element;

    @Before
    public void setup(){
        CurrentDepth[] depthList = new CurrentDepth[7];

        depthList[0] = new CurrentDepth(1, new WSPOpcode( 26, "iload_0",      0,  1, Opcode.Category.LOAD, 7));
        depthList[1] = new CurrentDepth(2, new WSPOpcode( 26, "iload_0",      0,  1, Opcode.Category.LOAD, 7));
        depthList[2] = new CurrentDepth(3, new WSPOpcode(  4, "iconst_1",     0,  1, Opcode.Category.CONSTANT, 1));
        depthList[3] = new CurrentDepth(2, new WSPOpcode(100, "isub",         0, -1, Opcode.Category.SUBTRACT, 4));
        depthList[4] = new CurrentDepth(2, new WSPOpcode(184, "invokestatic", 2,  0, Opcode.Category.INVOKE, 1));
        depthList[5] = new CurrentDepth(1, new WSPOpcode(104, "imul",         0, -1, Opcode.Category.MULTIPLY, 6));
        depthList[6] = new CurrentDepth(0, new WSPOpcode(172, "ireturn",      0, -1, Opcode.Category.RETURN, 2));

        element = new StackPatternBasedBirthmarkElement(depthList);
    }

    @Test
    public void testBasic() throws Exception{
        Assert.assertEquals(7, element.getLength());
        Assert.assertEquals(28, element.getWeight());

        Assert.assertEquals(1, element.getDepth(0).getDepth());
        Assert.assertEquals(2, element.getDepth(1).getDepth());
        Assert.assertEquals(3, element.getDepth(2).getDepth());
        Assert.assertEquals(2, element.getDepth(3).getDepth());
        Assert.assertEquals(2, element.getDepth(4).getDepth());
        Assert.assertEquals(1, element.getDepth(5).getDepth());
        Assert.assertEquals(0, element.getDepth(6).getDepth());
    }

    @Test
    public void testBuildFromString() throws Exception{
        StackPatternBasedBirthmarkElement element2 = new StackPatternBasedBirthmarkElement(element.toString());

        Assert.assertEquals(7, element2.getLength());
        Assert.assertEquals(28, element2.getWeight());

        Assert.assertEquals(1, element2.getDepth(0).getDepth());
        Assert.assertEquals(2, element2.getDepth(1).getDepth());
        Assert.assertEquals(3, element2.getDepth(2).getDepth());
        Assert.assertEquals(2, element2.getDepth(3).getDepth());
        Assert.assertEquals(2, element2.getDepth(4).getDepth());
        Assert.assertEquals(1, element2.getDepth(5).getDepth());
        Assert.assertEquals(0, element2.getDepth(6).getDepth());
    }

    @Test
    public void testCalculateWeightedCommonSubsequence(){
        CurrentDepth[] depthList = new CurrentDepth[10];
        depthList[0] = new CurrentDepth(1, new WSPOpcode( 26, "iload_0",      0,  1, Opcode.Category.LOAD, 7));
        depthList[1] = new CurrentDepth(2, new WSPOpcode(  4, "iconst_1",     0,  1, Opcode.Category.CONSTANT, 1));
        depthList[2] = new CurrentDepth(1, new WSPOpcode(100, "isub",         0, -1, Opcode.Category.SUBTRACT, 4));
        depthList[3] = new CurrentDepth(1, new WSPOpcode(184, "invokestatic", 2,  0, Opcode.Category.INVOKE, 1));
        depthList[4] = new CurrentDepth(2, new WSPOpcode( 26, "iload_0",      0,  1, Opcode.Category.LOAD, 7));
        depthList[5] = new CurrentDepth(3, new WSPOpcode(  5, "iconst_2",     0,  1, Opcode.Category.CONSTANT, 1));
        depthList[6] = new CurrentDepth(2, new WSPOpcode(100, "isub",         0, -1, Opcode.Category.SUBTRACT, 4));
        depthList[7] = new CurrentDepth(2, new WSPOpcode(184, "invokestatic", 2,  0, Opcode.Category.INVOKE, 1));
        depthList[8] = new CurrentDepth(1, new WSPOpcode( 96, "iadd",         0, -1, Opcode.Category.ADD, 3));
        depthList[9] = new CurrentDepth(0, new WSPOpcode(172, "ireturn",      0, -1, Opcode.Category.RETURN, 2));
        StackPatternBasedBirthmarkElement pattern2 = new StackPatternBasedBirthmarkElement(depthList);

        Assert.assertEquals(21, element.getWeight(pattern2));
        Assert.assertEquals(21, pattern2.getWeight(element));
    }
}
