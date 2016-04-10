package lftpd;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.io.File;
import org.junit.Test;

public class CommandTest{
    File file = new File("src/test/resources/config_test.ini");
    Config configIntance = Config.getInstance();
    
    public CommandTest() throws IOException{
        String absolutePath = file.getAbsolutePath();
        configIntance.parseFile(absolutePath); 
    }
    
    @Test
    public void testUser() {
            User usr;
            
            usr = new User("USER","anon",null);
            assertEquals(usr.getResponse(), "230 Login successful");
            
            usr = new User("USER","anon anon",null);
            assertEquals(usr.getResponse(), "230 Login successful");
            
            usr = new User("USER","john",null);
            assertEquals(usr.getResponse(), "331 user name is correct, password is required for john");
    }
    
    @Test
    public void testPass() {
            Pass pass;
            
            pass = new Pass("PASS","123",null);
            pass.authorize("john");
            assertEquals(pass.getResponse(), "430 Invalid user name or password");
            
            pass = new Pass("PASS","123456",null);
            pass.authorize("john1");
            assertEquals(pass.getResponse(), "430 Invalid user name or password");
            
            pass = new Pass("PASS","123456",null);
            pass.authorize("john");
            assertEquals(pass.getResponse(), "230 Login successful");
            
    }
    
    @Test 
    public void testPwd(){
        SessionState session = new SessionState("anon", "1:/:rw","127.0.0.1","127.0.0.1");
        Pwd pwd;
        pwd = new Pwd("PWD",null,session);
        assertEquals(pwd.getResponse(), "257 \"/\"");
    }
    
    @Test
    public void testSyst(){
        Syst syst ;
        syst = new Syst("SYST", null,null);
        assertEquals(syst.getResponse().substring(0,3),"215");
    }
    
    @Test
    public void testCwd(){
        Cwd cwd;
        SessionState session = new SessionState("anon", "1:/:rw","127.0.0.1","127.0.0.1");
        
        cwd = new Cwd("CWD","..",session);
        assertEquals(cwd.getResponse(),"250 Directory successfully changed");
        
        cwd = new Cwd("CWD",null,session);
        assertEquals(cwd.getResponse(),"550 failed to change directory");
        
        cwd = new Cwd("CWD","tmp1",session);
        assertEquals(cwd.getResponse(),"550 failed to change directory");
        
        cwd = new Cwd("CWD","tmp",session);
        assertEquals(cwd.getResponse(),"250 Directory successfully changed");
        
    }
    
    @Test
    public void testType(){
        Type type ;
        SessionState session ;
        session = new SessionState("anon", "1:/:rw","127.0.0.1","127.0.0.1");

        type = new Type("TYPE", "I",session);
        assertEquals(type.getResponse(),"200 TYPE set to I");

        type = new Type("TYPE", "A",session);
        assertEquals(type.getResponse(),"200 TYPE set to A");

        type = new Type("TYPE", null,session);
        assertEquals(type.getResponse(),"501 Unsupported parameters for Type command");
        
        type = new Type("TYPE", "X",session);
        assertEquals(type.getResponse(),"501 Unsupported parameters for Type command");
    }
    
    @Test
    public void testSize(){
        Size size;
        SessionState session ;
        session = new SessionState("anon", "1:/:rw","127.0.0.1","127.0.0.1");
        
        size = new Size("SIZE","/",session);
        assertEquals(size.getResponse(),"213 4096");
        
        size = new Size("SIZE","xyz",session);
        assertEquals(size.getResponse(),"550 file: xyz not found");
        
        size = new Size("SIZE",null,session);
        assertEquals(size.getResponse(),"213 4096");

    }
}