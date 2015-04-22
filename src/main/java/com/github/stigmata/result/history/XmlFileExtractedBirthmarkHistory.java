package com.github.stigmata.result.history;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.ExtractionResultSet;
import com.github.stigmata.result.XmlFileExtractionResultSet;
import com.github.stigmata.utils.Utility;

/**
 * 
 * @author Haruaki Tamada
 */
public class XmlFileExtractedBirthmarkHistory implements ExtractedBirthmarkHistory{
    private File parent;
    private Map<String, File> files = new LinkedHashMap<String, File>();

    public XmlFileExtractedBirthmarkHistory(){
        this(new File(BirthmarkEnvironment.getStigmataHome(), "extracted_birthmarks"));
    }

    public XmlFileExtractedBirthmarkHistory(String path){
        this(new File(path));
    }

    public XmlFileExtractedBirthmarkHistory(File parent){
        this.parent = parent;

        refresh();
    }

    @Override
    public void refresh(){
        files.clear();

        if(parent.exists()){
            for(File file: parent.listFiles()){
                if(isTarget(file)){
                    files.put(file.getName(), file);
                }
            }
        }
    }

    @Override
    public void deleteAllResultSets(){
        for(File file: parent.listFiles()){
            if(isTarget(file)){
                Utility.deleteDirectory(file);
            }
        }
    }

    @Override
    public void deleteResultSet(String id){
        File file = files.get(id);
        if(file != null){
            if(file.isDirectory()){
                Utility.deleteDirectory(file);
            }
            else{
                file.delete();
            }
        }
    }

    @Override
    public ExtractionResultSet getResultSet(String id){
        File file = files.get(id);
        if(file != null){
            return new XmlFileExtractionResultSet(file);
        }
        return null;
    }

    @Override
    public synchronized String[] getResultSetIds(){
        return files.keySet().toArray(new String[files.size()]);
    }

    @Override
    public Iterator<String> iterator(){
        return files.keySet().iterator();
    }

    private boolean isTarget(File file){
        return file.isDirectory()
            && file.getName().matches("\\d\\d\\d\\d\\d\\d\\d\\d-\\d\\d\\d\\d\\d\\d.\\d\\d\\d"); 
    }
}
