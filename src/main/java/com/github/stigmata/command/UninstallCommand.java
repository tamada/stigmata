package com.github.stigmata.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.Stigmata;
import com.github.stigmata.spi.BirthmarkService;
import com.github.stigmata.utils.ConfigFileExporter;

/**
 * 
 * @author Haruaki Tamada
 */
@Deprecated
public class UninstallCommand extends AbstractStigmataCommand{

    @Override
    public boolean isAvailableArguments(String[] args){
        return args.length > 0;
    }

    @Override
    public String getCommandString(){
        return "uninstall";
    }

    @Override
    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        File pluginsDir = new File(BirthmarkEnvironment.getStigmataHome(), "plugins");
        BirthmarkEnvironment env = context.getEnvironment();
        boolean removeServiceInConfigFile = false;

        for(int i = 0; i < args.length; i++){
            BirthmarkService service = env.getService(args[i]);
            if(service instanceof BirthmarkService){
                env.removeService(args[i]);
                removeServiceInConfigFile = true;
            }
            else{
                String fileName = getPluginFileNameOfService(context, service);
                if(fileName != null){
                    File pluginFile = new File(pluginsDir, fileName);
                    pluginFile.renameTo(new File(pluginFile.getParentFile(), pluginFile.getName() + ".back"));
                }
            }
        }
        if(removeServiceInConfigFile){
            updateConfigFile(env);
        }
    }

    private void updateConfigFile(BirthmarkEnvironment env){
        File configFile = new File(BirthmarkEnvironment.getStigmataHome(), "stigmata.xml");
        try{
            new ConfigFileExporter(env).export(new PrintWriter(new FileWriter(configFile)));
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private String getPluginFileNameOfService(BirthmarkContext context, BirthmarkService service){
        Class<?> serviceClass = service.getClass();
        URL location = serviceClass.getResource("/" + serviceClass.getName().replace('.', '/') + ".class");

        if(location != null){
            Pattern pattern = Pattern.compile("jar:(.*)/plugins/(.*.jar)!([a-zA-Z0-9$/.]+.class)");
            Matcher matcher = pattern.matcher(location.toString());

            if(matcher.matches()){
                try{
                    URL homeLocation = new File(BirthmarkEnvironment.getStigmataHome()).toURI().toURL();
                    String matchedLocation = matcher.group(1) + "/";
                    if(matchedLocation.equals(homeLocation.toString())){
                        return matcher.group(2);
                    }
                } catch(MalformedURLException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
