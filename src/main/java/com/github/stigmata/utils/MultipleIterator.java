package com.github.stigmata.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 
 * @author Haruaki Tamada
 */
public class MultipleIterator<T> implements Iterator<T>{
    private List<Iterator<T>> iterators = new ArrayList<Iterator<T>>();
    private int index = 0;

    public MultipleIterator(){
    }

    public MultipleIterator(Collection<Iterator<T>> collection){
        this(collection.iterator());
    }

    public MultipleIterator(Iterator<Iterator<T>> iterator){
        while(iterator.hasNext()){
            iterators.add(iterator.next());
        }
    }

    public MultipleIterator(Iterator<T>[] iteratorArray){
        for(Iterator<T> iterator: iteratorArray){
            iterators.add(iterator);
        }
    }

    public void add(Iterator<T> iterator){
        iterators.add(iterator);
    }

    @Override
    public boolean hasNext(){
        while(index < iterators.size()){
            boolean next = iterators.get(index).hasNext();
            if(next){
                return true;
            }
            index++;
        }
        return false;
    }

    /** 
     */
    @Override
    public T next(){
        if(!iterators.get(index).hasNext()){
            index++;
            while(index < iterators.size()){
                if(iterators.get(index).hasNext()){
                    return iterators.get(index).next();
                }
                index++;
            }
            throw new NoSuchElementException();
        }

        return iterators.get(index).next();
    }

    @Override
    public void remove(){
        iterators.get(index).remove();
    }

    public void remove(Iterator<T> iterator){
        iterators.remove(iterator);
    }
}
