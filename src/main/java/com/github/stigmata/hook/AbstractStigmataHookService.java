package com.github.stigmata.hook;

import com.github.stigmata.spi.StigmataHookService;

/**
 * 
 * 
 * @author Haruaki Tamada
 */
public abstract class AbstractStigmataHookService implements StigmataHookService{
    @Override
    public StigmataRuntimeHook afterComparison(){
        return null;
    }

    @Override
    public StigmataRuntimeHook afterExtraction(){
        return null;
    }

    @Override
    public StigmataRuntimeHook afterFiltering(){
        return null;
    }

    @Override
    public StigmataRuntimeHook beforeComparison(){
        return null;
    }

    @Override
    public StigmataRuntimeHook beforeExtraction(){
        return null;
    }

    @Override
    public StigmataRuntimeHook beforeFiltering(){
        return null;
    }

    @Override
    public StigmataHook onSetup(){
        return null;
    }

    @Override
    public StigmataHook onTearDown(){
        return null;
    }
}
