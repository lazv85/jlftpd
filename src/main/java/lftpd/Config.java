package lftpd;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.nio.file.Paths;
import java.util.HashMap;

public final class Config {
    
    private static Config instance = new Config();
    private Logger log = Logger.getLogger(Config.class.getName());
    private String configName = new String();
    private HashMap<String, String> configData = new HashMap<String, String>();

    public static Config getInstance(){
        return instance;
    }
    
    private String clearComment(String str){
        Pattern ptr = Pattern.compile("(^[^#]*)#?");
        Matcher m = ptr.matcher(str);
        if(m.find()){
            str = m.group(1);
        }
        return str;
    }
    
    private String getSection(String str, String defaultSection){
        Pattern ptr = Pattern.compile("\\[(\\w+)\\]");
        
        Matcher m = ptr.matcher(str);
        if(m.find()){
            str = m.group(1);
        }else{
            str = defaultSection;
        }
        return str;
    }
        
    private String[] parseLine(String str){
        Pattern ptr = Pattern.compile("([^=]+)\\s*=\\s*(.+)");
        String [] outStr = new String[2];
        Matcher m = ptr.matcher(str);
        if(m.find()){
            outStr[0] = m.group(1).trim();
            outStr[1] = m.group(2).trim();
        }
        return outStr;
    }
    
    public void parseFile(String configName) throws IOException{
        if(! this.configName.isEmpty()){
            log.log(Level.WARNING,"File " + this.configName + " already parsed");
            return;
        }
        
        log.log(Level.INFO,"Reading file:" + configName);
        
        this.configName = configName;
        String section = "default";
        String []keyval ;
        
        Path file =  Paths.get(configName);
        try (InputStream in = Files.newInputStream(file); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    line = clearComment(line);
                    section = getSection(line,section);
                    keyval = parseLine(line);
                    if(keyval.length != 0){
                        configData.put(section+keyval[0],keyval[1]);
                    }
                }
        } catch (IOException x) {
            log.log(Level.SEVERE,"Error during reading config file: "+ configName);
            throw x;
        }
        
        this.configName = configName;
        
        log.log(Level.INFO,"Config file has been read");
        
        return;
    }
    
    public String getValue(String key, String section){
        String str = configData.get(section+key);
        return str;
    }
    
    public String getValue(String key){
        return this.getValue(key, "default");
    }
    
}