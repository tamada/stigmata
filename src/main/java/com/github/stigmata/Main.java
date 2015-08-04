package com.github.stigmata;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.sourceforge.talisman.xmlcli.CommandLinePlus;
import jp.sourceforge.talisman.xmlcli.OptionsBuilder;
import jp.sourceforge.talisman.xmlcli.XmlCliConfigurationException;
import jp.sourceforge.talisman.xmlcli.builder.OptionsBuilderFactory;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.w3c.dom.DOMException;

import com.github.stigmata.command.HelpCommand;
import com.github.stigmata.command.StigmataCommandFactory;
import com.github.stigmata.digger.ClasspathContext;
import com.github.stigmata.hook.Phase;
import com.github.stigmata.hook.StigmataHookManager;
import com.github.stigmata.spi.BirthmarkService;

/**
 * Front end class.
 * 
 * @author Haruaki TAMADA
 */
public final class Main{
    /**
     * main process.
     * @throws org.apache.commons.cli.ParseException 
     */
    public Main(String[] args) throws ParseException{
        Options options = buildOptions();
        CommandLineParser parser = new PosixParser();
        CommandLinePlus commandLine = new CommandLinePlus(parser.parse(options, args, false));

        Stigmata stigmata = Stigmata.getInstance();
        stigmata.configuration(commandLine.getOptionValue("config-file"), commandLine.hasOption("reset-config"));

        String[] arguments = commandLine.getArgs();
        String commandString = "gui";

        if(arguments.length > 0){
            commandString = arguments[0];
        }

        arguments = shiftArray(arguments);

        BirthmarkContext context = stigmata.createContext();
        updateContext(context, commandLine);

        StigmataCommandFactory factory = StigmataCommandFactory.getInstance();
        factory.registerCommand(new HelpCommand(options));

        StigmataCommand command = factory.getCommand(commandString);
        if(!command.isAvailableArguments(arguments)){
            command = factory.getCommand("help");
        }

        StigmataHookManager.getInstance().runHook(Phase.SETUP, context.getEnvironment());

        command.setUp(context.getEnvironment());
        command.perform(stigmata, context, arguments);
        command.tearDown(context.getEnvironment());
    }

    /**
     * shift right given array.
     * @param args
     */
    private String[] shiftArray(String[] args){
        String[] returnValues = args;
        if(args.length > 0){
            String[] arguments = new String[args.length - 1];
            System.arraycopy(args, 1, arguments, 0, arguments.length);
            returnValues = arguments;
        }
        return returnValues;
    }

    private void updateContext(BirthmarkContext context, CommandLinePlus cl){
        BirthmarkEnvironment env = context.getEnvironment();

        String[] birthmarks = getTargetBirthmarks(env, cl);
        for(int i = 0; i < birthmarks.length; i++){
            context.addBirthmarkType(birthmarks[i]);
        }
        if(cl.hasOption("filter")){
            String[] filters = cl.getOptionValues("filter");
            for(int i = 0; i < filters.length; i++){
                context.addFilterType(filters[i]);
            }
        }
        if(cl.hasOption("store-target")){
            String value = cl.getOptionValue("store-target");
            BirthmarkStoreTarget bst = BirthmarkStoreTarget.valueOf(value);
            if(bst == null){
                bst = BirthmarkStoreTarget.MEMORY;
            }
            context.setStoreTarget(bst);
        }
        if(cl.hasOption("extraction-unit")){
            ExtractionUnit unit = ExtractionUnit.valueOf(cl.getOptionValue("extraction-unit"));
            context.setExtractionUnit(unit);
        }
        if(cl.hasOption("format")){
            context.setFormat(cl.getOptionValue("format"));
        }
        else{
            context.setFormat("csv");
        }

        addClasspath(env.getClasspathContext(), cl);
    }

    private String[] getTargetBirthmarks(BirthmarkEnvironment env, CommandLinePlus cl){
        String[] birthmarks = cl.getOptionValues("birthmark");
        if(birthmarks == null || birthmarks.length == 0){
            List<String> birthmarkList = new ArrayList<String>();
            for(BirthmarkService service: env.getServices()){
                if(!service.isExperimental()){
                    birthmarkList.add(service.getType());
                }
            }
            birthmarks = birthmarkList.toArray(new String[birthmarkList.size()]);
        }
        return birthmarks;
    }

    private void addClasspath(ClasspathContext context, CommandLinePlus commandLine){
        String[] classpath = commandLine.getOptionValues("classpath");
        if(classpath == null){
            return;
        }

        for(String cp: classpath){
            try{
                File f = new File(cp);
                if(f.exists()){
                    context.addClasspath(f.toURI().toURL());
                }
            }catch(MalformedURLException ex){                
            }
        }
    }

    private Options buildOptions(){
        try{
            OptionsBuilderFactory factory = OptionsBuilderFactory.getInstance();
            URL location = getClass().getResource("/resources/options.xml");
            OptionsBuilder builder = factory.createBuilder(location);

            return builder.buildOptions();
        }catch(XmlCliConfigurationException ex){
            ex.printStackTrace();
        }catch(DOMException ex){
            ex.printStackTrace();
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception{
        new Main(args);
    }
}
