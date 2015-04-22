package com.github.stigmata.cflib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Label;

/**
 *
 * @author Haruaki Tamada
 */
public class Opcode implements Serializable, Iterable<Label>{
    private static final long serialVersionUID = -2349834745416345564L;

    public static enum Category{
        CONSTANT, LOAD, STORE, ARRAY, STACK, ADD, SUBTRACT, MULTIPLY, DIVIDE, REMAIN,
        NEGATE, SHIFT_LEFT, SHIFT_RIGHT, USHIFT_RIGHT, AND, OR, XOR, CAST, COMPARE,
        BRANCH, RETURN, FIELD, INVOKE, NEW, THROW, TARGETER, OTHERS,
    };
    private int opcode;
    private String name;
    private int argumentCount;
    private int act;
    private Category category;
    private List<Label> labels = new ArrayList<Label>();

    public Opcode(Opcode opcode){
        this(opcode.getOpcode(), opcode.getName(), opcode.getArgumentCount(), opcode.getAct(), opcode.getCategory());
    }

    public Opcode(int opcode, String name, int argumentCount, int act, String category){
        this(opcode, name, argumentCount, act, Category.valueOf(category));
    }

    public Opcode(int opcode, String name, int argumentCount, int act, Category category){
        this.opcode = opcode;
        this.name = name;
        this.argumentCount = argumentCount;
        this.act = act;
        this.category = category;
    }

    public final int getOpcode(){
        return opcode;
    }

    public final String getName(){
        return name;
    }

    public int getArgumentCount(){
        return argumentCount;
    }

    public void addLabel(Label label){
        if(label == null){
            throw new NullPointerException();
        }
        if(!(category == Category.TARGETER && labels.size() == 0) 
                && category != Category.BRANCH){
            throw new IllegalStateException("this method allows only branch category");
        }
        labels.add(label);
    }

    public void setLabels(Label[] labelArray){
        if(labelArray == null){
            throw new NullPointerException();
        }
        if(category != Category.BRANCH){
            throw new IllegalStateException("this method allows only branch category");
        }
        labels.clear();
        for(Label label: labelArray){
            if(label == null){
                throw new NullPointerException();
            }
            labels.add(label);
        }
    }

    public boolean hasLabel(Label label){
        return labels.contains(label);
    }

    public Label getLabel(int index){
        return labels.get(index);
    }

    public synchronized Label[] getLabels(){
        return labels.toArray(new Label[labels.size()]);
    }

    @Override
    public Iterator<Label> iterator(){
        return Collections.unmodifiableList(labels).iterator();
    }

    public void setAct(int act){
        if(category != Category.FIELD && category != Category.INVOKE){
            throw new IllegalStateException("setAct can be called only object and invoke category.");
        }
        this.act = act;
    }

    public int getAct(){
        return act;
    }

    public Category getCategory(){
        return category;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d:%s:%d(%s)", getOpcode(), getName(), getAct(), getCategory()));

        if(getCategory() == Category.BRANCH || getCategory() == Category.TARGETER){
            sb.append(labels);
        }

        return new String(sb);
    }
}
