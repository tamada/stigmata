package com.github.stigmata;

/**
 * This exception represents occuring some exceptions caused birthmarking.
 * 
 * @author Haruaki TAMADA
 */
public class BirthmarkException extends Exception{
    private static final long serialVersionUID = -6422474631148213820L;

    public BirthmarkException(){
        super();
    }

    public BirthmarkException(String message, Throwable cause){
        super(message, cause);
    }

    public BirthmarkException(String message){
        super(message);
    }

    public BirthmarkException(Throwable cause){
        super(cause);
    }
}
