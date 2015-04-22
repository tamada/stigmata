package com.github.stigmata.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.ComparisonPairFilter;
import com.github.stigmata.ComparisonPairFilterSet;
import com.github.stigmata.spi.BirthmarkService;
import com.github.stigmata.spi.ReflectedBirthmarkService;

/**
 * Export birthmark environment to xml file.
 * 
 * @author Haruaki TAMADA
 */
public class ConfigFileExporter{
    private BirthmarkEnvironment environment;

    public ConfigFileExporter(BirthmarkEnvironment environment){
        this.environment = environment;
    }

    public void export(BirthmarkEnvironment environment, PrintWriter out) throws IOException{
        new ConfigFileExporter(environment).export(out);
    }

    public void export(PrintWriter out) throws IOException{
        try{
            out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            out.println("<stigmata>");

            exportServices(out);
            exportFilters(out);
            exportWellknownClasses(out);
            exportClasspath(out);
            exportProperties(out);

            out.println("</stigmata>");
            out.flush();
        } finally{
            out.close();
        }
    }

    private void exportProperties(PrintWriter out) throws IOException{
        out.println("  <properties>");
        for(Iterator<String> i = environment.propertyKeys(); i.hasNext(); ){
            String key = i.next();
            String value = environment.getProperty(key);
            out.println("    <property>");
            out.printf("      <name>%s</name>%n", key);
            out.printf("      <value>%s</value>%n", value);
            out.println("    </property>");
        }
        out.println("  </properties>");
    }

    private void exportClasspath(PrintWriter out) throws IOException{
        out.println("  <classpath-list>");
        for(URL location: environment.getClasspathContext()){
            out.printf("    <classpath>%s</classpath>%n", location.toString());
        }
        out.println("  </classpath-list>");
    }

    private void exportWellknownClasses(PrintWriter out) throws IOException{
        out.println("  <wellknown-classes>");
        for(WellknownClassJudgeRule rule: environment.getWellknownClassManager()){
            String value = rule.getPattern();
            String tag;
            String matchtag;
            switch(rule.getMatchPartType()){
            case CLASS_NAME:
                tag = "class-name";
                break;
            case FULLY_NAME:
                tag = "fully-name";
                break;
            case PACKAGE_NAME:
                tag = "package-name";
                break;
            default:
                throw new InternalError("unknown part type: " + rule.getMatchPartType());
            }
            switch(rule.getMatchType()){
            case EXACT:
                matchtag = "match";
                break;
            case NOT_MATCH:
                matchtag = "not-match";
                break;
            case PREFIX:
                matchtag = "prefix";
                break;
            case SUFFIX:
                matchtag = "suffix";
                break;
            default:
                throw new InternalError("unknown match type: " + rule.getMatchType());
            }

            out.print("    ");
            if(rule.isExclude()) out.print("<exclude>");
            out.printf("<%s><%s>%s</%s></%s>", tag, matchtag, value, matchtag, tag);
            if(rule.isExclude()) out.print("</exclude>");
            out.println();
        }
        out.println("  </wellknown-classes>");
    }

    @SuppressWarnings("rawtypes")
    private void exportFilters(PrintWriter out) throws IOException{
        out.println("  <filterset-list>");
        for(ComparisonPairFilterSet filterset: environment.getFilterManager().getFilterSets()){
            out.println("    <filterset>");
            out.printf("      <name>%s</name>%n", filterset.getName());
            out.printf("      <match>%s</match>%n", filterset.isMatchAll()? "all": "any");
            out.println("      <filter-list>");
            for(ComparisonPairFilter filter: filterset){
                out.println("        <filter>");
                out.printf("          <filter-type>%s</filter-type>%n", filter.getService().getFilterName());
                out.printf("          <criterion>%s</criterion>%n", filter.getCriterion());
                try{
                    Map props = BeanUtils.describe(filter);
                    props.remove("service");
                    props.remove("class");
                    props.remove("criterion");
                    props.remove("acceptableCriteria");
                    out.println("          <attributes>");
                    for(Object object: props.entrySet()){
                        Map.Entry entry = (Map.Entry)object;
                        Object value = entry.getValue();
                        out.println("            <attribute>");
                        out.printf("              <name>%s</name>%n", String.valueOf(entry.getKey()));
                        if(value == null){
                            out.println("              <value></value>%n");
                        }
                        else{
                            out.printf("              <value>%s</value>%n", String.valueOf(entry.getValue()));
                        }
                        out.println("            </attribute>");
                    }
                    out.println("          </attributes>");
                } catch(Exception e){
                    e.printStackTrace();
                }
                out.println("        </filter>");
            }
            out.println("      </filter-list>");
            out.println("    </filterset>");
        }
        out.println("  </filterset-list>");
    }

    private void exportServices(PrintWriter out) throws IOException{
        out.println("  <birthmark-services>");
        for(BirthmarkService service: environment.getServices()){
            if(service.isExperimental() && service instanceof BirthmarkService){
                out.println("    <birthmark-service>");
                out.printf("      <type>%s</type>%n", service.getType());
                if(service instanceof ReflectedBirthmarkService){
                    ReflectedBirthmarkService rbs = (ReflectedBirthmarkService)service;
                    out.printf("      <description>%s</description>%n", rbs.getDescription());
                    out.printf("      <extractor>%s</extractor>%n", rbs.getExtractorClassName());
                    out.printf("      <comparator>%s</comparator>%n", rbs.getComparatorClassName());
                }
                out.println("    </birthmark-service>");
            }
        }
        out.println("  </birthmark-services>");
    }
}
