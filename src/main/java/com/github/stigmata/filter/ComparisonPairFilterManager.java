package com.github.stigmata.filter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.ComparisonPairFilter;
import com.github.stigmata.ComparisonPairFilterSet;
import com.github.stigmata.spi.ComparisonPairFilterService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class ComparisonPairFilterManager{
    private Map<String, ComparisonPairFilterService> services = new HashMap<String, ComparisonPairFilterService>();
    private Map<String, ComparisonPairFilterSet> filters = new HashMap<String, ComparisonPairFilterSet>();
    private ComparisonPairFilterManager parent;

    public ComparisonPairFilterManager(BirthmarkEnvironment env, ComparisonPairFilterManager parent){
        this.parent = parent;
        for(Iterator<ComparisonPairFilterService> i = env.lookupProviders(ComparisonPairFilterService.class); i.hasNext(); ){
            ComparisonPairFilterService service = i.next();
            if(getService(service.getFilterName()) != null){
                services.put(service.getFilterName(), service);
            }
        }
    }

    public ComparisonPairFilterManager(BirthmarkEnvironment env){
        for(Iterator<ComparisonPairFilterService> i = env.lookupProviders(ComparisonPairFilterService.class); i.hasNext(); ){
            ComparisonPairFilterService service = i.next();
            services.put(service.getFilterName(), service);
        }
    }

    public synchronized ComparisonPairFilterSet[] getFilterSets(){
        List<ComparisonPairFilterSet> list = new ArrayList<ComparisonPairFilterSet>();
        if(parent != null){
            for(ComparisonPairFilterSet fs: parent.getFilterSets()){
                if(filters.get(fs.getName()) == null){
                    list.add(fs);
                }
            }
        }
        list.addAll(filters.values());
        return list.toArray(new ComparisonPairFilterSet[list.size()]);
    }

    public synchronized ComparisonPairFilterSet[] getFilterSets(String[] names){
        List<ComparisonPairFilterSet> list = new ArrayList<ComparisonPairFilterSet>();
        for(int i = 0; i < names.length; i++){
            list.add(getFilterSet(names[i]));
        }
        return list.toArray(new ComparisonPairFilterSet[list.size()]);
    }

    public void addFilterSet(ComparisonPairFilterSet filterset){
        filters.put(filterset.getName(), filterset);
    }

    public void removeFilterSet(String filterSetName){
        if(filters.get(filterSetName) != null){
            filters.remove(filterSetName);
        }
        else{
            if(parent != null && parent.getFilterSet(filterSetName) != null){
                filters.remove(filterSetName);
            }
        }
    }

    public ComparisonPairFilterSet getFilterSet(String filterSetName){
        ComparisonPairFilterSet filter = filters.get(filterSetName);
        if(filter == null && parent != null){
            filter = parent.getFilterSet(filterSetName);
        }
        return filter;
    }

    public ComparisonPairFilter buildFilter(String filterName, String criterion, Map<String, String> values){
        Criterion c = Criterion.valueOf(criterion);
        if(c != null){
            return buildFilter(filterName, c, values);
        }
        throw new IllegalArgumentException("criterion not found: " + criterion);
    }

    public ComparisonPairFilter buildFilter(String filterName, Criterion criterion, Map<String, String> values){
        ComparisonPairFilter filter = createFilter(filterName);
        if(filter != null){
            filter.setCriterion(criterion);
            for(Map.Entry<String, String> entry: values.entrySet()){
                try{
                    Object value = entry.getValue();
                    if(entry.getKey().equals("target")){
                        value = FilterTarget.valueOf(String.valueOf(value));
                    }
                    BeanUtils.setProperty(filter, entry.getKey(), value);
                }catch(IllegalAccessException e){
                    e.printStackTrace();
                    filter = null;
                }catch(InvocationTargetException e){
                    e.printStackTrace();
                    filter = null;
                }
            }
        }
        return filter;
    }

    public ComparisonPairFilter createFilter(String filterName){
        if(hasService(filterName)){
            return getService(filterName).getFilter();
        }
        return null;
    }

    public ComparisonPairFilterService removeService(String name){
        if(parent != null && parent.hasService(name)){
            parent.removeService(name);
        }
        return services.remove(name);
    }

    public void addService(ComparisonPairFilterService service){
        if(parent == null || parent.getService(service.getFilterName()) == null){
            services.put(service.getFilterName(), service);
        }
    }

    public boolean hasService(String name){
        return (parent != null && parent.hasService(name)) || services.get(name) != null;
    }

    public ComparisonPairFilterService getService(String name){
        ComparisonPairFilterService service = null;
        if(parent != null){
            service = parent.getService(name);
        }
        if(service == null){
            service = services.get(name);
        }
        return service;
    }

}
