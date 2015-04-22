package com.github.stigmata.filter;

import com.github.stigmata.ComparisonPairFilter;
import com.github.stigmata.spi.ComparisonPairFilterService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class TargetNameComparisonPairFilterService implements ComparisonPairFilterService{

    @Override
    public ComparisonPairFilter getFilter(){
        return new TargetNameComparisonPairFilter(this);
    }

    @Override
    public String getFilterName(){
        return "name";
    }

    @Override
    public String getDescription(){
        return "Filtering by Target Name";
    }
}
