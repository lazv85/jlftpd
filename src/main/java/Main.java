import lftpd.Server;
import java.util.logging.Logger;
import java.io.IOException;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        Server srv = Server.getInstance();
        try{

            srv.loadConfiguration("config_test.ini");
            
            srv.loop();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
    }

}