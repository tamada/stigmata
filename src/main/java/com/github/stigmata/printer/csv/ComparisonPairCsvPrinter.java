package com.github.stigmata.printer.csv;

import java.io.PrintWriter;

import com.github.stigmata.ComparisonPair;
import com.github.stigmata.ComparisonPairElement;
import com.github.stigmata.printer.AbstractComparisonPairPrinter;

public class ComparisonPairCsvPrinter extends AbstractComparisonPairPrinter{
    private ExtractionResultSetCsvPrinter list;

    public ComparisonPairCsvPrinter(ExtractionResultSetCsvPrinter list){
        this.list = list;
    }

    @Override
    public void printResult(PrintWriter out, ComparisonPair pair){
        list.printBirthmarkSet(out, pair.getTarget1());
        list.printBirthmarkSet(out, pair.getTarget2());

        for(ComparisonPairElement element: pair){
            out.print("compare,");
            out.print(element.getType());
            out.print(",");
            out.println(element.getSimilarity());
        }
        out.flush();
    }
}
