package com.github.stigmata.spi;

import com.github.stigmata.ComparisonPairFilter;

/**
 * Service provider interface for filtering comparison pair.
 * 
 * @author Haruaki TAMADA
 */
public interface ComparisonPairFilterService{
    public String getFilterName();

    public String getDescription();

    public ComparisonPairFilter getFilter();
}
