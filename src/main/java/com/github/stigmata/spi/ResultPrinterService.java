package com.github.stigmata.spi;

import com.github.stigmata.printer.BirthmarkServicePrinter;
import com.github.stigmata.printer.ComparisonPairPrinter;
import com.github.stigmata.printer.ComparisonResultSetPrinter;
import com.github.stigmata.printer.ExtractionResultSetPrinter;

/**
 * Service provider interface for printing comparison/extracting
 * result to certain output stream.
 *
 * @author Haruaki TAMADA
 */
public interface ResultPrinterService{
    /**
     * return a format.
     */
    public String getFormat();

    public String getDescription();

    public BirthmarkServicePrinter getBirthmarkServicePrinter();

    public ComparisonResultSetPrinter getComparisonResultSetPrinter();

    public ComparisonPairPrinter getComparisonPairPrinter();

    public ExtractionResultSetPrinter getExtractionResultSetPrinter();
}
