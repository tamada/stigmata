package com.github.stigmata.result;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.BirthmarkSet;
import com.github.stigmata.BirthmarkStoreException;
import com.github.stigmata.BirthmarkStoreTarget;
import com.github.stigmata.ExtractionTarget;
import com.github.stigmata.ExtractionUnit;
import com.github.stigmata.Stigmata;
import com.github.stigmata.utils.ArrayIterator;

/**
 * 
 * @author Haruaki Tamada
 */
public class RDBExtractionResultSet extends AbstractExtractionResultSet{
    private String id;
    private DataSource source;
    private boolean initFlag = true;

    public RDBExtractionResultSet(BirthmarkContext context){
        super(context);

        initializeDataSource();
    }

    public RDBExtractionResultSet(DataSource source, String id){
        super(Stigmata.getInstance().createContext());

        this.source = source;
        this.id = id;

        initializeDataSource();
        initialization(getContext());
    }

    @Override
    public String[] getBirthmarkTypes(){
        if(initFlag){
            String[] types = select(
                "SELECT TYPE FROM EXTRACTED_BIRTHMARK_TYPES WHERE EXTRACTED_ID = ?",
                new StringHandler(), id
            );
            if(types == null){
                types = new String[0];
            }
            return types;
        }
        return super.getBirthmarkTypes();
    }

    @Override
    public ExtractionUnit getExtractionUnit(){
        if(initFlag){
            String[] units = select(
                "SELECT UNIT FROM EXTRACTED_BIRTHMARKS WHERE EXTRACTED_ID = ?",
                new StringHandler(), id
            );
            if(units != null && units.length > 0){
                return ExtractionUnit.valueOf(units[0]);
            }
            return ExtractionUnit.CLASS;
        }
        return super.getExtractionUnit();
    }

    @Override
    public void addBirthmarkSet(ExtractionTarget target, BirthmarkSet set) throws BirthmarkStoreException{
        
    }

    @Override
    public BirthmarkSet[] getBirthmarkSets(ExtractionTarget target){
        return select(
            "SELECT * FROM EXTRACTED_BIRTHMARK WHERE EXTRACTED_ID = ? AND STORE_TARGET = ? ORDER TO TYPE, INDEX",
            new BirthmarkSetListHandler(getEnvironment()), id, target.name()
        );
    }

    @Override
    public Iterator<BirthmarkSet> birthmarkSets(ExtractionTarget target){
        return new ArrayIterator<BirthmarkSet>(getBirthmarkSets(target));
    }

    @Override
    public int getBirthmarkSetSize(ExtractionTarget target){
        Integer o = (Integer)select(
            "SELECT DISTINCT COUNT(LOCATION) FROM EXTRACTED_BIRTHMARK WEHERE ID=? and STORE_TARGET=?",
            new ScalarHandler(), id, target.name()
        );
        return o.intValue();
    }

    @Override
    public void removeAllBirthmarkSets(ExtractionTarget target){
        QueryRunner qr = new QueryRunner(source);
        try{
            qr.update(
                "DELETE FROM EXTRACTED_BIRTHMARK WHERE ID=? and STORE_TARGET=?",
                new Object[] { id, target.name(), }
            );
        } catch(SQLException e){
        }
    }

    @Override
    public void removeBirthmarkSet(ExtractionTarget target, BirthmarkSet set){
        QueryRunner qr = new QueryRunner(source);
        try{
            qr.update(
                "DELETE FROM EXTRACTED_BIRTHMARK WHERE ID=? and LOCATION=? and STORE_TARGET=?",
                new Object[] { id, set.getLocation(), target.name(), }
            );
        } catch(SQLException e){
        }
    }

    @Override
    public BirthmarkStoreTarget getStoreTarget(){
        return BirthmarkStoreTarget.RDB;
    }

    private <T> T select(String sql, ResultSetHandler<T> handler, Object... parameters){
        QueryRunner qr = new QueryRunner(source);
        try{
            return qr.<T>query(sql, handler, parameters);
        } catch(SQLException e){
        }
        return null;
    }

    private void initialization(BirthmarkContext context){
        context.setStoreTarget(BirthmarkStoreTarget.RDB);
        context.setBirthmarkTypes(getBirthmarkTypes());
        context.setExtractionUnit(getExtractionUnit());

        initFlag = false;
    }

    private void initializeDataSource(){
        try{
            Context namingContext = new InitialContext();
            String dataSourceName = getEnvironment().getProperty("rdb.datasource");
            if(dataSourceName == null){
                throw new IllegalStateException("property ``rdb.datasource''  is missing");
            }
            source = (DataSource)namingContext.lookup(dataSourceName);
        } catch(NamingException e){
        }
    }

    public static class StringHandler implements ResultSetHandler<String[]>{
        private int index;

        public StringHandler(){
            this(0);
        }

        public StringHandler(int index){
            this.index = index;
        }

        @Override
        public String[] handle(ResultSet rs) throws SQLException{
            List<String> list = new ArrayList<String>();
            while(rs.next()){
                list.add(rs.getString(index));
            }
            return list.toArray(new String[list.size()]);
        }
    }

    private static class BirthmarkSetListHandler implements ResultSetHandler<BirthmarkSet[]>{
        private BirthmarkEnvironment env;

        public BirthmarkSetListHandler(BirthmarkEnvironment env){
            this.env = env;
        }

        @Override
        public BirthmarkSet[] handle(ResultSet rs) throws SQLException{
            Map<URL, BirthmarkSet> map = new HashMap<URL, BirthmarkSet>();

            while(rs.next()){
                try{
                    String name = rs.getString("NAME");
                    String location = rs.getString("LOCATION");

                    URL url = new URL(location);
                    BirthmarkSet bs = map.get(url);
                    if(bs == null){
                        bs = new BirthmarkSet(name, url);
                        map.put(url, bs);
                    }
                    String type = rs.getString("TYPE");

                    Birthmark birthmark = bs.getBirthmark(type);
                    if(birthmark == null){
                        birthmark = env.getService(type).getExtractor().createBirthmark();
                        bs.addBirthmark(birthmark);
                    }
                    String element = rs.getString("ELEMENT");
                    birthmark.addElement(env.getService(type).getExtractor().buildElement(element));

                } catch(MalformedURLException e){
                }
            }
            return map.values().toArray(new BirthmarkSet[map.size()]);
        }
    }
}
