package com.github.stigmata;

/**
 *
 * @author Haruaki Tamada
 */
public class ExtractorNotFoundException extends BirthmarkException{
    private static final long serialVersionUID = 2050231007494812969L;

    public ExtractorNotFoundException(){
    }

    public ExtractorNotFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public ExtractorNotFoundException(String message){
        super(message);
    }

    public ExtractorNotFoundException(Throwable cause){
        super(cause);
    }
}
