package com.github.stigmata.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.ComparisonPairFilter;
import com.github.stigmata.ComparisonPairFilterSet;
import com.github.stigmata.Stigmata;
import com.github.stigmata.spi.BirthmarkService;
import com.github.stigmata.spi.ReflectedBirthmarkService;

/**
 * configuration file parser.
 * 
 * @author Haruaki TAMADA
 */
public class ConfigFileImporter{
    private BirthmarkEnvironment environment;

    public ConfigFileImporter(BirthmarkEnvironment environment){
        this.environment = environment;
    }

    public BirthmarkEnvironment parse(InputStream in) throws IOException{
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            Handler handler = new Handler(getEnvironment());
            parser.parse(in, handler);
            this.environment = handler.getEnvironment();

            return environment;
        }catch(ParserConfigurationException e){
            throw new IOException(e);
        }catch(SAXException e){
            throw new IOException(e);
        }
    }

    public BirthmarkEnvironment getEnvironment(){
        return environment;
    }

    private static enum Part{
        WELLKNOWN_CLASSES, PROPERTIES, CLASSPATH, SERVICES, FILTER_SET, FILTER_DEFINITION,
    }

    private static class Handler extends DefaultHandler{
        private BirthmarkEnvironment environment;
        private WellknownClassManager manager;
        private Map<String, String> serviceMap;
        private ComparisonPairFilterSet filter;
        private Part part;
        private boolean exclude;
        private WellknownClassJudgeRule.MatchType matchType;
        private WellknownClassJudgeRule.MatchPartType partType;
        private String qname;
        private String key;
        private String filterType, filterCriterion, attributeName;
        private Map<String, String> filterAttributes = new HashMap<String, String>();

        public Handler(BirthmarkEnvironment environment){
            if(environment == null){
                environment = Stigmata.getInstance().createEnvironment();
            }
            this.environment = environment;
            this.manager = environment.getWellknownClassManager();
        }

        public BirthmarkEnvironment getEnvironment(){
            return environment;
        }

        @Override
        public void startElement(String uri, String localName, String qname,
                                 Attributes attributes) throws SAXException{
            this.qname = qname;

            if(qname.equals("wellknown-classes")){
                part = Part.WELLKNOWN_CLASSES;
            }
            else if(qname.equals("property")){
                part = Part.PROPERTIES;
            }
            else if(qname.equals("classpath-list")){
                part = Part.CLASSPATH;
            }
            else if(qname.equals("birthmark-service")){
                part = Part.SERVICES;
            }
            else if(qname.equals("filterset-list")){
                part = Part.FILTER_SET;
            }

            if(part == Part.FILTER_SET){
                if(qname.equals("filterset")){
                    filter = new ComparisonPairFilterSet();
                }
                else if(qname.equals("filter")){
                    part = Part.FILTER_DEFINITION;
                    filterAttributes.clear();
                }
            }
            else if(part == Part.WELLKNOWN_CLASSES){
                if(qname.equals("exclude")){
                    exclude = true;
                }
                else if(qname.equals("package-name")){
                    partType = WellknownClassJudgeRule.MatchPartType.PACKAGE_NAME;
                }
                else if(qname.equals("class-name")){
                    partType = WellknownClassJudgeRule.MatchPartType.CLASS_NAME;
                }
                else if(qname.equals("fully-name")){
                    partType = WellknownClassJudgeRule.MatchPartType.FULLY_NAME;
                }
                else if(qname.equals("suffix")){
                    matchType = WellknownClassJudgeRule.MatchType.SUFFIX;
                }
                else if(qname.equals("prefix")){
                    matchType = WellknownClassJudgeRule.MatchType.PREFIX;
                }
                else if(qname.equals("match")){
                    matchType = WellknownClassJudgeRule.MatchType.EXACT;
                }
                else if(qname.equals("not-match")){
                    matchType = WellknownClassJudgeRule.MatchType.NOT_MATCH;
                }
            }
        }

        @Override
        public void characters(char[] data, int offset, int length) throws SAXException{
            String value = new String(data, offset, length).trim();

            if(value.length() > 0){
                if(part == Part.PROPERTIES){
                    if(qname.equals("name")){
                        key = value;
                    }
                    else if(qname.equals("value")){
                        environment.addProperty(key, value);
                    }
                }
                else if(part == Part.WELLKNOWN_CLASSES
                        && (qname.equals("suffix") || qname.equals("prefix") || qname.equals("match"))){
                    manager.add(new WellknownClassJudgeRule(value, matchType, partType, exclude));
                    exclude = false;
                }
                else if(part == Part.CLASSPATH && qname.equals("classpath")){
                    try{
                        environment.getClasspathContext().addClasspath(new URL(value));
                    }catch(MalformedURLException e){
                        throw new SAXException(e);
                    }
                }
                else if(part == Part.SERVICES){
                    if(serviceMap == null){
                        serviceMap = new HashMap<String, String>();
                    }
                    serviceMap.put(qname, value);
                }
                else if(part == Part.FILTER_SET){
                    if(qname.equals("name")){
                        filter.setName(value);
                    }
                    else if(qname.equals("match")){
                        if(value.equals("all")){
                            filter.setMatchAll();
                        }
                        else{
                            filter.setMatchAny();
                        }
                    }
                }
                else if(part == Part.FILTER_DEFINITION){
                    if(qname.equals("filter-type")){
                        filterType = value;
                    }
                    else if(qname.equals("criterion")){
                        filterCriterion = value;
                    }
                    else if(qname.equals("name")){
                        attributeName = value;
                    }
                    else{
                        filterAttributes.put(attributeName, value);
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localname, String qname){
            if(part == Part.SERVICES && qname.equals("birthmark-service")){
                BirthmarkService service = new ReflectedBirthmarkService(
                    serviceMap.get("type"),
                    serviceMap.get("description"),
                    serviceMap.get("extractor"),
                    serviceMap.get("comparator")
                );
                environment.addService(service);
            }
            else if(part == Part.FILTER_DEFINITION && qname.equals("filter")){
                ComparisonPairFilter f = environment.getFilterManager().buildFilter(
                    filterType, filterCriterion, filterAttributes
                );
                filter.addFilter(f);
                part = Part.FILTER_SET;
            }
            else if(part == Part.FILTER_SET && qname.equals("filterset")){
                environment.getFilterManager().addFilterSet(filter);
            }
        }
    }
}
