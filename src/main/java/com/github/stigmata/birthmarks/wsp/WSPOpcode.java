package com.github.stigmata.birthmarks.wsp;

import com.github.stigmata.cflib.Opcode;

/**
 *
 *
 * @author Haruaki Tamada
 */
public class WSPOpcode extends Opcode {
    private static final long serialVersionUID = 31469629831901737L;

    private int weight;

    public WSPOpcode(int opcode, String name, int argumentCount,
                     int act, Category category) {
        super(opcode, name, argumentCount, act, category);
    }

    public WSPOpcode(int opcode, String name, int argumentCount,
                     int act, String category) {
        super(opcode, name, argumentCount, act, category);
    }

    public WSPOpcode(Opcode opcode) {
        super(opcode);
    }

    public WSPOpcode(int opcode, String name, int argumentCount,
                     int act, Category category, int weight){
        this(opcode, name, argumentCount, act, category);

        setWeight(weight);
    }

    public WSPOpcode(Opcode opcode, int weight){
        super(opcode);

        setWeight(weight);
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public int getWeight(){
        return weight;
    }

    @Override
    public String toString(){
        return String.format(
            "%d:%s:%d:%f(%s)", getOpcode(), getName(),
	    getWeight(), getAct(), getCategory()
        );
    }
}
