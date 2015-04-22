package com.github.stigmata.hook;

import com.github.stigmata.BirthmarkEnvironment;

/**
 * 
 * @author Haruaki Tamada
 */
public interface StigmataHook{
    public void onHook(Phase phase, BirthmarkEnvironment env);
}