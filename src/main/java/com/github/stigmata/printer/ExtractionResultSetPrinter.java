package com.github.stigmata.printer;

import java.io.PrintWriter;

import com.github.stigmata.ExtractionResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public interface ExtractionResultSetPrinter{
    public void printResult(PrintWriter out, ExtractionResultSet ers);

    public String getResult(ExtractionResultSet ers);
}
