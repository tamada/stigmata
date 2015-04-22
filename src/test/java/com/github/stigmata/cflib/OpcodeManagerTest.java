package com.github.stigmata.cflib;

/*
 * $Id$
 */

import org.junit.Assert;
import org.junit.Test;

import com.github.stigmata.cflib.Opcode;
import com.github.stigmata.cflib.OpcodeManager;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$
 */
public class OpcodeManagerTest{
    @Test
    public void testBasic(){
        Assert.assertNotNull(OpcodeManager.getInstance());

        Opcode opcode = OpcodeManager.getInstance().getOpcode(182);

        Assert.assertEquals(182, opcode.getOpcode());
        Assert.assertEquals("invokevirtual", opcode.getName());
    }
}
