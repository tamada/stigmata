package com.github.stigmata.printer.csv;

import com.github.stigmata.printer.BirthmarkServicePrinter;
import com.github.stigmata.printer.ComparisonPairPrinter;
import com.github.stigmata.printer.ComparisonResultSetPrinter;
import com.github.stigmata.printer.ExtractionResultSetPrinter;
import com.github.stigmata.spi.ResultPrinterService;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class CsvResultPrinterService implements ResultPrinterService{
    private BirthmarkServiceCsvPrinter serviceList = new BirthmarkServiceCsvPrinter();
    private ExtractionResultSetCsvPrinter list = new ExtractionResultSetCsvPrinter();
    private ComparisonPairCsvPrinter pairPrinter = new ComparisonPairCsvPrinter(list);
    private ComparisonResultSetCsvPrinter compare = new ComparisonResultSetCsvPrinter();

    /**
     * returns a localized description of the birthmark this service provides.
     */
    @Override
    public String getDescription(){
        return "Print Birthmarks in Csv Format";
    }

    @Override
    public String getFormat(){
        return "csv";
    }
    
    @Override
    public ComparisonResultSetPrinter getComparisonResultSetPrinter() {
        return compare;
    }

    @Override
    public ExtractionResultSetPrinter getExtractionResultSetPrinter() {
        return list;
    }

    @Override
    public BirthmarkServicePrinter getBirthmarkServicePrinter() {
        return serviceList;
    }

    @Override
    public ComparisonPairPrinter getComparisonPairPrinter(){
        return pairPrinter;
    }
}
