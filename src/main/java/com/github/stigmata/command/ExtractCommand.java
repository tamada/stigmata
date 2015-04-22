package com.github.stigmata.command;

import java.io.PrintWriter;
import java.util.Iterator;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkEngine;
import com.github.stigmata.ComparisonMethod;
import com.github.stigmata.ExtractionResultSet;
import com.github.stigmata.Stigmata;
import com.github.stigmata.event.BirthmarkEngineAdapter;
import com.github.stigmata.event.BirthmarkEngineEvent;
import com.github.stigmata.event.WarningMessages;
import com.github.stigmata.printer.ExtractionResultSetPrinter;
import com.github.stigmata.spi.ResultPrinterService;

/**
 * 
 * @author Haruaki Tamada
 */
public class ExtractCommand extends AbstractStigmataCommand{
    @Override
    public boolean isAvailableArguments(String[] args){
        return args.length > 0;
    }

    @Override
    public String getCommandString(){
        return "extract";
    }

    @Override
    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        try{
            context.setComparisonMethod(ComparisonMethod.ROUND_ROBIN_SAME_PAIR);
            BirthmarkEngine engine = new BirthmarkEngine(context.getEnvironment());

            engine.addBirthmarkEngineListener(new BirthmarkEngineAdapter(){
                @Override
                public void operationDone(BirthmarkEngineEvent e){
                    WarningMessages warnings = e.getMessage();
                    for(Iterator<Exception> i = warnings.exceptions(); i.hasNext(); ){
                        i.next().printStackTrace();
                    }
                }
            });
            ExtractionResultSet ers = engine.extract(args, context);

            ResultPrinterService spi = stigmata.getPrinterManager().getService(context.getFormat());
            ExtractionResultSetPrinter formatter = spi.getExtractionResultSetPrinter();
            formatter.printResult(new PrintWriter(System.out), ers);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
