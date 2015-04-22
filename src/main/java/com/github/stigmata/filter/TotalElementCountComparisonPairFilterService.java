package com.github.stigmata.filter;

import com.github.stigmata.ComparisonPairFilter;
import com.github.stigmata.spi.ComparisonPairFilterService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class TotalElementCountComparisonPairFilterService implements ComparisonPairFilterService{

    @Override
    public ComparisonPairFilter getFilter(){
        return new TotalElementCountComparisonPairFilter(this);
    }

    @Override
    public String getFilterName(){
        return "totalelementcount";
    }

    @Override
    public String getDescription(){
        return "Filtering Element Count";
    }
}
