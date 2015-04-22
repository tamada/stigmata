package com.github.stigmata;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import com.github.stigmata.digger.ClasspathContext;
import com.github.stigmata.filter.ComparisonPairFilterManager;
import com.github.stigmata.resolvers.StigmataHomeManager;
import com.github.stigmata.result.history.ExtractedBirthmarkServiceManager;
import com.github.stigmata.spi.BirthmarkService;
import com.github.stigmata.utils.NullIterator;
import com.github.stigmata.utils.WellknownClassManager;

/**
 * This class represents the context for extracting/comparing birthmarks.
 * 
 * @author  Haruaki TAMADA
 */
public class BirthmarkEnvironment{
    /**
     * Default environment. All instance of this class is based on default environment.
     */
    private static BirthmarkEnvironment DEFAULT_ENVIRONMENT = new BirthmarkEnvironment(true);

    /**
     * home directory path.
     */
    private static StigmataHomeManager stigmataHome = new StigmataHomeManager();

    /**
     * parent of this environment.
     */
    private BirthmarkEnvironment parent;

    /**
     * context for classpath.
     */
    private ClasspathContext classpathContext;

    /**
     * wellknown class manager. This object judge a class is user made class or
     * wellknown class.
     */
    private WellknownClassManager manager;

    /**
     * collection of services.
     */
    private Map<String, BirthmarkService> services = new HashMap<String, BirthmarkService>();

    /**
     * properties manager.
     */
    private Map<String, String> properties = new HashMap<String, String>();

    /**
     * listeners for updating properties.
     */
    private List<PropertyChangeListener> propertyListeners = new ArrayList<PropertyChangeListener>();

    /**
     * filter manager.
     */
    private ComparisonPairFilterManager filterManager;

    /**
     * history manager.
     */
    private ExtractedBirthmarkServiceManager historyManager;

    /**
     * 
     */
    private ClassLoader loader;

    /**
     * constructor for root environment
     */
    private BirthmarkEnvironment(boolean flag){
        manager = new WellknownClassManager();
        classpathContext = new ClasspathContext();
        filterManager = new ComparisonPairFilterManager(this);
        historyManager = new ExtractedBirthmarkServiceManager(this);

        findDefaultServices();
    }

    /**
     * constructor for specifying parent environment.
     */
    public BirthmarkEnvironment(BirthmarkEnvironment parent){
        this.parent = parent;
        this.manager = new WellknownClassManager(parent.getWellknownClassManager());
        this.classpathContext = new ClasspathContext(parent.getClasspathContext());
        this.filterManager = new ComparisonPairFilterManager(this, parent.getFilterManager());
        this.historyManager = new ExtractedBirthmarkServiceManager(this, parent.getHistoryManager());
    }

    /**
     * returns the default birthmark environment.
     */
    public static final BirthmarkEnvironment getDefaultEnvironment(){
        return DEFAULT_ENVIRONMENT;
    }

    public static synchronized final String getStigmataHome(){
        return stigmataHome.getStigmataHome();
    }

    static void resetSettings(){
        DEFAULT_ENVIRONMENT = new BirthmarkEnvironment(false);
    }

    public BirthmarkEnvironment getParent(){
        return parent;
    }

    /**
     * remove property mapped given key.
     */
    public void removeProperty(String key){
        String old = properties.get(key);
        properties.remove(key);
        firePropertyEvent(new PropertyChangeEvent(this, key, old, null));
    }

    /**
     * add given property.
     */
    public void addProperty(String key, String value){
        boolean contains = properties.containsKey(key);
        String old = getProperty(key);
        properties.put(key, value);

        // value is updated?
        if(!((old != null && old.equals(value)) ||
             (contains && old == null && value == null))){
            firePropertyEvent(new PropertyChangeEvent(this, key, old, value));
        }
    }

    /**
     * returns the property mapped given key
     */
    public String getProperty(String key){
        String value = properties.get(key);
        if(value == null && parent != null){
            value = parent.getProperty(key);
        }
        return value;
    }

