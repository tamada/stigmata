package com.github.stigmata;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class manages a set of birthmarks which extracted from a target.
 *
 * @author  Haruaki TAMADA
 */
public class BirthmarkSet implements Iterable<Birthmark>{
    /**
     * this object name.
     */
    private String name;

    /**
     * location of target is loaded from.
     */
    private URL location;

    /**
     * map for birthmarks.
     */
    private Map<String, Birthmark> birthmarks = new HashMap<String, Birthmark>();

    /**
     * constructor.
     */
    public BirthmarkSet(String name, URL location){
        this.name = name;
        this.location = location;
    }

    /**
     * return the sum of all element count of birthmarks this instance has.
     */
    public int getSumOfElementCount(){
        int count = 0;
        for(Iterator<String> i = birthmarkTypes(); i.hasNext();){
            Birthmark birthmark = getBirthmark(i.next());
            count += birthmark.getElementCount();
        }
        return count;
    }

    /**
     * return the number of birthmarks.
     */
    public int getBirthmarksCount(){
        return birthmarks.size();
    }

    /**
     * return the name.
     */
    public String getName(){
        return name;
    }

    /**
     * return the location.
     */
    public URL getLocation(){
        return location;
    }

    /**
     * add given birthmark to this instance.
     * @throws NullPointerException given birthmark is null.
     */
    public void addBirthmark(Birthmark birthmark){
        if(birthmark == null){
            throw new NullPointerException("given birthmark is null");
        }
        birthmarks.put(birthmark.getType(), birthmark);
    }

    /**
     * return the given type of birthmark.
     */
    public Birthmark getBirthmark(String type){
        return birthmarks.get(type);
    }

    /**
     * does this object have the given birthmark type.
     */
    public boolean hasBirthmark(String type){
        return birthmarks.get(type) != null;
    }

    /**
     * returns an array containing all of the birthmarks in this object.
     */
    public Birthmark[] getBirthmarks(){
        Birthmark[] b = new Birthmark[getBirthmarksCount()];
        int index = 0;
        for(Iterator<String> i = birthmarkTypes(); i.hasNext();){
            b[index] = getBirthmark(i.next());
            index++;
        }
        return b;
    }

    /**
     * returns an iterator over the birthmarks in this birthmark-set.
     */
    @Override
    public Iterator<Birthmark> iterator(){
        return birthmarks.values().iterator();
    }

    /**
     * returns an iterator over the birthmark types.
     */
    public Iterator<String> birthmarkTypes(){
        return birthmarks.keySet().iterator();
    }

    /**
     * returns an array of birthmark types.
     */
    public synchronized String[] getBirthmarkTypes(){
        return birthmarks.keySet().toArray(new String[birthmarks.size()]);
    }
}
