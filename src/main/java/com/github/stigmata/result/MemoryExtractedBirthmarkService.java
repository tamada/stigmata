package com.github.stigmata.result;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkStoreTarget;
import com.github.stigmata.ExtractionResultSet;
import com.github.stigmata.result.history.ExtractedBirthmarkHistory;
import com.github.stigmata.result.history.MemoryExtractedBirthmarkHistory;
import com.github.stigmata.spi.ExtractedBirthmarkService;

/**
 * 
 * @author Haruaki Tamada
 */
public class MemoryExtractedBirthmarkService implements ExtractedBirthmarkService{
    private MemoryExtractedBirthmarkHistory history = new MemoryExtractedBirthmarkHistory();

    @Override
    public ExtractionResultSet createResultSet(BirthmarkContext context){
        MemoryExtractionResultSet mers = new MemoryExtractionResultSet(context);
        history.addResultSet(mers);
        return mers;
    }

    @Override
    public ExtractedBirthmarkHistory getHistory(String parameter){
        return history;
    }

    @Override
    public ExtractionResultSet getResultSet(String id){
        return history.getResultSet(id);
    }

    @Override
    public BirthmarkStoreTarget getTarget(){
        return BirthmarkStoreTarget.MEMORY;
    }

    @Override
    public String getType(){
        return "memory";
    }

    @Override
    public String getDescription(){
        return "Store birthmarks in memory";
    }

}
