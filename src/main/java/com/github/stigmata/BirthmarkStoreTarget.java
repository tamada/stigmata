package com.github.stigmata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents how to store extracted birthmarks.
 * memory?, databases?, or files?
 * 
 * @author Haruaki Tamada
 */
public class BirthmarkStoreTarget implements Serializable, Comparable<BirthmarkStoreTarget>{
    private static final long serialVersionUID = -4225861589804166362L;

    private static final Map<String, BirthmarkStoreTarget> TARGETS = new HashMap<String, BirthmarkStoreTarget>();

    public static BirthmarkStoreTarget MEMORY = new BirthmarkStoreTarget(0, "MEMORY");
    public static BirthmarkStoreTarget XMLFILE = new BirthmarkStoreTarget(1, "XMLFILE");
    public static BirthmarkStoreTarget MEMORY_SINGLE = new BirthmarkStoreTarget(2, "MEMORY_SINGLE");
    public static BirthmarkStoreTarget RDB = new BirthmarkStoreTarget(3, "RDB");

    private final String name;
    private final int ordinal;

    private BirthmarkStoreTarget(int index, String name){
        this.name = name;
        this.ordinal = index;
        TARGETS.put(name, this);
    }

    public String name(){
        return name;
    }

    public int ordinal(){
        return ordinal;
    }

    @Override
    public boolean equals(Object o){
        return this == o;
    }

    @Override
    public int compareTo(BirthmarkStoreTarget other){
        return ordinal - other.ordinal;
    }

    public static BirthmarkStoreTarget valueOf(String name){
        return TARGETS.get(name);
    }

    public static BirthmarkStoreTarget createTarget(String name){
        BirthmarkStoreTarget bst = TARGETS.get(name);
        if(bst == null){
            bst = new BirthmarkStoreTarget(TARGETS.size(), name);
        }
        return bst;
    }
}
