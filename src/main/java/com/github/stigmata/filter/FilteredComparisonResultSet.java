package com.github.stigmata.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.BirthmarkSet;
import com.github.stigmata.ComparisonPair;
import com.github.stigmata.ComparisonPairFilterSet;
import com.github.stigmata.ComparisonResultSet;

/**
 * Filtering {@link ComparisonResultSet <code>ComparisonResultSet</code>}.
 * 
 * @author Haruaki TAMADA
 */
public class FilteredComparisonResultSet implements ComparisonResultSet{
    private ComparisonResultSet resultset;
    private List<ComparisonPairFilterSet> filters = new ArrayList<ComparisonPairFilterSet>(); 

    /**
     * constructor.
     * 
     * @param resultset filtering target
     */
    public FilteredComparisonResultSet(ComparisonResultSet resultset){
        this.resultset = resultset;
    }

    /**
     * constructor.
     * @param resultset filtering target
     * @param filters filtering rule
     */
    public FilteredComparisonResultSet(ComparisonResultSet resultset, ComparisonPairFilterSet[] filters){
        this.resultset = resultset;
        for(int i = 0; i < filters.length; i++){
            addFilterSet(filters[i]);
        }
    }

    public void addFilterSet(ComparisonPairFilterSet filter){
        filters.add(filter);
    }

    public void removeFilterSet(ComparisonPairFilterSet filter){
        filters.remove(filter);
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
    public ComparisonPair[] getPairs(){
        List<ComparisonPair> list = new ArrayList<ComparisonPair>();
        for(Iterator<ComparisonPair> i = iterator(); i.hasNext(); ){
            list.add(i.next());
        }
        return list.toArray(new ComparisonPair[list.size()]);
    }

    @Override
    public int getPairCount(){
        return resultset.getPairCount();
    }

    @Override
    public BirthmarkContext getContext(){
        return resultset.getContext();
    }

    @Override
    public BirthmarkEnvironment getEnvironment(){
        return resultset.getEnvironment();
    }

    @Override
    public Iterator<ComparisonPair> iterator(){
        return new FilteredIterator(resultset.iterator());
    }

    @Override
    public synchronized BirthmarkSet[] getPairSources(){
        List<BirthmarkSet> list = new ArrayList<BirthmarkSet>();
        for(Iterator<BirthmarkSet> i = pairSources(); i.hasNext(); ){
            list.add(i.next());
        }
        return list.toArray(new BirthmarkSet[list.size()]);
    }

    @Override
    public Iterator<BirthmarkSet> pairSources(){
        return resultset.pairSources();
    }

    private class FilteredIterator implements Iterator<ComparisonPair>{
        private Iterator<ComparisonPair> iterator;
        private ComparisonPair next;

        public FilteredIterator(Iterator<ComparisonPair> iterator){
            this.iterator = iterator;
            
            next = findNext();
        }

        @Override
        public boolean hasNext(){
            return next != null;
        }

        @Override
        public ComparisonPair next(){
            ComparisonPair returnValue = next;
            next = findNext();
            return returnValue;
        }

        @Override
        public void remove(){
            throw new InternalError("not implemented");
        }

        private ComparisonPair findNext(){
            boolean nowFinding = true;
            while(nowFinding && iterator.hasNext()){
                ComparisonPair nextPair = iterator.next();
                // return the pair which the all filters is passed
                if(isAllFilterPassed(nextPair)){
                    nowFinding = false; // found next value!
                    next = nextPair;
                }
            }
            if(nowFinding && !iterator.hasNext()){
                next = null;
            }
            return next;
        }

        private boolean isAllFilterPassed(ComparisonPair pair){
            boolean flag = true;
            for(Iterator<ComparisonPairFilterSet> i = filters.iterator(); i.hasNext(); ){
                ComparisonPairFilterSet filter = i.next();
                if(!filter.isFiltered(pair)){
                    flag = false;
                    break;
                }
            }
            return flag;
        }
    };
}
