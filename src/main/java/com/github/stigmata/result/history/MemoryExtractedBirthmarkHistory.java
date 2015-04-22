package com.github.stigmata.result.history;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.github.stigmata.ExtractionResultSet;
import com.github.stigmata.result.MemoryExtractionResultSet;

public class MemoryExtractedBirthmarkHistory implements ExtractedBirthmarkHistory{
    private Map<String, MemoryExtractionResultSet> map = new HashMap<String, MemoryExtractionResultSet>();

    public MemoryExtractedBirthmarkHistory(){
    }

    public MemoryExtractedBirthmarkHistory(MemoryExtractionResultSet[] mersArray){
        for(MemoryExtractionResultSet mers: mersArray){
            map.put(mers.getId(), mers);
        }
    }

    public void addResultSet(MemoryExtractionResultSet mers){
        map.put(mers.getId(), mers);
    }

    @Override
    public void deleteResultSet(String id){
        map.remove(id);
    }

    @Override
    public void deleteAllResultSets(){
        map.clear();
    }

    @Override
    public ExtractionResultSet getResultSet(String id){
        return map.get(id);
    }

    @Override
    public synchronized String[] getResultSetIds(){
        return map.keySet().toArray(new String[map.size()]);
    }

    @Override
    public Iterator<String> iterator(){
        return Collections.unmodifiableSet(map.keySet()).iterator();
    }

    @Override
    public void refresh(){
    }
}
