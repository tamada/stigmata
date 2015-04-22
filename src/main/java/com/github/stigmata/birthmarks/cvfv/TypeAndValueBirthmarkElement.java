package com.github.stigmata.birthmarks.cvfv;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import com.github.stigmata.BirthmarkElement;

/**
 * 
 * @author Haruaki TAMADA
 */
public class TypeAndValueBirthmarkElement extends BirthmarkElement implements Serializable{
    private static final long serialVersionUID = 237098465735321L;

    private String signature;
    private Serializable serialValue;
    private transient Object value;

    /**
     * @param value
     */
    public TypeAndValueBirthmarkElement(String signature, Object value){
        super(signature + "=" + value);
        this.signature = signature;
        setValue(value);
    }

    public String getSignature(){
        return signature;
    }

    public void setValue(Object value){
        this.value = value;
        if(signature.length() == 1 && value == null){
            switch(signature.charAt(0)){
            case 'Z': value = Boolean.FALSE;  break;
            case 'D': value = new Double(0d); break;
            case 'F': value = new Float(0f);  break;
            case 'C':
            case 'S':
            case 'B':
            case 'I':
            default:  value = new Integer(0); break;
            }
        }

        if(value != null && value instanceof Serializable){
            serialValue = (Serializable)value;
        }
    }

    @Override
    public Object getValue(){
        return value;
    }

    @Override
    public String toString(){
        return signature + "=" + value;
    }

    @Override
    public int hashCode(){
        return signature.hashCode() + value.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof TypeAndValueBirthmarkElement){
            TypeAndValueBirthmarkElement tvbe = (TypeAndValueBirthmarkElement)o;

            if(getSignature().equals(tvbe.getSignature())){
                if(getValue() == null && tvbe.getValue() == null){
                    return true;
                }
                else if(getValue() != null && tvbe.getValue() != null){
                    return getValue().equals(tvbe.getValue());
                }
            }
        }
        return false;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        if(serialValue != null){
            value = serialValue;
        }
    }
}
