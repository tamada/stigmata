package com.github.stigmata.hook;

import com.github.stigmata.BirthmarkContext;

/**
 * 
 * @author Haruaki Tamada
 */
public interface StigmataRuntimeHook{
    public void onHook(Phase phase, BirthmarkContext context);
}