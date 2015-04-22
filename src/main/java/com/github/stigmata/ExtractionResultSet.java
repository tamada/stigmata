package com.github.stigmata;

import java.util.Iterator;

/**
 * This interface represents a set of extracted birthmarks.
 * 
 * @author Haruaki Tamada
 */
public interface ExtractionResultSet extends Iterable<BirthmarkSet>{
    public BirthmarkStoreTarget getStoreTarget();

    /**
     * returns an environment of extraction result.
     */
    public BirthmarkEnvironment getEnvironment();

    /**
     * returns a context of extraction result.
     */
    public BirthmarkContext getContext();

    public String getId();

    /**
     * sets extraction 
     */
    public boolean isTableType();

    /**
     * sets table type comparison flag.
     * @see #isTableType()
     */
    public void setTableType(boolean flag);

    /**
     * returns types of extracted birthmarks.
     */
    public String[] getBirthmarkTypes();

    public ExtractionUnit getExtractionUnit();

    /**
     * returns the number of target birthmark-set size (# of classes, packages, or jar files).
     */
    public int getBirthmarkSetSize();

    /**
     * returns an iterator for all of birthmark-sets.
     */
    @Override
    public Iterator<BirthmarkSet> iterator();

    /**
     * returns a birthmark-set of given index.
     */
    public BirthmarkSet getBirthmarkSet(int index);

    /**
     * returns a birthmark-set of given name.
     */
    public BirthmarkSet getBirthmarkSet(String name);

    /**
     * returns an array for all of birthmark-sets.
     */
    public BirthmarkSet[] getBirthmarkSets();

    /**
     * removes given birthmark-set from this object.
     */
    public void removeBirthmarkSet(BirthmarkSet bs);

    /**
     * removes all of birthmark-set this object has.
     */
    public void removeAllBirthmarkSets();

    /**
     * returns the number of birthmark-set to specified extraction target (TARGET_X, TARGET_Y, TARGET_XY, or TARGET_BOTH).
     */
    public int getBirthmarkSetSize(ExtractionTarget target);

    /**
     * returns an iterator of birthmark-sets from specified extraction target.
     */
    public Iterator<BirthmarkSet> birthmarkSets(ExtractionTarget target);

    /**
     * 
     */
    public BirthmarkSet getBirthmarkSet(ExtractionTarget target, int index);

    /**
     * 
     */
    public BirthmarkSet getBirthmarkSet(ExtractionTarget target, String setname);

    /**
     * 
     */
    public BirthmarkSet[] getBirthmarkSets(ExtractionTarget target);

    /**
     * adds birthmark-set to extraction target.
     * This method must be called when building birthmark-set is completely finished.
     * All of birthmark must be added to birthmark-set.
     * Because, if the concrete class of this interface stores given birthmark-set to database,
     * birthmarks is parsed and store into target database in this method.
     * @throws IllegalArgumentsException target is ExtractionTarget.TARGET_BOTH 
     */
    public void addBirthmarkSet(ExtractionTarget target, BirthmarkSet set) throws BirthmarkStoreException;

    /**
     * remove all of birthmark-set this object has, then, adds each birthmark-sets to this object. 
     * @see #removeAllBirthmarkSets(ExtractionTarget)
     * @see #addBirthmarkSet(ExtractionTarget, BirthmarkSet)
     * @throws IllegalArgumentsException target is ExtractionTarget.TARGET_BOTH 
     */
    public void setBirthmarkSets(ExtractionTarget target, BirthmarkSet[] sets) throws BirthmarkStoreException;

    /**
     * remove specified birthmark-set from specified extraction target.
     */
    public void removeBirthmarkSet(ExtractionTarget target, BirthmarkSet set);

    /**
     * remove all birthmark-sets from specified extraction target.
     */
    public void removeAllBirthmarkSets(ExtractionTarget target);
}
