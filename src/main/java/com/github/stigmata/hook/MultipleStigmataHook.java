package com.github.stigmata.hook;

import java.util.ArrayList;
import java.util.List;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkEnvironment;

/**
 * 
 * @author Haruaki Tamada
 */
public class MultipleStigmataHook implements StigmataHook, StigmataRuntimeHook{
    private List<StigmataHook> hooks = new ArrayList<StigmataHook>();
    private List<StigmataRuntimeHook> runtimeHooks = new ArrayList<StigmataRuntimeHook>();

    @Override
    public void onHook(Phase phase, BirthmarkContext context){
        for(StigmataRuntimeHook hook: runtimeHooks){
            if(hook != null){
                hook.onHook(phase, context);
            }
        }
    }

    @Override
    public void onHook(Phase phase, BirthmarkEnvironment env){
        for(StigmataHook hook: hooks){
            if(hook != null){
                hook.onHook(phase, env);
            }
        }
    }

    public void addHook(StigmataHook hook){
        hooks.add(hook);
    }

    public void removeHook(StigmataHook hook){
        hooks.remove(hook);
    }

    public int getHookCount(){
        return hooks.size();
    }

    public StigmataHook getHook(int index){
        return hooks.get(index);
    }

    public void addRuntimeHook(StigmataRuntimeHook hook){
        runtimeHooks.add(hook);
    }

    public void removeRuntimeHook(StigmataRuntimeHook hook){
        runtimeHooks.remove(hook);
    }

    public int getRuntimeHookCount(){
        return runtimeHooks.size();
    }

    public StigmataRuntimeHook getRuntimeHook(int index){
        return runtimeHooks.get(index);
    }
}