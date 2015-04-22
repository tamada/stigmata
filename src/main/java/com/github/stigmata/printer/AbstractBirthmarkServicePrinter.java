package com.github.stigmata.printer;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.github.stigmata.spi.BirthmarkService;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public abstract class AbstractBirthmarkServicePrinter implements BirthmarkServicePrinter, Printer{
    @Override
    public abstract void printResult(PrintWriter out, BirthmarkService[] spilist);

    @Override
    public String getResult(BirthmarkService[] spilist){
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        printResult(out, spilist);

        out.close();
        return writer.toString();
    }

    @Override
    public void printHeader(PrintWriter out){
    }

    @Override
    public void printFooter(PrintWriter out){
        out.flush();
    }
}
