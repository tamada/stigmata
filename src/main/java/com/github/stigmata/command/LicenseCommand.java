package com.github.stigmata.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.Stigmata;

/**
 * 
 * @author Haruaki Tamada
 */
public class LicenseCommand extends AbstractStigmataCommand{
    @Override
    public String getCommandString(){
        return "license";
    }

    @Override
    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        try{
            InputStream in = getClass().getResourceAsStream("/META-INF/license.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
            reader.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
