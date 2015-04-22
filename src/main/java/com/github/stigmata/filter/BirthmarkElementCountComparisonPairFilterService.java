package com.github.stigmata.filter;

import com.github.stigmata.ComparisonPairFilter;
import com.github.stigmata.spi.ComparisonPairFilterService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class BirthmarkElementCountComparisonPairFilterService implements ComparisonPairFilterService{

    @Override
    public ComparisonPairFilter getFilter(){
        return new BirthmarkElementCountComparisonPairFilter(this);
    }

    @Override
    public String getFilterName(){
        return "elementcount";
    }

    @Override
    public String getDescription(){
        return "Element count Filter";
    }
}
