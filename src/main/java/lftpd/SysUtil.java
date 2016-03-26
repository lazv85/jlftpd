package lftpd;
import java.util.logging.Logger;

public class SysUtil{
    private static SysUtil instance = new SysUtil();
    
    private Logger log = Logger.getLogger(SysUtil.class.getName());
    
    public static SysUtil getInstance(){
        return instance;
    }
    
    
    
}