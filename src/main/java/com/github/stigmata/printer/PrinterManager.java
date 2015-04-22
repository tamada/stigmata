package com.github.stigmata.printer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.printer.csv.CsvResultPrinterService;
import com.github.stigmata.spi.ResultPrinterService;

/**
 *
 * @author Haruaki TAMADA
 */
public class PrinterManager{
    private static final PrinterManager manager = new PrinterManager();
    private ServiceLoader<ResultPrinterService> serviceLoader;

    private Map<String, ResultPrinterService> formats = new HashMap<String, ResultPrinterService>();

    private PrinterManager(){
        serviceLoader = ServiceLoader.load(ResultPrinterService.class);
        load();
    }

    public void refresh(){
        serviceLoader.reload();
        load();
    }

    public static void refresh(BirthmarkEnvironment env){
        PrinterManager instance = getInstance();
        instance.formats.clear();
        for(Iterator<ResultPrinterService> i = env.lookupProviders(ResultPrinterService.class); i.hasNext(); ){
            instance.addService(i.next());
        }
    }

    public static ResultPrinterService getDefaultFormatService(){
        return new CsvResultPrinterService();
    }

    public static PrinterManager getInstance(){
        return manager;
    }

    public ResultPrinterService getService(String format){
        return formats.get(format);
    }

    private void load(){
        formats.clear();
        for(Iterator<ResultPrinterService> i = serviceLoader.iterator(); i.hasNext(); ){
            ResultPrinterService spi = i.next();
            addService(spi);
        }
    }

    private void addService(ResultPrinterService service){
        formats.put(service.getFormat(), service);
    }
}
