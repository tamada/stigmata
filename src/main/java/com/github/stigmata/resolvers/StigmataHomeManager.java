package com.github.stigmata.resolvers;

import java.io.File;
import java.util.ServiceLoader;

/**
 * 
 * @author Haruaki Tamada
 */
public class StigmataHomeManager implements StigmataHomeResolver{
    private String home;

    @Override
    public String getStigmataHome(){
        if(home == null){
            home = resolveHome();
        }
        return home;
    }

    private String resolveHome(){
        ServiceLoader<StigmataHomeResolver> loader = ServiceLoader.load(StigmataHomeResolver.class);
        String home = null;
        String name = System.getProperty("os.name");

        for(StigmataHomeResolver resolver: loader){
            if(resolver.isTarget(name)){
                home = resolver.getStigmataHome();
                break;
            }
        }
        if(home == null){
            home = System.getProperty("stigmata.home");
            if(home == null){
                home = System.getenv("STIGMATA_HOME");
            }
            if(home == null){
                home = getUserHome() + File.separator + ".stigmata";
            }
        }
        return home;
    }

    static final String getUserHome(){
        String userHome = System.getProperty("user.home");
        if(userHome == null){
            userHome = System.getenv("HOME");
        }
        if(userHome == null){
            userHome = ".";
        }
        return userHome;
    }

    @Override
    public boolean isTarget(String osName){
        return true;
    }
}
