package com.github.stigmata.printer;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.github.stigmata.ComparisonPair;

/**
 * 
 * @author Haruaki Tamada
 */
public abstract class AbstractComparisonPairPrinter implements ComparisonPairPrinter, Printer{

    @Override
    public abstract void printResult(PrintWriter out, ComparisonPair pair);

    @Override
    public void printFooter(PrintWriter out){
        out.flush();
    }

    @Override
    public void printHeader(PrintWriter out){
    }

    @Override
    public String getResult(ComparisonPair pair){
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        printResult(out, pair);

        out.close();
        return writer.toString();
    }
}
