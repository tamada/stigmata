package com.github.stigmata;

/**
 *
 * @author Haruaki Tamada
 */
public interface StigmataCommand{
    public boolean isAvailableArguments(String[] args);

    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args);

    public void perform(Stigmata stigmata, String[] args);

    public void setUp(BirthmarkEnvironment env);

    public void tearDown(BirthmarkEnvironment env);

    public String getCommandString();
}
