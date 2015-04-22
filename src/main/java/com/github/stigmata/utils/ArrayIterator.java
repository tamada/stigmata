package com.github.stigmata.utils;

import java.util.Iterator;

/**
 * 
 * @author Haruaki Tamada
 */
public class ArrayIterator<T> implements Iterator<T>{
    private T[] values;
    private int length;
    private int currentIndex = 0;

    public ArrayIterator(T[] values){
        this.values = values;
        if(values != null){
            length = values.length;
        }
        else{
            length = 0;
        }
    }

    public ArrayIterator(){
    }

    @Override
    public boolean hasNext(){
        return currentIndex < length;
    }

    @Override
    public T next(){
        if(values != null){
            T value = values[currentIndex];
            currentIndex++;
            return value;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void remove(){
    }
}
