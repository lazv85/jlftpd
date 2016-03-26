package lftpd;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public final class Config {
    
    private static Config instance = new Config();
    private Logger log = Logger.getLogger(Config.class.getName());
    private String configName;

    public static Config getInstance(){
        return instance;
    }
    
    private String clearComment(String str){
        Pattern ptr = Pattern.compile("(^[^#]*)#?");
        //Pattern ptr = Pattern.compile("(a)bc");
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
    
    public void parseFile(String configName){
        if(! this.configName.isEmpty()){
            log.warning("File " + this.configName + " already parsed");
            return;
        }
        
        this.configName = configName;
        
        return;
    }
    
    
    
}