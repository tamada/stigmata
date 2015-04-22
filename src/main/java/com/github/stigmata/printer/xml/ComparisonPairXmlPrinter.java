package com.github.stigmata.printer.xml;

import java.io.PrintWriter;

import com.github.stigmata.BirthmarkSet;
import com.github.stigmata.ComparisonPair;
import com.github.stigmata.ComparisonPairElement;
import com.github.stigmata.printer.AbstractComparisonPairPrinter;

public class ComparisonPairXmlPrinter extends AbstractComparisonPairPrinter{
    private ExtractionResultSetXmlPrinter list;

    public ComparisonPairXmlPrinter(ExtractionResultSetXmlPrinter list){
        this.list = list;
    }

    @Override
    public void printResult(PrintWriter out, ComparisonPair pair){
        printHeader(out);
        out.println("  <extracted-birthmarks>");
        list.printBirthmarkSet(out, pair.getTarget1());
        list.printBirthmarkSet(out, pair.getTarget2());
        out.println("  </extracted-birthmarks>");
        out.println("  <comparison-result-set>");
        printComparisonPair(out, pair);
        out.println("  </comparison-result-set>");
        printFooter(out);
    }

    @Override
    public void printHeader(PrintWriter out){
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<birthmark-result-set>");
    }

    @Override
    public void printFooter(PrintWriter out){
        out.println("</birthmark-result-set>");
        out.flush();
    }
    public void printComparisonPair(PrintWriter out, ComparisonPair pair){
        out.println("    <comparison-result>");
        printTarget(out, pair.getTarget1(), 1);
        printTarget(out, pair.getTarget2(), 2);
        out.println("      <birthmark-similarities>");
        for(ComparisonPairElement element: pair){
            printPairElement(out, element);
        }
        out.println("      </birthmark-similarities>");
        out.print("      <similarity>");
        out.print(pair.calculateSimilarity());
        out.println("</similarity>");
        out.println("    </comparison-result>");
    }

    private void printTarget(PrintWriter out, BirthmarkSet set, int index){
        out.printf("      <target%d>%n", index);
        out.printf("        <name>%s</name>%n", list.escapeToXmlString(set.getName()));
        out.printf("        <location>%s</location>%n", list.escapeToXmlString(set.getLocation()));
        out.printf("      </target%d>%n", index);
    }

    private void printPairElement(PrintWriter out, ComparisonPairElement e){
        out.printf("        <birthmark-similarity type=\"%s\" comparison-count=\"%d\">%g</birthmark-similarity>%n",
                   e.getType(), e.getComparisonCount(), e.getSimilarity());

    }
}
