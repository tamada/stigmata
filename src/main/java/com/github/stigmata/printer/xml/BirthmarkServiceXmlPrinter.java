package com.github.stigmata.printer.xml;

import java.io.PrintWriter;

import com.github.stigmata.printer.AbstractBirthmarkServicePrinter;
import com.github.stigmata.spi.BirthmarkService;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class BirthmarkServiceXmlPrinter extends AbstractBirthmarkServicePrinter{
    @Override
    public void printResult(PrintWriter out, BirthmarkService[] spilist){
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<birthmark-result-set>");
        out.println("  <birthmark-services>");
        for(BirthmarkService spi: spilist){
            out.println("    <birthmark-service>");
            out.printf("      <type>%s</type>%n", spi.getType());
            out.printf("      <description>%s</description>%n", spi.getDescription());
            out.printf("      <class-name>%s</class-name>%n", spi.getClass().getName());
            out.println("    </birthmark-service>");
        }
        out.println("  </birthmark-services>");
        out.println("</birthmark-result-set>");

        out.flush();
    }
}
