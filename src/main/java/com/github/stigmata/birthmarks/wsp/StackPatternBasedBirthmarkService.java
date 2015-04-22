package com.github.stigmata.birthmarks.wsp;

import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.BirthmarkPreprocessor;
import com.github.stigmata.spi.BirthmarkService;

/**
 * Weighted Stack Pattern based birthmark.
 *
 * this birthmark is proposed by LIM et al. in following papers.
 * <ul>
 * <li>Hyun-il Lim, Heewan Park, Seokwoo Choi, Taisook Han, ``Detecting Theft
 * of Java Applications via a Static Birthmark Based on Weighted Stack
 * Patterns,'' IEICE Transactions on Information and Systems, Vol.E91-D No.9
 * pp.2323-2332, September 2008.</li>
 * <li>Heewan Park, Hyun-il Lim, Seokwoo Choi and Taisook Han, ``A Static Java
 * Birthmark Based on Operand Stack Behaviors,'' In Proc. of 2008
 * International Conference on Information Security and Assurance,
 * pp.133-136, April 2008.</li>
 * </ul>
 *
 * @author Haruaki Tamada
 */
public class StackPatternBasedBirthmarkService implements BirthmarkService{
    private BirthmarkPreprocessor preprocessor =
        new OpcodeWeightCalculatePreprocessor(this);
    private BirthmarkExtractor extractor =
        new StackPatternBasedBirthmarkExtractor(this);
    private BirthmarkComparator comparator =
        new StackPatternBasedBirthmarkComparator(this);

    @Override
    public String getDescription(){
        return "Weighted stack pattern based birthmark";
    }

    @Override
    public boolean isUserDefined(){
        return false;
    }

    public boolean isExperimental(){
        return false;
    }

    @Override
    public String getType(){
        return "wsp";
    }

    @Override
    public BirthmarkComparator getComparator(){
        return comparator;
    }

    @Override
    public BirthmarkExtractor getExtractor(){
        return extractor;
    }

    @Override
    public BirthmarkPreprocessor getPreprocessor(){
        return preprocessor;
    }
}
