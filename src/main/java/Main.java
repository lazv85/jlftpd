import lftpd.Server;
import java.util.logging.Logger;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Server srv = Server.getInstance();
        try{
            srv.loop();
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        
    }

}