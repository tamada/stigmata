package com.github.stigmata.cflib;

import org.objectweb.asm.Label;

/**
 * 
 * @author Haruaki Tamada
 */
public class LabelOpcode extends Opcode{
    private static final long serialVersionUID = -346783431316464L;

    public LabelOpcode(Label label){
        super(-1, "targeter", 0, 0, Category.TARGETER);
        super.addLabel(label);
    }

    @Override
    public final void addLabel(Label label){
        throw new NoSuchMethodError("illegal method call");
    }

    @Override
    public final void setLabels(Label[] label){
        throw new NoSuchMethodError("illegal method call");
    }

    public Label getLabel(){
        return getLabel(0);
    }
}
