package com.github.stigmata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.stigmata.event.BirthmarkEngineListener;
import com.github.stigmata.printer.PrinterManager;
import com.github.stigmata.spi.BirthmarkService;
import com.github.stigmata.utils.ConfigFileExporter;
import com.github.stigmata.utils.ConfigFileImporter;
import com.github.stigmata.utils.ExtensionFilter;

/**
 * 
 * @author Haruaki Tamada
 */
public class Stigmata{
    /**
     * instance. singleton pattern.
     */
    private static Stigmata stigmata;
    private PrinterManager manager = PrinterManager.getInstance();
    private BirthmarkEnvironment defaultEnvironment;
    private List<BirthmarkEngineListener> listeners = new ArrayList<BirthmarkEngineListener>();

    /**
     * private constructor.
     */
    private Stigmata(){
        configuration();
    }

    /**
     * gets only instance of this class.
     */
    public static synchronized Stigmata getInstance(){
        if(stigmata == null){
            stigmata = new Stigmata();
        }
        return stigmata;
    }

    /**
     * creates a new birthmark context.
     */
    public BirthmarkContext createContext(){
        BirthmarkContext context = new BirthmarkContext(createEnvironment());
        return context;
    }

    /**
     * creates a new birthmark environment.
     */
    public BirthmarkEnvironment createEnvironment(){
        return new BirthmarkEnvironment(defaultEnvironment);
    }

    /**
     * creates a new birthmark engine.
     */
    public BirthmarkEngine createEngine(){
        return createEngine(createEnvironment());
    }

    /**
     * creates a new birthmark engine with given environment.
     */
    public BirthmarkEngine createEngine(BirthmarkEnvironment environment){
        BirthmarkEngine engine = new BirthmarkEngine(environment);
        for(BirthmarkEngineListener listener: listeners){
            engine.addBirthmarkEngineListener(listener);
        }
        return engine;
    }

    public PrinterManager getPrinterManager(){
        return manager;
    }

    public void addBirthmarkEngineListener(BirthmarkEngineListener listener){
        listeners.add(listener);
    }

    public void removeBirthmarkEngineListener(BirthmarkEngineListener listener){
        listeners.remove(listener);
    }

    public void configuration(){
        configuration(null, false);
    }

    public void configuration(String filePath, boolean resetFlag){
        InputStream target = null;
        if(filePath != null){
            try{
                target = new FileInputStream(filePath);
            } catch(FileNotFoundException e){
                filePath = null;
            }
        }

        if(filePath == null){
            String currentDirectory = System.getProperty("execution.directory");
            if(currentDirectory == null){
                currentDirectory = System.getProperty("user.dir");
            }
            File file = new File(currentDirectory, "stigmata.xml");
            if(!file.exists()){
                file = new File(BirthmarkEnvironment.getStigmataHome(), "stigmata.xml");
                if(!file.exists()){
                    file = null;
                }
            }
            if(file != null){
                try {
                    target = new FileInputStream(file);
                } catch (FileNotFoundException ex) {
                    // never throwed this exception;
                    throw new InternalError(ex.getMessage());
                }
            }
        }
        if(target == null || resetFlag){
            target = getClass().getResourceAsStream("/resources/stigmata.xml");
            if(resetFlag){
                defaultEnvironment = null;
                BirthmarkEnvironment.resetSettings();
            }
        }
        initConfiguration(target);
    }

    private void initConfiguration(InputStream in){
        if(defaultEnvironment == null){
            defaultEnvironment = BirthmarkEnvironment.getDefaultEnvironment();
        }
        buildStigmataDirectory(BirthmarkEnvironment.getStigmataHome());

        defaultEnvironment.setClassLoader(buildClassLoader("plugins"));
        try{
            ConfigFileImporter parser = new ConfigFileImporter(defaultEnvironment);
            parser.parse(in);
        } catch(IOException e){
            throw new ApplicationInitializationError(e);
        }
        for(Iterator<BirthmarkService> i = defaultEnvironment.lookupProviders(BirthmarkService.class); i.hasNext(); ){
            BirthmarkService service = i.next();
            defaultEnvironment.addService(service);
        }
        PrinterManager.refresh(defaultEnvironment);
        exportConfigFile(BirthmarkEnvironment.getStigmataHome(), "stigmata.xml");
    }

    private void buildStigmataDirectory(String homeDirectory){
        File file = new File(homeDirectory);
        if(file.exists() && file.isFile()){
            File dest = new File(file.getParent(), ".stigmata.back");
            file.renameTo(dest);
        }
        if(!file.exists()){
            file.mkdirs();
        }
        File pluginDir = new File(file, "plugins");
        if(!pluginDir.exists()){
            pluginDir.mkdirs();
        }
    }

    private boolean exportConfigFile(String parent, String fileName){
        try{
            File file = new File(parent, fileName);
            if(!file.exists()){
                ConfigFileExporter exporter = new ConfigFileExporter(defaultEnvironment);
                exporter.export(new PrintWriter(new FileWriter(file)));
            }
            return true;
        } catch(IOException e){
            // e.printStackTrace();
            return false;
        }
    }

    private static ClassLoader buildClassLoader(String path){
        File directory = new File(BirthmarkEnvironment.getStigmataHome(), path);
        File[] jarfiles = directory.listFiles(new ExtensionFilter("jar"));

        if(jarfiles == null) jarfiles = new File[0];
        try{
            URL[] urls = new URL[jarfiles.length];
            for(int i = 0; i < jarfiles.length; i++){
                urls[i] = jarfiles[i].toURI().toURL();
            }
            return new URLClassLoader(urls, Stigmata.class.getClassLoader());
        } catch(MalformedURLException e){
        }
        return null;
    }
}
