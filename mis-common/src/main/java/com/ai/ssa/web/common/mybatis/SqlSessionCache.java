package com.ai.ssa.web.common.mybatis; 

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * mybatis的mapper文件有改动时，进行重新加载
 * @author ycblus
 *
 */
@Component
public class SqlSessionCache {     
    private Logger log  = Logger.getLogger(SqlSessionCache.class);    
    
    @Value("${mybatis.mapperLocations}")
    private String mapperLocations ;
    
    private String mapperPath;
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    
    
    @Resource
    private SqlSessionTemplate sql;
    
    WatchService watcher;
    
    Configuration configuration;
    private HashMap<String, Long> fileMapping = new HashMap<String, Long>();// 记录文件是否变化 
    @PostConstruct
    public void init(){
      String path = mapperLocations.split(",")[0];
      mapperPath = path.substring(5,path.lastIndexOf("/"));
       try {
        watcher = FileSystems.getDefault().newWatchService();
        Paths.get(mapperPath).register(watcher,   
            StandardWatchEventKinds.ENTRY_CREATE,  
            StandardWatchEventKinds.ENTRY_DELETE,  
            StandardWatchEventKinds.ENTRY_MODIFY);
        FileThread ft = new FileThread();
        Thread t = new Thread(ft);
        t.start();
      } catch (IOException e) {
        e.printStackTrace();
      }  
        
    }
    
    public void  refreshMapper(List<File> flist) throws Exception{    
      this.replaceMap();
      Configuration configuration = sqlSessionFactory.getConfiguration();
      Field loadedResourcesField = configuration.getClass().getDeclaredField("loadedResources");  
      loadedResourcesField.setAccessible(true);
      Set loadedResourcesSet = ((Set)loadedResourcesField.get(configuration));  
     
     
      
      for (File f : flist) {
          InputStream inputStream = new FileInputStream(f); 
          String resource = f.getAbsolutePath();   
          loadedResourcesSet.remove(resource);
          XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(inputStream, configuration,   
              resource, configuration.getSqlFragments());  
          xmlMapperBuilder.parse(); 
        }
        
    } 
    
    public void replaceMap() throws  Exception{
      Configuration configuration = sqlSessionFactory.getConfiguration();
      String[] mapFieldNames = new String[]{  
          "mappedStatements", "caches",  
          "resultMaps", "parameterMaps",  
          "keyGenerators", "sqlFragments"  
      };  
      for (String fieldName : mapFieldNames){  
          Field field = configuration.getClass().getDeclaredField(fieldName);  
          field.setAccessible(true);  
          Map map = ((Map)field.get(configuration));  
          if (!(map instanceof StrictMap)){  
              Map newMap = new StrictMap(StringUtils.capitalize(fieldName) + "collection");  
              for (Object key : map.keySet()){  
                  try {  
                      newMap.put(key, map.get(key));  
                  }catch(IllegalArgumentException ex){  
                      newMap.put(key, ex.getMessage());  
                  }  
              }  
              field.set(configuration, newMap);  
          }  
      }  
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
      String x = "file:ssaConf/service-mybatis/mapper/*.xml"; 
    }
 
 
    public void watchFileEvent() { 
      InputStream inputStream = null;
      try {
        WatchKey key = watcher.take();
        boolean valid = key.reset();    
        if (!valid) {  
            return;  
        }   
        this.replaceMap();
        Configuration configuration = sqlSessionFactory.getConfiguration();
        Field loadedResourcesField = configuration.getClass().getDeclaredField("loadedResources");  
        loadedResourcesField.setAccessible(true);
        Set loadedResourcesSet = ((Set)loadedResourcesField.get(configuration));   
        System.out.println("start reload XML--------------------------");
       for (WatchEvent<?> event: key.pollEvents()) {  
         
           Path watchablePath = (Path) key.watchable();
           File dir = new File(watchablePath.toUri()); 
           String path = dir.getAbsolutePath()+"/"+  event.context(); 
           if( path.endsWith(".xml")){   
             String resource = path.toString();    
             loadedResourcesSet.remove(resource);
             if(!event.kind().toString().equals("ENTRY_DELETE")){
               File f =  new File(path);
                inputStream = new FileInputStream(f); 
               XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(inputStream, configuration,   
                   resource, configuration.getSqlFragments());  
               xmlMapperBuilder.parse(); 
             }
           }
           
           
          }
       System.out.println("end reload XML--------------------------");
 
        
      } catch (Exception e) {
        e.printStackTrace();
      }finally{
        if(inputStream!=null)
          try {
            inputStream.close();
          } catch (IOException e) { 
            e.printStackTrace();
          }
      }
      
    }
    
    class FileThread implements Runnable{   
      @Override  
      public void run(){
        while (true) {
          watchFileEvent(); 
        } 
      }  
   }  
    
    
    protected static class StrictMap<V> extends HashMap<String, V> {

      private static final long serialVersionUID = -4950446264854982944L;
      private final String name;

      public StrictMap(String name, int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.name = name;
      }

      public StrictMap(String name, int initialCapacity) {
        super(initialCapacity);
        this.name = name;
      }

      public StrictMap(String name) {
        super();
        this.name = name;
      }

      public StrictMap(String name, Map<String, ? extends V> m) {
        super(m);
        this.name = name;
      }

      @SuppressWarnings("unchecked")
      public V put(String key, V value) {
        remove(key);  
        if (containsKey(key)) {
          throw new IllegalArgumentException(name + " already contains value for " + key);
        }
        if (key.contains(".")) {
          final String shortKey = getShortName(key);
          if (super.get(shortKey) == null) {
            super.put(shortKey, value);
          } else {
            super.put(shortKey, (V) new Ambiguity(shortKey));
          }
        }
        return super.put(key, value);
      }

      public V get(Object key) {
        V value = super.get(key);
        if (value == null) {
          throw new IllegalArgumentException(name + " does not contain value for " + key);
        }
       
        if (value instanceof Ambiguity) { 
          throw new IllegalArgumentException(((Ambiguity) value).getSubject() + " is ambiguous in " + name
              + " (try using the full name including the namespace, or rename one of the entries)");
        }
        return value;
      }

      private String getShortName(String key) {
        final String[] keyParts = key.split("\\.");
        return keyParts[keyParts.length - 1];
      }

      protected static class Ambiguity {
        final private String subject;

        public Ambiguity(String subject) {
          this.subject = subject;
        }

        public String getSubject() {
          return subject;
        }
      }
    }

}