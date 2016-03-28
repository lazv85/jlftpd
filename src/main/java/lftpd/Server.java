package lftpd;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

public class Server {
    
    private static Server instance = new Server();
    private Logger log = Logger.getLogger(Server.class.getName());
    private Config cfg = Config.getInstance();
    private boolean configLoaded = false;
    
    private  Server() {
     
    }
    
    public static Server getInstance(){
        return instance;
    }
    
    public void loadConfiguration(String configFile){
        try{
            cfg.parseFile(configFile);
            log.log(Level.INFO, cfg.getValue("version"));
            configLoaded = true;
        }catch(IOException e){
            log.log(Level.SEVERE, "loadConfiguration failed, cannot load server configuration from " + configFile);
        }
    }

    private void sayHello(PrintWriter pw) throws IOException{
        pw.println("220 Hey there");
    }
    
    private void authorize(PrintWriter pw, BufferedReader br) throws IOException{
        
        ICommand usr = CommandFactory.getCommand(br.readLine());
        
        if(usr.getCommand().equals("USER")){
            pw.println("230 Login successful");
        }
        
    }
    
    private int getServerPort(){
        int portNumber ;
        
        try{
            portNumber = Integer.parseInt(cfg.getValue("port","system"));
        }catch(Exception e){
            portNumber = 21; //default port of ftp server
        }
        
        return portNumber;
    }
    
    private void processConnection(ServerSocket serverSocket) throws IOException{
        try (
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            sayHello(out);
            authorize(out, in);
        }
    }
    
    public void loop() throws IOException,RuntimeException {
        if(!configLoaded){
            log.log(Level.SEVERE,"Server configuration has not been loaded");
            throw new RuntimeException("Server configuration has not been loaded");
        }
        
        int portNumber = getServerPort();
        
        log.log(Level.INFO, "Starting lftpd server on " + String.valueOf(portNumber) + " port");
        
        try (
            ServerSocket serverSocket = new ServerSocket(portNumber);
        ){
            while(true){
                processConnection(serverSocket);
            }
        }
    }

}