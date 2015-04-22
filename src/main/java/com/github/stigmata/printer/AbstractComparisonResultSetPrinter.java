package com.github.stigmata.printer;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.github.stigmata.ComparisonResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public abstract class AbstractComparisonResultSetPrinter implements ComparisonResultSetPrinter, Printer{
    @Override
    public abstract void printResult(PrintWriter out, ComparisonResultSet resultset);

    @Override
    public void printHeader(PrintWriter out){
    }

    @Override
    public void printFooter(PrintWriter out){
        out.flush();
    }

    @Override
    public String getResult(ComparisonResultSet resultset){
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        printResult(out, resultset);

        out.close();
        return writer.toString();
    }
}
