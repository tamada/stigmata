package com.github.stigmata.birthmarks;

import com.github.stigmata.BirthmarkElement;

/**
 * 
 * @author Haruaki Tamada
 */
public class FrequencyBirthmarkElement extends BirthmarkElement implements ValueCountable{
    private static final long serialVersionUID = 4454345943098520436L;

    private int count = 1;

    public FrequencyBirthmarkElement(String value){
        super(parseValue(value));
        if(!value.equals(super.getValue())){
            int number = Integer.parseInt(value.substring(0, value.indexOf(":")));
            this.count = number;
        }
    }

    public FrequencyBirthmarkElement(String value, int count){
        super(value);
        this.count = count;
    }

    void incrementValueCount(){
        count++;
    }

    @Override
    public boolean equals(Object o){
        boolean flag = false;
        if(o instanceof FrequencyBirthmarkElement){
            FrequencyBirthmarkElement fmbe = (FrequencyBirthmarkElement)o;
            flag = super.equals(fmbe) && getValueCount() == fmbe.getValueCount();
        }
        return flag;
    }

    @Override
    public Object getValue(){
        return getValueCount() + ": " + getValueName();
    }

    @Override
    public int hashCode(){
        int hash = super.hashCode();
        int shift = getValueCount() % 32;

        // cyclic shift
        for(int i = 0; i < shift; i++){
            int v = hash & 1;
            hash = hash >>> 1 | v << 31;
        }

        return hash;
    }

    @Override
    public String getValueName(){
        return (String)super.getValue();
    }

    @Override
    public int getValueCount(){
        return count;
    }

    private static String parseValue(String value){
        if(value.indexOf(":") > 0){
            int index = value.indexOf(":");
            String val = value.substring(index + 1);

            return val.trim();
        }
        return value;
    }
}
