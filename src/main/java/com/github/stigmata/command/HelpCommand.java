package com.github.stigmata.command;

import java.util.ResourceBundle;

import jp.sourceforge.talisman.xmlcli.ResourceHelpFormatter;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.ComparisonPairFilter;
import com.github.stigmata.ComparisonPairFilterSet;
import com.github.stigmata.Stigmata;
import com.github.stigmata.StigmataCommand;
import com.github.stigmata.spi.BirthmarkService;

/**
 * 
 * @author Haruaki Tamada
 */
public class HelpCommand extends AbstractStigmataCommand{
    private Options options;

    public HelpCommand(Options options){
        this.options = options;
    }

    @Override
    public String getCommandString(){
        return "help";
    }

    @Override
    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        BirthmarkEnvironment env = context.getEnvironment();
        Package p = getClass().getPackage();
        StigmataCommandFactory factory = StigmataCommandFactory.getInstance(); 

        ResourceBundle helpResource = ResourceBundle.getBundle("resources.options");
        HelpFormatter formatter = new ResourceHelpFormatter(helpResource);
        String defaultCommand = getDefaultCommandName(factory);
        String commandList = getCommandList(factory);

        formatter.printHelp(
            String.format(
                helpResource.getString("cli.interface"),
                p.getImplementationVersion(),
                commandList, defaultCommand
            ),
            options
        );
        System.out.println();
        System.out.println(helpResource.getString("cli.interface.birthmarks"));
        for(BirthmarkService service: env.getServices()){
            if(!service.isExperimental()){
                System.out.printf("    %-5s (%s): %s%n", service.getType(),
                        service.getType(), service.getDescription());
            }
        }
        System.out.println();
        System.out.println(helpResource.getString("cli.interface.filters"));
        for(ComparisonPairFilterSet filterset: env.getFilterManager().getFilterSets()){
            String matchString = helpResource.getString("cli.interface.filter.matchall");
            if(filterset.isMatchAny()) matchString = helpResource.getString("cli.interface.filter.matchany");
            System.out.printf("    %s (%s)%n", filterset.getName(), matchString);
            for(ComparisonPairFilter filter: filterset){
                System.out.printf("        %s%n", filter);
            }
        }
        System.out.println();
        System.out.println(helpResource.getString("cli.interface.copyright"));
        System.out.println(helpResource.getString("cli.interface.mailto"));
    }

    public String getCommandList(StigmataCommandFactory factory){
        StringBuilder sb = new StringBuilder();

        for(StigmataCommand command: factory){
            if(sb.length() != 0){
                sb.append(", ");
            }
            sb.append("`").append(command.getCommandString()).append("'");
        }

        return new String(sb);
    }

    private String getDefaultCommandName(StigmataCommandFactory factory){
        return "`" + factory.getDefaultCommand().getCommandString() + "'";
    }
}
