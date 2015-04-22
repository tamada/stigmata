package com.github.stigmata.command;

import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.Stigmata;
import com.github.stigmata.StigmataCommand;
import com.github.stigmata.hook.Phase;
import com.github.stigmata.hook.StigmataHookManager;

/**
 * 
 * @author Haruaki Tamada
 */
public abstract class AbstractStigmataCommand implements StigmataCommand{
    /**
     * {@link perform(Stigmata, BirthmarkContext, String[]) <code>perform(stigmata, stigmata.createContext(), args)</code>}.
     */
    @Override
    public void perform(Stigmata stigmata, String[] args){
        perform(stigmata, stigmata.createContext(), args);
    }

    @Override
    public boolean isAvailableArguments(String[] args){
        return true;
    }

    @Override
    public void setUp(BirthmarkEnvironment env){
        StigmataHookManager.getInstance().runHook(Phase.SETUP, env);
    }

    @Override
    public void tearDown(BirthmarkEnvironment env){
        StigmataHookManager.getInstance().runHook(Phase.TEAR_DOWN, env);
    }

    @Override
    public abstract String getCommandString();
}
