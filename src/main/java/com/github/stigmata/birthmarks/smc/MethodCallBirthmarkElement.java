package com.github.stigmata.birthmarks.smc;

import java.io.Serializable;

import com.github.stigmata.BirthmarkElement;

/**
 * 
 * @author Haruaki TAMADA
 */
public class MethodCallBirthmarkElement extends BirthmarkElement implements Serializable {
    private static final long serialVersionUID = -3178451461780859954L;

    private String className;
    private String methodName;
    private String signature;

    public MethodCallBirthmarkElement(String className, String methodName, String signature) {
        super(className + "#" + methodName);

        this.className = className;
        this.methodName = methodName;
        this.signature = signature;
    }

    public String getClassName(){
        return className;
    }

    public String getMethodName(){
        return methodName;
    }

    public String getSignature(){
        return signature;
    }

    @Override
    public Object getValue(){
        return getClassName() + "#" + getMethodName() + "!" + getSignature();
    }

    @Override
    public int hashCode(){
        return System.identityHashCode(this);
    }

    @Override
    public boolean equals(Object o){
        boolean flag = false;
        if(o instanceof MethodCallBirthmarkElement){
            MethodCallBirthmarkElement mcbe = (MethodCallBirthmarkElement)o;

            flag = getClassName().equals(mcbe.getClassName()) &&
                getMethodName().equals(mcbe.getMethodName())  &&
                getSignature().equals(mcbe.getSignature());
        }

        return flag;
    }
}
