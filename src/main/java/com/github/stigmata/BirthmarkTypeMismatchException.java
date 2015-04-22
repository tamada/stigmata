package com.github.stigmata;

/**
 * This exception class be thrown when birthmark types are different in some
 * operation such as merging, and comparing.
 * 
 * @author Haruaki TAMADA
 */
public class BirthmarkTypeMismatchException extends BirthmarkException{
    private static final long serialVersionUID = -6198325117696243731L;

    public BirthmarkTypeMismatchException(){
        super();
    }

    public BirthmarkTypeMismatchException(String message, Throwable cause){
        super(message, cause);
    }

    public BirthmarkTypeMismatchException(String message){
        super(message);
    }

    public BirthmarkTypeMismatchException(Throwable cause){
        super(cause);
    }
}
