package com.github.stigmata.digger;

public class ParseClassNameFailedException extends Exception{
    private static final long serialVersionUID = -5373395187256103446L;

    public ParseClassNameFailedException(){
        super();
    }

    public ParseClassNameFailedException(String message, Throwable cause){
        super(message, cause);
    }

    public ParseClassNameFailedException(String message){
        super(message);
    }

    public ParseClassNameFailedException(Throwable cause){
        super(cause);
    }
}
