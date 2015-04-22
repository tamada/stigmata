package com.github.stigmata.birthmarks.wsp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.stigmata.birthmarks.wsp.CurrentDepth;
import com.github.stigmata.birthmarks.wsp.WSPOpcode;
import com.github.stigmata.cflib.Opcode;

/**
 *
 * @author Haruaki Tamada
 * @version $Revision$
 */
public class CurrentDepthTest{
    private CurrentDepth depth;
    private WSPOpcode opcode;

    @Before
    public void setup(){
        opcode = new WSPOpcode(4, "iconst_1", 0, 1, Opcode.Category.CONSTANT);
        depth = new CurrentDepth(3, opcode);
    }

    @Test
    public void testBasic(){
        Assert.assertEquals(3, depth.getDepth());
        Assert.assertEquals(opcode, depth.getOpcode());
    }
}