    /**
     * fire property change event to listeners.
     * @param e Event object.
     */
    private void firePropertyEvent(PropertyChangeEvent e){
        for(PropertyChangeListener listener: propertyListeners){
            listener.propertyChange(e);
        }
    }

    /**
     * add listener for updating properties.
     */
    public void addPropertyListener(PropertyChangeListener listener){
        propertyListeners.add(listener);
    }

    /**
     * remove specified listener.
     */
    public void removePropertyListener(PropertyChangeListener listener){
        propertyListeners.remove(listener);
    }

    public void clearProperties(){
        properties.clear();
    }

    public Iterator<String> propertyKeys(){
        Set<String> set = new HashSet<String>();
        if(parent != null){
            for(Iterator<String> i = parent.propertyKeys(); i.hasNext(); ){
                set.add(i.next());
            }
        }
        set.addAll(properties.keySet());
        return set.iterator();
    }

    /**
     * returns the classpath context.
     */
    public ClasspathContext getClasspathContext(){
        return classpathContext;
    }

    /**
     * add given birthmark service to this environment.
     */
    public synchronized void addService(BirthmarkService service){
        if(parent == null || parent.getService(service.getType()) == null){
            services.put(service.getType(), service);
        }
    }

    /**
     * remove given birthmark service from this environment.
     */
    public void removeService(String type){
        services.remove(type);
    }

    /**
     * return birthmark service registered with given birthmark type.
     */
    public BirthmarkService getService(String type){
        BirthmarkService service = null;
        for(BirthmarkService bs: services.values()){
            if(bs.isType(type)){
                service = bs;
            }
        }
        if(service == null && parent != null){
            service = parent.getService(type);
        }
        return service;
    }

    /**
     * return all birthmark services searching traverse to root environment.
     */
    public synchronized BirthmarkService[] getServices(){
        List<BirthmarkService> list = getServiceList();
        BirthmarkService[] services = list.toArray(new BirthmarkService[list.size()]);
        Arrays.sort(services, new BirthmarkSpiComparator());

        return services;
    }

    public <T> Iterator<T> lookupProviders(Class<T> providerClass){
        ServiceLoader<T> services;
        if(loader != null){
            services = ServiceLoader.load(providerClass, loader);
        }
        else{
            services = ServiceLoader.load(providerClass);
        }

        Iterator<T> iterator;
        if(services != null){
            iterator = services.iterator();
        }
        else{
            iterator = new NullIterator<T>();
        }
        return iterator;
    }

    /**
     * return birthmark services lookup from current class path.
     */
    public synchronized BirthmarkService[] findServices(){
        List<BirthmarkService> list = getServiceList();

        for(Iterator<BirthmarkService> i = lookupProviders(BirthmarkService.class); i.hasNext(); ){
            BirthmarkService spi = i.next();
            if(getService(spi.getType()) == null){
                list.add(spi);
            }
        }
        BirthmarkService[] services = list.toArray(new BirthmarkService[list.size()]);
        Arrays.sort(services, new BirthmarkSpiComparator());

        return services;
    }

    /**
     * return wellknown class manager.
     */
    public WellknownClassManager getWellknownClassManager(){
        return manager;
    }

    public ComparisonPairFilterManager getFilterManager(){
        return filterManager;
    }

    public ExtractedBirthmarkServiceManager getHistoryManager(){
        return historyManager;
    }

    void setClassLoader(ClassLoader loader){
        this.loader = loader;
    }

    /**
     * find the all birthmark services searching to root environment.
     */
    private List<BirthmarkService> getServiceList(){
        List<BirthmarkService> list = new ArrayList<BirthmarkService>();
        if(parent != null){
            for(BirthmarkService spi: parent.getServices()){
                list.add(spi);
            }
        }
        for(String key : services.keySet()){
            list.add(services.get(key));
        }
        return list;
    }

    private void findDefaultServices(){
        for(BirthmarkService service: ServiceLoader.load(BirthmarkService.class)){
            addService(service);
        }
    }
}
