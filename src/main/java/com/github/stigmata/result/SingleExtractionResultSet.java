package com.github.stigmata.result;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkSet;
import com.github.stigmata.BirthmarkStoreTarget;
import com.github.stigmata.ExtractionTarget;
import com.github.stigmata.utils.NullIterator;

/**
 *
 * @author Haruaki Tamada
 */
public class SingleExtractionResultSet extends AbstractExtractionResultSet{
    private BirthmarkSet bs;

    public SingleExtractionResultSet(BirthmarkContext context, BirthmarkSet bs){
        super(context, false);
        this.bs = bs;
    }

    public SingleExtractionResultSet(BirthmarkContext context){
        super(context, false);
    }

    @Override
    public BirthmarkStoreTarget getStoreTarget(){
        return BirthmarkStoreTarget.MEMORY_SINGLE;
    }

    @Override
    public void addBirthmarkSet(ExtractionTarget target, BirthmarkSet set){
        this.bs = set;
    }

    @Override
    public void setBirthmarkSets(ExtractionTarget target, BirthmarkSet[] sets){
        this.bs = sets[0];
    }

    @Override
    public Iterator<BirthmarkSet> birthmarkSets(ExtractionTarget target){
        if(bs != null){
            return new SingleIterator(bs);
        }
        return new NullIterator<BirthmarkSet>();
    }

    @Override
    public void removeBirthmarkSet(ExtractionTarget target, BirthmarkSet set){
        bs = null;
    }

    @Override
    public void removeAllBirthmarkSets(ExtractionTarget target){
        bs = null;
    }

    @Override
    public BirthmarkSet getBirthmarkSet(ExtractionTarget target, int index){
        if(index != 0){
            throw new IndexOutOfBoundsException("illegal index: " + index);
        }
        return bs;
    }

    @Override
    public BirthmarkSet getBirthmarkSet(ExtractionTarget target, String name){
        if(bs.getName().equals(name)){
            return bs;
        }
        return null;
    }

    @Override
    public int getBirthmarkSetSize(ExtractionTarget target){
        return 1;
    }

    private static class SingleIterator implements Iterator<BirthmarkSet>{
        private boolean first = true;
        private BirthmarkSet bs;

        public SingleIterator(BirthmarkSet bs){
            this.bs = bs;
        }

        @Override
        public BirthmarkSet next(){
            if(first){
                first = false;
                return bs;
            }
            throw new NoSuchElementException();
        }

        @Override
        public boolean hasNext(){
            return first;
        }

        @Override
        public void remove(){
        }
    }
}
