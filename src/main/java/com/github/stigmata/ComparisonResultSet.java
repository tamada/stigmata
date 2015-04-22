package com.github.stigmata;

import java.util.Iterator;

/**
 * result set of birthmark comparison.
 *
 * @author Haruaki TAMADA
 */
public interface ComparisonResultSet extends Iterable<ComparisonPair>{
    /**
     * the birthmark environment.
     */
    public BirthmarkEnvironment getEnvironment();

    /**
     * the birthmark context.
     */
    public BirthmarkContext getContext();

    /**
     * a iterator for {@link ComparisonPair <code>ComparisonPair</code>}.
     */
    @Override
    public Iterator<ComparisonPair> iterator();

    /**
     * a comparison pair at given index.
     */
    public ComparisonPair getPairAt(int index);

    /**
     * all comparison pairs.
     */
    public ComparisonPair[] getPairs();

    /**
     * comparison pair count of this instance has.
     */
    public int getPairCount();

    /**
     * comparison source.
     */
    public BirthmarkSet[] getPairSources();

    /**
     * returns an array of comparison sources.
     */
    public Iterator<BirthmarkSet> pairSources();
}
