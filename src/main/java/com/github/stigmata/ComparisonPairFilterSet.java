package com.github.stigmata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Set of {@link ComparisonPairFilter <Ccode>ComparisonPairFilter</code>}.
 *
 * @author Haruaki TAMADA
 */
public class ComparisonPairFilterSet implements Iterable<ComparisonPairFilter>{
    private List<ComparisonPairFilter> filters = new ArrayList<ComparisonPairFilter>();
    private String name;
    private boolean matchall = true;

    public boolean isFiltered(ComparisonPair pair){
        boolean flag;
        if(isMatchAll()){ // all of criteria are matched?
            flag = true;
            for(ComparisonPairFilter filter: filters){
                if(!filter.isFiltered(pair)){
                    flag = false;
                    break;
                }
            }
        }
        else{ // any of criteria are matched
            flag = false;
            for(ComparisonPairFilter filter: filters){
                if(filter.isFiltered(pair)){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * returns a flag that all filters must be matched.
     */
    public boolean isMatchAll(){
        return matchall;
    }

    /**
     * returns a flag that any filters must be matched.
     */
    public boolean isMatchAny(){
        return !isMatchAll();
    }

    /**
     * filtering if all criteria is matched.
     */
    public void setMatchAll(){
        matchall = true;
    }

    /**
     * filtering if any criterion is matched.
     */
    public void setMatchAny(){
        matchall = false;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public boolean addFilter(ComparisonPairFilter o){
        return filters.add(o);
    }

    public void removeAllFilters(){
        filters.clear();
    }

    public ComparisonPairFilter getFilter(int index){
        return filters.get(index);
    }

    @Override
    public Iterator<ComparisonPairFilter> iterator(){
        return filters.iterator();
    }

    public ComparisonPairFilter removeFilter(int index){
        return filters.remove(index);
    }

    public int getFilterCount(){
        return filters.size();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("filterset{ ");
        sb.append("name=").append(getName()).append(", ");
        sb.append(isMatchAll()? "match_all": "match_any");
        sb.append(", ").append(filters).append("}");
        
        return new String(sb);
    }
}
