package com.github.stigmata.filter;

import com.github.stigmata.ComparisonPairFilter;
import com.github.stigmata.spi.ComparisonPairFilterService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class SimilarityComparisonPairFilterService implements ComparisonPairFilterService{
    @Override
    public ComparisonPairFilter getFilter(){
        return new SimilarityComparisonPairFilter(this);
    }

    @Override
    public String getFilterName(){
        return "similarity";
    }

    @Override
    public String getDescription(){
        return "Filtering all comparison pair by its similarity";
    }
}
