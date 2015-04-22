package com.github.stigmata.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import com.github.stigmata.StigmataCommand;

/**
 * 
 * @author Haruaki Tamada
 */
public class StigmataCommandFactory implements Iterable<StigmataCommand>{
    private static final StigmataCommandFactory factory = new StigmataCommandFactory();
    private Map<String, StigmataCommand> commands = new HashMap<String, StigmataCommand>();

    private StigmataCommandFactory(){
        for(StigmataCommand command: ServiceLoader.load(StigmataCommand.class)){
            registerCommand(command);
        }
    }

    public static StigmataCommandFactory getInstance(){
        return factory;
    }

    public void registerCommand(StigmataCommand command){
        commands.put(command.getCommandString(), command);
    }

    public StigmataCommand getDefaultCommand(){
        StigmataCommand gui = getCommand("gui");
        return gui;
    }

    @Override
    public Iterator<StigmataCommand> iterator(){
        return commands.values().iterator();
    }

    public StigmataCommand getCommand(String commandString){
        StigmataCommand command = commands.get(commandString);
        if(command == null){
            command = commands.get("help");
        }
        return command;
    }
}
