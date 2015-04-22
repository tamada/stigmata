package com.github.stigmata.result;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkSet;
import com.github.stigmata.BirthmarkStoreException;
import com.github.stigmata.ComparisonPair;
import com.github.stigmata.ExtractionResultSet;
import com.github.stigmata.ExtractionTarget;
import com.github.stigmata.result.history.ExtractedBirthmarkServiceManager;

/**
 * Concrete class for ComparisonResultSet. This instance compare class files by
 * certain pair. The pair is guessed by system with class name, or specified by
 * user.
 * 
 * @author Haruaki TAMADA
 */
public class CertainPairComparisonResultSet extends AbstractComparisonResultSet{
    private int compareCount = -1;
    private Collection<BirthmarkSet> sources = null;

    public CertainPairComparisonResultSet(ExtractionResultSet extraction){
        super(extraction);
    }

    public CertainPairComparisonResultSet(ExtractionResultSet extraction, Map<String, String> nameMap){
        super(extraction);
        BirthmarkContext context = extraction.getContext();
        for(Map.Entry<String, String> entry: nameMap.entrySet()){
            context.addNameMapping(entry.getKey(), entry.getValue());
        }
    }

    /**
     * This constructor is the comparison pair list is specified.
     */
    public CertainPairComparisonResultSet(ComparisonPair[] pairs, BirthmarkContext context){
        super(createExtractionResultSet(pairs, context));
    }

    /**
     * This constructor is the comparison pair was guessed with class name.
     */
    public CertainPairComparisonResultSet(BirthmarkSet[] targetX,
            BirthmarkSet[] targetY, BirthmarkContext context){
        super(createExtractionResultSet(targetX, targetY, context));
    }

    /**
     * This constructor is the comparison pair was specified as mapping.
     */
    public CertainPairComparisonResultSet(BirthmarkSet[] targetX, BirthmarkSet[] targetY,
            Map<String, String> mapping, BirthmarkContext context){
        super(createExtractionResultSet(targetX, targetY, context));

        for(Map.Entry<String, String> entry : mapping.entrySet()){
            context.addNameMapping(entry.getKey(), entry.getValue());
        }
    }

    /**
     * return comparison count.
     */
    @Override
    public int getPairCount(){
        BirthmarkContext context = getContext();
        if(compareCount < 0){
            int count = 0;
            if(context.hasNameMapping()){
                count = context.getNameMappingCount();
            }
            else{
                count = super.getPairCount();
            }
            compareCount = count;
        }
        return compareCount;
    }

    /**
     * return the iterator of each pair.
     */
    @Override
    public Iterator<ComparisonPair> iterator(){
        Iterator<ComparisonPair> iterator = null;
        final BirthmarkContext context = getContext();
        if(context.hasNameMapping()){
            iterator = new NameMappingIterator(extraction);
        }
        else{
            iterator = new NameFindIterator(extraction);
        }
        return iterator;
    }

    @Override
    public Iterator<BirthmarkSet> pairSources(){
        if(sources == null){
            sources = createSources();
        }
        return sources.iterator();
    }

    @Override
    public BirthmarkSet[] getPairSources(){
        if(sources == null){
            sources = createSources();
        }
        return sources.toArray(new BirthmarkSet[sources.size()]);
    }

    private Collection<BirthmarkSet> createSources(){
        Map<URL, BirthmarkSet> map = new HashMap<URL, BirthmarkSet>();
        for(Iterator<ComparisonPair> i = iterator(); i.hasNext(); ){
            ComparisonPair pair = i.next();
            addToMap(map, pair.getTarget1());
            addToMap(map, pair.getTarget2());
        }
        return map.values();
    }

    private void addToMap(Map<URL, BirthmarkSet> map, BirthmarkSet set){
        map.put(set.getLocation(), set);
    }

