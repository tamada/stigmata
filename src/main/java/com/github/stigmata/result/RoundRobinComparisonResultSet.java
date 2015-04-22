package com.github.stigmata.result;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.BirthmarkSet;
import com.github.stigmata.ComparisonPair;
import com.github.stigmata.ExtractionResultSet;
import com.github.stigmata.ExtractionTarget;

/**
 * Concrete class for ComparisonResultSet. This instance compare class files by round robin.
 *
 * @author  Haruaki TAMADA
 */
public class RoundRobinComparisonResultSet extends AbstractComparisonResultSet{
    private int compareCount = -1;
    private boolean tableType;
    private boolean samePair = false;

    /**
     * constructor.  if user gives { a, b, c, } as holders1, then
     * the instance (created by this constructor) compares { a<->b, a<->c,
     * b<->c, }.
     */
    public RoundRobinComparisonResultSet(ExtractionResultSet resultset){
        this(resultset, false);
    }

    /**
     * constructor.  if user gives { a, b, c, } as holders1, then the
     * instance (created by this constructor when samePair is true)
     * compares { a<->a, b<->a, b<->b, c<->a, c<->b, c<->c, }.
     * Otherwise, the instance compares { a<->b, a<->c, b<->c, } when
     * samePair is false.
     */
    public RoundRobinComparisonResultSet(ExtractionResultSet resultset, boolean samePair){
        super(resultset);
        this.tableType = resultset.isTableType();
        setCompareSamePair(samePair);
    }

    /**
     * @return  environment
     */
    @Override
    public BirthmarkEnvironment getEnvironment(){
        return extraction.getEnvironment();
    }

    @Override
    public BirthmarkContext getContext(){
        return extraction.getContext();
    }

    /**
     * update same pair comparing flag unless two birthmark array is setted.
     */
    public void setCompareSamePair(boolean flag){
        samePair = flag;
        if(!extraction.isTableType()){
            int size = extraction.getBirthmarkSetSize(ExtractionTarget.TARGET_XY);
            if(flag){
                if(size > 1)       compareCount = (size - 1) * (size - 2) + 1;
                else if(size == 1) compareCount = 0;
                else throw new IllegalStateException("no extraction result");
            }
            else{
                if(size > 1)       compareCount = (size - 1) * (size - 2) + 1 + size;
                else if(size == 1) compareCount = 1;
                else throw new IllegalStateException("no extraction result");
            }
        }
        else if(compareCount == -1){
            compareCount = extraction.getBirthmarkSetSize(ExtractionTarget.TARGET_X)
                    * extraction.getBirthmarkSetSize(ExtractionTarget.TARGET_Y); 
        }
    }

    public boolean isCompareSamePair(){
        return samePair;
    }

    /**
     * returns the compare count of birthmark sets.
     */
    @Override
    public int getPairCount(){
        return compareCount;
    }

    /**
     * return a iterator of whole comparison.
     */
    @Override
    public Iterator<ComparisonPair> iterator(){
        if(tableType){
            return new CompareTableIterator();
        }
        else{
            return new CompareTriangleIterator();
        }
    }

    @Override
    public BirthmarkSet[] getPairSources(){
        List<BirthmarkSet> list = new ArrayList<BirthmarkSet>();
        for(Iterator<BirthmarkSet> i = pairSources(); i.hasNext(); ){
            list.add(i.next());
        }

        return list.toArray(new BirthmarkSet[list.size()]);
    }

    @Override
    public Iterator<BirthmarkSet> pairSources(){
        Map<URL, BirthmarkSet> map = new HashMap<URL, BirthmarkSet>();
        if(extraction.isTableType()){
            for(Iterator<BirthmarkSet> i = extraction.birthmarkSets(ExtractionTarget.TARGET_X); i.hasNext(); ){
                BirthmarkSet bs = i.next();
                map.put(bs.getLocation(), bs);
            }
            for(Iterator<BirthmarkSet> i = extraction.birthmarkSets(ExtractionTarget.TARGET_Y); i.hasNext(); ){
                BirthmarkSet bs = i.next();
                map.put(bs.getLocation(), bs);
            }
        }
        else{
            for(Iterator<BirthmarkSet> i = extraction.birthmarkSets(ExtractionTarget.TARGET_XY); i.hasNext(); ){
                BirthmarkSet bs = i.next();
                map.put(bs.getLocation(), bs);
            }
        }
        return map.values().iterator();
    }

    private class CompareTableIterator implements Iterator<ComparisonPair>{
        private Iterator<BirthmarkSet> ix = extraction.birthmarkSets(ExtractionTarget.TARGET_X);
        private Iterator<BirthmarkSet> iy = extraction.birthmarkSets(ExtractionTarget.TARGET_Y);
        private BirthmarkSet x = ix.next();

        @Override
        public boolean hasNext(){
            return ix.hasNext() || iy.hasNext();
        }

        @Override
        public ComparisonPair next(){
            if(!iy.hasNext()){
                iy = extraction.birthmarkSets(ExtractionTarget.TARGET_Y);
                x = ix.next();
            }
            BirthmarkSet y = iy.next();

            return new ComparisonPair(x, y, extraction.getContext());
        }

        @Override
        public void remove(){
        }
    }

    /**
     * iterator class.
     */
    private class CompareTriangleIterator implements Iterator<ComparisonPair>{
        private List<String> names = new ArrayList<String>();
        private Iterator<BirthmarkSet> iterator;
        private int index = 0;
        private BirthmarkSet bs;
        private ComparisonPair next;

        public CompareTriangleIterator(){
            iterator = extraction.birthmarkSets(ExtractionTarget.TARGET_XY);
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

        private ComparisonPair findNext(){
            if(bs == null && iterator.hasNext()){
                bs = iterator.next();
                names.add(bs.getName());
            }
            if((isCompareSamePair() && index == names.size()) || (!isCompareSamePair() && index == (names.size() - 1))){
                index = 0;
                if(iterator.hasNext()){
                    bs = iterator.next();
                    names.add(bs.getName());
                }
                else{
                    return null;
                }
            }
            String name = names.get(index);
            BirthmarkSet bsX = extraction.getBirthmarkSet(ExtractionTarget.TARGET_XY, name);
            ComparisonPair pair = null;
            if(bsX != null){
                pair = new ComparisonPair(bsX, bs, extraction.getContext());
            }
            index++;

            if(pair == null){
                pair = findNext();
            }
            
            return pair;
        }

        @Override
        public void remove(){
        }
    }
}
