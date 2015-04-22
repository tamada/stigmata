package com.github.stigmata.result;

import java.io.File;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.BirthmarkStoreTarget;
import com.github.stigmata.ExtractionResultSet;
import com.github.stigmata.result.history.ExtractedBirthmarkHistory;
import com.github.stigmata.result.history.XmlFileExtractedBirthmarkHistory;
import com.github.stigmata.spi.ExtractedBirthmarkService;

/**
 * 
 * @author Haruaki Tamada
 */
public class XmlFileExtractedBirthmarkService implements ExtractedBirthmarkService{
    private File defaultBaseDirectory;

    public XmlFileExtractedBirthmarkService(){
        defaultBaseDirectory = new File(
            BirthmarkEnvironment.getStigmataHome(),
            "extracted_birthmarks"
        );
    }

    @Override
    public ExtractionResultSet createResultSet(BirthmarkContext context){
        return createResultSet(context, defaultBaseDirectory);
    }

    public ExtractionResultSet createResultSet(BirthmarkContext context, File base){
        return new XmlFileExtractionResultSet(
            context, new File(base, AbstractExtractionResultSet.generateId())
        );
    }

    @Override
    public ExtractedBirthmarkHistory getHistory(String parameter){
        File file = defaultBaseDirectory;
        if(parameter != null){
            file = new File(parameter);
        }
        if(!file.exists()){
            file.mkdirs();
        }
        return new XmlFileExtractedBirthmarkHistory(file);
    }

    @Override
    public ExtractionResultSet getResultSet(String id){
        return new XmlFileExtractionResultSet(new File(id));
    }

    @Override
    public BirthmarkStoreTarget getTarget(){
        return BirthmarkStoreTarget.XMLFILE;
    }

    @Override
    public String getType(){
        return "xmlfile";
    }

    @Override
    public String getDescription(){
        return "Store birthmarks into Xml File";
    }
}
