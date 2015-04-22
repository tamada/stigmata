package com.github.stigmata;

/**
 * This exception class will be thrown when birthmark storing is failed.
 * 
 * @author Haruaki Tamada
 */
public class BirthmarkStoreException extends BirthmarkException{
    private static final long serialVersionUID = -7106053809841281816L;

    public BirthmarkStoreException(){
    }

    public BirthmarkStoreException(String message, Throwable cause){
        super(message, cause);
    }

    public BirthmarkStoreException(String message){
        super(message);
    }

    public BirthmarkStoreException(Throwable cause){
        super(cause);
    }
}
