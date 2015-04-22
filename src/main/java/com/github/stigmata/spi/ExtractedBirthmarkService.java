package com.github.stigmata.spi;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkStoreTarget;
import com.github.stigmata.ExtractionResultSet;
import com.github.stigmata.result.history.ExtractedBirthmarkHistory;

/**
 * This service provider interface manages extracted birthmark histories.
 * 
 * @author Haruaki Tamada
 */
public interface ExtractedBirthmarkService{
    /**
     * finds and returns history from given parameter.
     * @param parameter search base.
     * @return found history.
     */
    public ExtractedBirthmarkHistory getHistory(String parameter);

    public ExtractionResultSet getResultSet(String id);

    public ExtractionResultSet createResultSet(BirthmarkContext context);

    public BirthmarkStoreTarget getTarget();

    public String getType();

    public String getDescription();
}
