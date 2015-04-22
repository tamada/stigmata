package com.github.stigmata.result.history;

import java.sql.SQLException;
import java.util.Iterator;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;

import com.github.stigmata.ExtractionResultSet;
import com.github.stigmata.result.RDBExtractionResultSet;
import com.github.stigmata.result.RDBExtractionResultSet.StringHandler;
import com.github.stigmata.utils.ArrayIterator;

/**
 * 
 * @author Haruaki Tamada
 */
public class RDBExtractedBirthmarkHistory implements ExtractedBirthmarkHistory{
    private DataSource source;

    public RDBExtractedBirthmarkHistory(DataSource source){
        this.source = source;
    }

    @Override
    public void deleteResultSet(String id){
        QueryRunner runner = new QueryRunner(source);

        try{
            runner.update("DELETE FROM EXTRACTED_BIRTHMARK_TYPES WHERE EXTRACTED_ID=?", id);
            runner.update("DELETE FROM EXTRACTED_BIRTHMARK WHERE EXTRACTED_ID=?", id);
            runner.update("DELETE FROM EXTRACTED_BIRTHMARKS WHERE EXTRACTED_ID=?", id);
        } catch(SQLException e){
        }
    }

    @Override
    public void deleteAllResultSets(){
        QueryRunner runner = new QueryRunner(source);
        try{
            runner.update("DELETE FROM EXTRACTED_BIRTHMARK_TYPES");
            runner.update("DELETE FROM EXTRACTED_BIRTHMARK");
            runner.update("DELETE FROM EXTRACTED_BIRTHMARKS");
        } catch(SQLException e){
        }
    }

    @Override
    public ExtractionResultSet getResultSet(String id){
        return new RDBExtractionResultSet(source, id);
    }

    @Override
    public String[] getResultSetIds(){
        QueryRunner runner = new QueryRunner(source);
        try{
            String[] ids = (String[])runner.query(
                "SELECT EXTRACTED_ID FROM EXTRACTED_BIRTHMARKS", new StringHandler()
            );
            return ids;
        } catch(SQLException e){
        }
        return new String[0];
    }

    @Override
    public Iterator<String> iterator(){
        return new ArrayIterator<String>(getResultSetIds());
    }

    @Override
    public void refresh(){
        // do nothing.
    }
}
