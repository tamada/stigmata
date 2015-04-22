package com.github.stigmata.resolvers;

import java.io.File;

public class MacOSXStigmataHomeResolver implements StigmataHomeResolver{
    @Override
    public String getStigmataHome(){
        String home = StigmataHomeManager.getUserHome();

        if(home.startsWith("/Users/")){
            home = home + File.separator + "Library/Application Support" + File.separator + "Stigmata";
        }

        return home;
    }

    @Override
    public boolean isTarget(String osName){
        return osName != null && osName.toLowerCase().contains("mac");
    }
}
