package com.github.stigmata.printer;

import java.io.PrintWriter;

import com.github.stigmata.ComparisonPair;

/**
 * 
 * @author Haruaki Tamada
 */
public interface ComparisonPairPrinter{
    public String getResult(ComparisonPair pair);

    public void printResult(PrintWriter out, ComparisonPair pair);
}
