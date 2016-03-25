package lftpd;

public class Server {
    
    private static Server instance = new Server();
    
    private  Server() {
        
    }
    
    public static getInstance(){
        return instance;
    }

}