package lftpd;
import java.io.IOException;
import java.util.logging.Logger;

public class Server {
    
    private static Server instance = new Server();
    private Logger log = Logger.getLogger(Server.class.getName());
    
    private  Server() {
        
    }
    
    public static Server getInstance(){
        return instance;
    }
    
    public void loop()throws IOException {
        throw new IOException("serios error");
    }

}