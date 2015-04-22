package com.github.stigmata;

import com.github.stigmata.filter.Criterion;
import com.github.stigmata.spi.ComparisonPairFilterService;

/**
 * Filtering results by some criteria.
 * For example,
 * <ul>
 *   <li>extract comparison pairs which similarity over 0.8, and</li>
 *   <li>extract comparison pairs which similarity over 0.8 and element count over 10.</li>
 * </ul>
 *
 * @author Haruaki TAMADA
 */
public interface ComparisonPairFilter{
    public boolean isFiltered(ComparisonPair pair);

    public Criterion[] getAcceptableCriteria();

    public boolean isAcceptable(Criterion criterion);

    public void setCriterion(Criterion criterion);

    public Criterion getCriterion();

    public ComparisonPairFilterService getService();
}