    private static ExtractionResultSet createExtractionResultSet(ComparisonPair[] pairs, BirthmarkContext context){
        ExtractedBirthmarkServiceManager historyManager = context.getEnvironment().getHistoryManager();
        ExtractionResultSet ers = historyManager.createDefaultResultSet(context);
        ers.setTableType(false);
        try{
            for(int i = 0; i < pairs.length; i++){
                ers.addBirthmarkSet(ExtractionTarget.TARGET_X, pairs[i].getTarget1());
                ers.addBirthmarkSet(ExtractionTarget.TARGET_Y, pairs[i].getTarget2());
            }
        }catch(BirthmarkStoreException e){
            throw new InternalError("never thrown BirthmarkStoreException is thrown");
        }
        return ers;
    }

    private static ExtractionResultSet createExtractionResultSet(BirthmarkSet[] targetX, BirthmarkSet[] targetY, BirthmarkContext context){
        ExtractionResultSet ers = context.getEnvironment().getHistoryManager().createDefaultResultSet(context);
        ers.setTableType(true);
        try{
            ers.setBirthmarkSets(ExtractionTarget.TARGET_X, targetX);
            ers.setBirthmarkSets(ExtractionTarget.TARGET_Y, targetY);
        }catch(BirthmarkStoreException e){
            throw new InternalError("never thrown BirthmarkStoreException is thrown");
        }
        return ers;
    }

    private static class NameFindIterator implements Iterator<ComparisonPair>{
        private ComparisonPair next;
        private BirthmarkSet setX = null;
        private Iterator<BirthmarkSet> iteratorX;
        private Iterator<BirthmarkSet> iteratorY;
        private ExtractionResultSet extraction;

        public NameFindIterator(ExtractionResultSet extraction){
            this.extraction = extraction;
            iteratorX = extraction.birthmarkSets(ExtractionTarget.TARGET_X);
            setX = iteratorX.next();
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
        }

        private ComparisonPair findNext(){
            ComparisonPair next = null;
            if(iteratorY == null || !iteratorY.hasNext()){
                iteratorY = extraction.birthmarkSets(ExtractionTarget.TARGET_Y);
            }

            if(setX != null){
                for(; iteratorY.hasNext(); ){
                    BirthmarkSet setY = iteratorY.next();
                    if(setX.getName().equals(setY.getName())){
                        next = new ComparisonPair(setX, setY, extraction.getContext());
                        break;
                    }
                }

                if(iteratorX.hasNext()){
                    setX = iteratorX.next();
                }
                else{
                    setX = null;
                }
                if(next == null){
                    next = findNext();
                }
            }
            return next;
        }
    };

    private static class NameMappingIterator implements Iterator<ComparisonPair>{
        private Iterator<Map.Entry<String, String>> names;
        private ComparisonPair nextPair;
        private ExtractionResultSet ers;

        public NameMappingIterator(ExtractionResultSet ers){
            this.ers = ers;
            names = ers.getContext().nameMappingEntries();
            nextPair = findNextPair();
        }

        @Override
        public ComparisonPair next(){
            ComparisonPair cp = nextPair;
            nextPair = findNextPair();
            return cp;
        }

        @Override
        public boolean hasNext(){
            return nextPair != null;
        }

        @Override
        public void remove(){
        }

        private ComparisonPair findNextPair(){
            ComparisonPair pair = null;
            if(names.hasNext()){
                Map.Entry<String, String> entry = names.next();
                String n1 = entry.getKey();
                String n2 = entry.getValue();

                BirthmarkSet bs1 = ers.getBirthmarkSet(ExtractionTarget.TARGET_X, n1);
                BirthmarkSet bs2 = ers.getBirthmarkSet(ExtractionTarget.TARGET_Y, n2);

                if(bs1 == null || bs2 == null){
                    pair = findNextPair();
                }
                else{
                    pair = new ComparisonPair(bs1, bs2, ers.getContext());
                }
            }
            return pair;
        }
    };
}
