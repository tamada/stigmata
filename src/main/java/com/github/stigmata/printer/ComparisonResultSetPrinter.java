package com.github.stigmata.printer;

import java.io.PrintWriter;

import com.github.stigmata.ComparisonResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public interface ComparisonResultSetPrinter{
    public void printResult(PrintWriter out, ComparisonResultSet resultset);

    public String getResult(ComparisonResultSet resultset);
}
