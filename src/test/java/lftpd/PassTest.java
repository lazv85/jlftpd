package lftpd;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.io.File;
import org.junit.Test;

public class PassTest{
    File file = new File("src/test/resources/config_test.ini");
    Config configIntance = Config.getInstance();
    
    public PassTest() throws IOException{
        String absolutePath = file.getAbsolutePath();
        configIntance.parseFile(absolutePath); 
    }
    
    @Test
    public void TestPass() throws IOException{
        SessionState session = new SessionState("anon", "1:/abc:rw", "127.0.0.1", "127.0.0.1");
        User usr;
        
        usr = new User("USER","anon",session);
        
        assertEquals(usr.getResponse(), "230 Login successful");
        
        usr = new User("USER","john",session);
        assertEquals(usr.getResponse(), "331 user name is correct, password is required for john");
    }
}