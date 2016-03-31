package lftpd;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
   
    public class ServerProcess implements Runnable{
        
        private Socket sock;
        
        public ServerProcess( Socket clientSocket){
            try{
                sock = clientSocket;
            }catch(Exception e){
                log.log(Level.INFO, "cannot open streams");
            }
        }
        
        @Override
        public void run() {
            try{
                SessionState session;
                
                sayHello(sock);
                session = authorize(sock);
                serveClient(sock, session);
            }catch(Exception e){
                log.log(Level.INFO, "thread completed");
            }
        }
    }
    
    private static Server instance = new Server();
    private Logger log = Logger.getLogger(Server.class.getName());
    private Config cfg = Config.getInstance();
    private boolean configLoaded = false;
    private ExecutorService executor = Executors.newCachedThreadPool();
    
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

    private void sayHello(Socket clientSocket) throws IOException{

        PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        pw.println("220 Hey there");

    }
    
    private SessionState authorize(Socket clientSocket) throws IOException{
        
        boolean authorized = false;
        SessionState session = new SessionState();
        
        
        PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        do{
            String remoteAddress = clientSocket.getInetAddress().getHostAddress();
            String localAddress = clientSocket.getLocalAddress().getHostAddress(); 
            
            ICommand usr = CommandFactory.getCommand(br.readLine());
        
            pw.println(usr.getResponse());
            
            if(usr.getStatus() == CommandStatus.CMD_OK && usr.getCommand().equals("USER")){
                session = new SessionState(usr.getParameter(), cfg.getValue("anonymous_dir","system"), localAddress, remoteAddress);
                authorized = true;
            }else if(usr.getStatus() != CommandStatus.CMD_OK && usr.getCommand().equals("USER")){
                ICommand pass = CommandFactory.getCommand(br.readLine());
                
                if(pass.getCommand().equals("PASS")){
                    ((Pass)pass).authorize(usr.getParameter());
                    if(pass.getStatus() == CommandStatus.CMD_OK){
                        session = new SessionState(usr.getParameter(), cfg.getValue(usr.getParameter(),"users"), localAddress, remoteAddress);
                        authorized = true;
                    }
                }
                pw.println(pass.getResponse());
                pass = null;
            }
            usr = null;
        }while(!authorized);
        
        return session;
    }
    
    private void serveClient(Socket clientSocket, SessionState session) throws IOException{
        ICommand cmd;

        PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        Socket sock = null;
        
        do{
            cmd = CommandFactory.getCommand(br.readLine(), session);
            
            if(cmd.isNetwork() && sock!= null){
                sock.close();
                sock = null;
            }
            
            if(cmd.isNetwork()){
                sock = ((INetwork)cmd).getSocket();
                pw.println(cmd.getResponse());
            }else{
                pw.println(cmd.getResponse());
                if(sock != null && cmd.isData()){
                    ((IData)cmd).transferData(sock);
                    sock.close();
                    sock = null;
                }else if(sock != null){
                    sock.close();
                    sock = null;
                }
            }
            
            session = cmd.getSessionState();
            
        }while(!cmd.getCommand().equals("QUIT"));
        
    }
     
    private int getServerPort(){
        int portNumber ;
        
        try{
            portNumber = Integer.parseInt(cfg.getValue("port","system"));
        }catch(Exception e){
            portNumber = 21;
        }
        
        return portNumber;
    }
    
    private void processConnection(ServerSocket serverSocket) throws IOException{
            Socket clientSocket = serverSocket.accept();
            Runnable process = new ServerProcess( clientSocket);
            executor.execute(process);
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