package com.github.stigmata.resolvers;

import java.io.File;

public class WindowsStigmataHomeResolver implements StigmataHomeResolver{
    @Override
    public String getStigmataHome(){
        String home = StigmataHomeManager.getUserHome();

        if(home.startsWith("C:\\Documents and Settings\\")){
            home = home + File.separator + "Application Data" + File.separator + "Stigmata";
        }
        else if(home.startsWith("C:\\Users\\")){ // for windows 7
            home = home + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "Stigmata";
        }
        return home;
    }

    @Override
    public boolean isTarget(String osName){
        return osName != null && osName.toLowerCase().contains("windows");
    }
}
