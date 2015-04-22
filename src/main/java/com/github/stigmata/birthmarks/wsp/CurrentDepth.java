package com.github.stigmata.birthmarks.wsp;

/**
 *
 * @author Haruaki Tamada
 */
public class CurrentDepth{
    private WSPOpcode opcode;
    private int depth;

    public CurrentDepth(int depth, WSPOpcode opcode){
        this.depth = depth;
        this.opcode = opcode;
    }

    public int getDepth(){
        return depth;
    }

    public WSPOpcode getOpcode(){
        return opcode;
    }

    @Override
    public String toString(){
        return String.format(
            "%d:%d:%d:%d", opcode.getOpcode(),
            depth, opcode.getWeight(), opcode.getAct()
        );
    }
}
