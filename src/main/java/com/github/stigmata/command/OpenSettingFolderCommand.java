package com.github.stigmata.command;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.Stigmata;

public class OpenSettingFolderCommand extends AbstractStigmataCommand{

    @Override
    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        try{
            Desktop.getDesktop().open(new File(BirthmarkEnvironment.getStigmataHome()));
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getCommandString(){
        return "open-setting-folder";
    }
}
