package com.github.stigmata.event;

import java.util.EventObject;

/**
 * 
 * @author Haruaki Tamada
 */
public class BirthmarkEngineEvent extends EventObject{
    private static final long serialVersionUID = -1260793588721638917L;

    private OperationType type;
    private OperationStage stage;
    private WarningMessages message;

    public BirthmarkEngineEvent(OperationStage stage, OperationType type, WarningMessages message){
        super(type.ordinal());
        this.type = type;
        this.stage = stage;
        this.message = message;
    }

    public OperationStage getStage(){
        return stage;
    }

    public OperationType getType(){
        return type;
    }

    public WarningMessages getMessage(){
        return message;
    }
}
