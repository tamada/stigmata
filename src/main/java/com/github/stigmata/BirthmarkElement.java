package com.github.stigmata;

import java.io.Serializable;

/**
 * element of birthmark.
 * 
 * @author  Haruaki TAMADA
 */
public class BirthmarkElement implements Serializable{
    private static final long serialVersionUID = 943675475343245243L;

    /**
     * element value.
     */
    private String value;

    /**
     * construct birthmark element with given value. 
     */
    public BirthmarkElement(String value) {
        this.value = value;
    }

    /**
     * return the value of this element.
     */
    public Object getValue(){
        return value;
    }

    /**
     * to string.
     */
    @Override
    public String toString(){
        return String.valueOf(getValue());
    }

    /**
     * hash code for overriding equals method.
     */
    @Override
    public int hashCode(){
        if(getValue() == null){
            return 0;
        }
        else{
            return getValue().hashCode();
        }
    }

    /**
     * equals method.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof BirthmarkElement){
            if(getValue() != null){
                return getValue().equals(((BirthmarkElement)o).getValue());
            }
            else{
                return ((BirthmarkElement)o).getValue() == null;
            }
        }
        return false;
    }
}
