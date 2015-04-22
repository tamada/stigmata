package com.github.stigmata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Birthmark runtime context.
 * 
 * @author Haruaki Tamada
 */
public class BirthmarkContext{
    private BirthmarkEnvironment environment;

    private ComparisonMethod method = ComparisonMethod.ROUND_ROBIN_SAME_PAIR;
    private ExtractionUnit unit = ExtractionUnit.CLASS;
    private BirthmarkStoreTarget store = BirthmarkStoreTarget.MEMORY;

    private List<String> birthmarkTypes = new ArrayList<String>();
    private List<String> filterTypes = new ArrayList<String>();
    private Map<String, String> nameMappings = new HashMap<String, String>();
    private String format;
    /**
     * properties which available on a session.
     */
    private Map<String, Object> properties = new HashMap<String, Object>();

    /**
     * self constructor.
     */
    public BirthmarkContext(BirthmarkContext context){
        this.environment = context.getEnvironment();
        this.method = context.getComparisonMethod();
        this.unit = context.getExtractionUnit();
        this.birthmarkTypes = new ArrayList<String>(context.birthmarkTypes);
        this.filterTypes = new ArrayList<String>(context.filterTypes);
        this.nameMappings = new HashMap<String, String>(context.nameMappings);
    }

    public BirthmarkContext(BirthmarkEnvironment environment){
        this.environment = environment;
    }

    public BirthmarkEnvironment getEnvironment(){
        return environment;
    }

    public boolean hasNameMapping(){
        return getNameMappingCount() > 0;
    }

    public int getNameMappingCount(){
        return nameMappings.size();
    }

    public String getNameMapping(String key){
        return nameMappings.get(key);
    }

    public void addNameMapping(String name1, String name2){
        nameMappings.put(name1, name2);
    }

    public void removeNameMapping(String name1){
        nameMappings.remove(name1);
    }

    public Map<String, String> getNameMappings(){
        return Collections.unmodifiableMap(nameMappings);
    }

    public Iterator<Map.Entry<String, String>> nameMappingEntries(){
        return getNameMappings().entrySet().iterator();
    }

    public void setNameMappings(Map<String, String> mappings){
        nameMappings.clear();
        for(Iterator<Map.Entry<String, String>> i = mappings.entrySet().iterator(); i.hasNext(); ){
            Map.Entry<String, String> entry = i.next();
            addNameMapping(entry.getKey(), entry.getValue());
        }
    }

    public void setBirthmarkTypes(String[] types){
        birthmarkTypes.clear();
        for(int i = 0; i < types.length; i++){
            addBirthmarkType(types[i]);
        }
    }

    public void addBirthmarkType(String type){
        if(!birthmarkTypes.contains(type)){
            birthmarkTypes.add(type);
        }
    }

    public void removeBirthmarkType(String type){
        birthmarkTypes.remove(type);
    }

    public String getFormat(){
        return format;
    }

    public void setFormat(String format){
        this.format = format;
    }

    public synchronized String[] getBirthmarkTypes(){
        return birthmarkTypes.toArray(new String[getBirthmarkTypeSize()]);
    }

    public int getBirthmarkTypeSize(){
        return birthmarkTypes.size();
    }

    public ComparisonMethod getComparisonMethod(){
        return method;
    }

    public void setComparisonMethod(ComparisonMethod method){
        this.method = method;
    }

    public ExtractionUnit getExtractionUnit(){
        return unit;
    }

    public void setExtractionUnit(ExtractionUnit unit){
        this.unit = unit;
    }

    public BirthmarkStoreTarget getStoreTarget(){
        return store;
    }

    public void setStoreTarget(BirthmarkStoreTarget store){
        this.store = store;
    }

    public boolean hasFilter(){
        return filterTypes.size() > 0;
    }

    public void setFilterTypes(String[] filterTypes){
        if(filterTypes != null){
            for(int i = 0; i < filterTypes.length; i++){
                addFilterType(filterTypes[i]);
            }
        }
    }

    public void addFilterType(String filterType){
        if(filterType != null){
            filterTypes.add(filterType);
        }
    }

    public void removeFilterType(String filterType){
        filterTypes.remove(filterType);
    }

    public synchronized String[] getFilterTypes(){
        return filterTypes.toArray(new String[getFilterTypesCount()]);
    }

    public Iterator<String> filterTypes(){
        return filterTypes.iterator();
    }

    public int getFilterTypesCount(){
        return filterTypes.size();
    }

    Iterator<String> propertyKeys(){
        return properties.keySet().iterator();
    }

    public Object getProperty(String key){
        return getProperty(key, null);
    }

    public Object getProperty(String key, Object defaultValue){
        Object value = properties.get(key);
        if(value == null){
            value = defaultValue;
        }
        return value;
    }

    public void removeProperty(String key){
        properties.remove(key);
    }

    public void putProperty(String key, Object value){
        properties.put(key, value);
    }
}
