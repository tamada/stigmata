package com.github.stigmata;

/**
 * Thrown an application fails to launch in initialization.
 * This error causes invalid deployment. 
 *
 * @author Haruaki TAMADA
 */
public class ApplicationInitializationError extends Error{
    private static final long serialVersionUID = 32097456654328L;

    public ApplicationInitializationError(){
    }

    public ApplicationInitializationError(String message){
        super(message);
    }

    public ApplicationInitializationError(String message, Throwable cause){
        super(message, cause);
    }

    public ApplicationInitializationError(Throwable cause){
        super(cause);
    }
}
