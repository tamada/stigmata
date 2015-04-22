package com.github.stigmata.event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Haruaki Tamada
 */
public class WarningMessages{
    private OperationType type;
    private Map<Exception, String> messages = new HashMap<Exception, String>();

    public WarningMessages(OperationType type){
        this.type = type;
    }

    public void addMessage(Exception e, String string){
        messages.put(e, string);
    }

    public Iterator<Exception> exceptions(){
        return messages.keySet().iterator();
    }

    public String getString(Exception e){
        return messages.get(e);
    }

    public OperationType getType(){
        return type;
    }

    public void setType(OperationType type){
        this.type = type;
    }

    public int getWarningCount(){
        return messages.size();
    }
}
