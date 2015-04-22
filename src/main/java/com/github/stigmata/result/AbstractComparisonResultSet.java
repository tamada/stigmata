package com.github.stigmata.result;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.BirthmarkSet;
import com.github.stigmata.ComparisonPair;
import com.github.stigmata.ComparisonResultSet;
import com.github.stigmata.ExtractionResultSet;

/**
 * Abstract class for ComparisonResultSet.
 *
 * @author Haruaki Tamada
 */
public abstract class AbstractComparisonResultSet implements ComparisonResultSet{
    protected ExtractionResultSet extraction;
    private int count = -1;

    public AbstractComparisonResultSet(ExtractionResultSet extraction){
        this.extraction = extraction;
    }

    @Override
    public abstract Iterator<ComparisonPair> iterator();

    @Override
    public abstract Iterator<BirthmarkSet> pairSources();

    @Override
    public synchronized BirthmarkSet[] getPairSources(){
        return AbstractComparisonResultSet.<BirthmarkSet>getArrays(pairSources(), new BirthmarkSet[0]);
    }

    @Override
    public int getPairCount(){
        if(count < 0){
            int calculateCount = 0;
            for(Iterator<ComparisonPair> i = iterator(); i.hasNext(); ){
                calculateCount++;
                i.next();
            }
            this.count = calculateCount;
        }
        return count;
    }

    @Override
    public synchronized ComparisonPair[] getPairs(){
        return AbstractComparisonResultSet.<ComparisonPair>getArrays(iterator(), new ComparisonPair[0]);
    }

    @Override
    public ComparisonPair getPairAt(int index){
        int currentIndex = 0;
        for(Iterator<ComparisonPair> i = iterator(); i.hasNext(); ){
            ComparisonPair pair = i.next();
            if(currentIndex == index){
                return pair;
            }
            currentIndex++;
        }
        return null;
    }

    @Override
    public BirthmarkContext getContext(){
        return extraction.getContext();
    }

    @Override
    public BirthmarkEnvironment getEnvironment(){
        return extraction.getEnvironment();
    }

    @SuppressWarnings("unchecked")
    static synchronized <T> T[] getArrays(Iterator<T> i, T[] array){
        List<Object> list = new ArrayList<Object>();
        while(i.hasNext()){
            list.add(i.next());
        }

        if(list.size() > array.length){
            array = (T[])Array.newInstance(array.getClass().getComponentType(), list.size());
        }
        for(int index = 0; index < list.size(); index++){
            array[index] = (T)list.get(index);
        }
        return array;
    }
}
