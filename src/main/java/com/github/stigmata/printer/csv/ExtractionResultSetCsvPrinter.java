package com.github.stigmata.printer.csv;

import java.io.PrintWriter;
import java.util.Iterator;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.BirthmarkSet;
import com.github.stigmata.ExtractionResultSet;
import com.github.stigmata.ExtractionTarget;
import com.github.stigmata.printer.AbstractExtractionResultSetPrinter;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class ExtractionResultSetCsvPrinter extends AbstractExtractionResultSetPrinter{
    @Override
    public void printResult(PrintWriter out, ExtractionResultSet ers){
        printHeader(out);
        for(Iterator<BirthmarkSet> i = ers.birthmarkSets(ExtractionTarget.TARGET_BOTH); i.hasNext(); ){
            printBirthmarkSet(out, i.next());
        }
        printFooter(out);
    }

    protected void printBirthmarkSet(PrintWriter out, BirthmarkSet holder){
        for(String type: holder.getBirthmarkTypes()){
            out.print(holder.getName());
            out.print(",");
            out.print(holder.getLocation());

            Birthmark birthmark = holder.getBirthmark(type);
            out.print(",");
            out.print(birthmark.getType());
            for(Iterator<BirthmarkElement> elements = birthmark.iterator(); elements.hasNext(); ){
                out.print(",");
                out.print(elements.next());
            }
            out.println();
        }
    }
}
