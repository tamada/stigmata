package com.github.stigmata.printer.xml;

import java.io.PrintWriter;

import com.github.stigmata.ComparisonPair;
import com.github.stigmata.ComparisonResultSet;
import com.github.stigmata.printer.AbstractComparisonResultSetPrinter;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class ComparisonResultSetXmlPrinter extends AbstractComparisonResultSetPrinter{
    private ComparisonPairXmlPrinter pairPrinter;

    public ComparisonResultSetXmlPrinter(ComparisonPairXmlPrinter pairPrinter){
        this.pairPrinter = pairPrinter;
    }

    @Override
    public void printHeader(PrintWriter out){
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<birthmark-result-set>");
        out.println("  <comparison-result-set>");
    }

    @Override
    public void printFooter(PrintWriter out){
        out.println("  </comparison-result-set>");
        out.println("</birthmark-result-set>");
        out.flush();
    }

    @Override
    public void printResult(PrintWriter out, ComparisonResultSet resultset){
        printHeader(out);
        for(ComparisonPair pair: resultset){
            pairPrinter.printComparisonPair(out, pair);
        }
        printFooter(out);
    }
}
