package com.github.stigmata.command;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.Stigmata;
import com.github.stigmata.utils.ConfigFileExporter;

/**
 * 
 * @author Haruaki Tamada
 */
public class ExportConfigCommand extends AbstractStigmataCommand{

    @Override
    public String getCommandString(){
        return "export-config";
    }

    @Override
    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        try{
            PrintWriter out;
            if(args == null || args.length == 0){
                out = new PrintWriter(System.out);
            }
            else{
                if(!args[0].endsWith(".xml")){
                    args[0] = args[0] + ".xml";
                }
                out = new PrintWriter(new FileWriter(args[0]));
            }

            new ConfigFileExporter(context.getEnvironment()).export(out);
            out.close();
        }catch(IOException e){
        }
    }
}
