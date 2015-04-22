package com.github.stigmata.result;

import javax.sql.DataSource;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkStoreTarget;
import com.github.stigmata.ExtractionResultSet;
import com.github.stigmata.result.history.ExtractedBirthmarkHistory;
import com.github.stigmata.result.history.RDBExtractedBirthmarkHistory;
import com.github.stigmata.spi.ExtractedBirthmarkService;

/**
 * 
 * @author Haruaki Tamada
 */
public class RDBExtractedBirthmarkService implements ExtractedBirthmarkService{
    private DataSource source;

    public RDBExtractedBirthmarkService(){
    }

    @Override
    public ExtractionResultSet createResultSet(BirthmarkContext context){
        return new RDBExtractionResultSet(context);
    }

    @Override
    public ExtractedBirthmarkHistory getHistory(String parameter){
        return new RDBExtractedBirthmarkHistory(source);
    }

    @Override
    public ExtractionResultSet getResultSet(String id){
        return new RDBExtractionResultSet(source, id);
    }

    @Override
    public BirthmarkStoreTarget getTarget(){
        return BirthmarkStoreTarget.RDB;
    }

    @Override
    public String getType(){
        return "rdb";
    }

    @Override
    public String getDescription(){
        return "Store birthmarks to Relational Database.";
    }
}
