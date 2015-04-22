package com.github.stigmata.command;

import java.io.PrintWriter;
import java.util.Iterator;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkEngine;
import com.github.stigmata.ComparisonMethod;
import com.github.stigmata.ComparisonResultSet;
import com.github.stigmata.ExtractionResultSet;
import com.github.stigmata.Stigmata;
import com.github.stigmata.event.BirthmarkEngineAdapter;
import com.github.stigmata.event.BirthmarkEngineEvent;
import com.github.stigmata.event.WarningMessages;
import com.github.stigmata.printer.ComparisonResultSetPrinter;
import com.github.stigmata.spi.ResultPrinterService;

/**
 * 
 * @author Haruaki Tamada
 */
public class CompareCommand extends AbstractStigmataCommand{
    @Override
    public boolean isAvailableArguments(String[] args){
        return args.length > 0;
    }

    @Override
    public String getCommandString(){
        return "compare";
    }

    @Override
    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        try{
            BirthmarkEngine engine = new BirthmarkEngine(context.getEnvironment());
            context.setComparisonMethod(ComparisonMethod.ROUND_ROBIN_SAME_PAIR);
            engine.addBirthmarkEngineListener(new BirthmarkEngineAdapter(){
                @Override
                public void operationDone(BirthmarkEngineEvent e){
                    WarningMessages warnings = e.getMessage();
                    for(Iterator<Exception> i = warnings.exceptions(); i.hasNext(); ){
                        i.next().printStackTrace();
                    }
                }
            });

            ExtractionResultSet rs = engine.extract(args, context);
            ComparisonResultSet resultset = engine.compare(rs);
            if(context.hasFilter()){
                resultset = engine.filter(resultset);
            }

            ResultPrinterService spi = stigmata.getPrinterManager().getService(context.getFormat());
            ComparisonResultSetPrinter formatter = spi.getComparisonResultSetPrinter();
            formatter.printResult(new PrintWriter(System.out), resultset);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
