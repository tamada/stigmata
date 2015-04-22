package com.github.stigmata.printer.csv;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.stigmata.ComparisonPair;
import com.github.stigmata.ComparisonResultSet;
import com.github.stigmata.printer.AbstractComparisonResultSetPrinter;
import com.github.stigmata.result.CertainPairComparisonResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class ComparisonResultSetCsvPrinter extends AbstractComparisonResultSetPrinter{
    public ComparisonResultSetCsvPrinter(){
    }

    @Override
    public void printResult(PrintWriter out, ComparisonResultSet resultset){
        if(resultset instanceof CertainPairComparisonResultSet){
            printResultImpl(out, (CertainPairComparisonResultSet)resultset);
        }
        else{
            printResultImpl(out, resultset);
        }
    }

    private void printResultImpl(PrintWriter out, CertainPairComparisonResultSet resultset){
        for(ComparisonPair pair: resultset){
            out.print(pair.getTarget1().getName());
            out.print(",");
            out.print(pair.getTarget1().getName());
            out.print(",");
            out.println(pair.calculateSimilarity());
        }
    }

    private void printResultImpl(PrintWriter out, ComparisonResultSet resultset){
        Map<String, Map<String, Double>> map = new LinkedHashMap<String, Map<String, Double>>();
        List<String> names = new ArrayList<String>();

        long time = System.currentTimeMillis();
        for(ComparisonPair pair: resultset){
            Map<String, Double> val = map.get(pair.getTarget1().getName());
            if(val == null){
                val = new HashMap<String, Double>();
            }
            val.put(pair.getTarget2().getName(), new Double(pair.calculateSimilarity()));
            if(!names.contains(pair.getTarget2().getName())){
                names.add(pair.getTarget2().getName());
            }
            map.put(pair.getTarget1().getName(), val);
        }

        for(String name: names){
            out.print(",");
            out.print(name);
        }
        out.println();
        for(Map.Entry<String, Map<String, Double>> entry: map.entrySet()){
            out.print(entry.getKey());
            Map<String, Double> element = entry.getValue();
            for(String name: names){
                out.print(",");
                Double v = element.get(name);
                if(v != null){
                    out.print(v.doubleValue());
                }
            }
            out.println();
        }
        out.flush();
        long elapsedTime = System.currentTimeMillis() - time;
        System.err.println("Time: " + elapsedTime + "ms");
    }
}
