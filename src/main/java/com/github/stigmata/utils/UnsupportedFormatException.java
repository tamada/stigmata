package com.github.stigmata.utils;

/**
 * 
 * @author Haruaki TAMADA
 */
public class UnsupportedFormatException extends Exception{
    private static final long serialVersionUID = 8165384564671102575L;

    public UnsupportedFormatException(){
    }

    public UnsupportedFormatException(String message){
        super(message);
    }

    public UnsupportedFormatException(Throwable cause){
        super(cause);
    }

    public UnsupportedFormatException(String message, Throwable cause){
        super(message, cause);
    }
}
