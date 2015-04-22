package com.github.stigmata.command;

import java.util.ResourceBundle;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.Stigmata;

/**
 * 
 * @author Haruaki Tamada
 */
public class VersionCommand extends AbstractStigmataCommand{
    @Override
    public String getCommandString(){
        return "version";
    }

    @Override
    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        ResourceBundle helpResource = ResourceBundle.getBundle("resources.options");
        Package p = getClass().getPackage();
        System.out.printf("%s %s%n", helpResource.getString("cli.version.header"), p.getImplementationVersion());
    }
}
