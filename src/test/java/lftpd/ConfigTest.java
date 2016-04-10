package lftpd;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.io.File;

import org.junit.Test;
import java.lang.reflect.*;

public class ConfigTest{
    Config configIntance = Config.getInstance();
    
    // MyClass myClass = new MyClass();
    // Method method = MyClass.class.getDeclaredMethod("myMethod", String.class);
    // method.setAccessible(true);
    // String output = (String) method.invoke(myClass, "some input");
    
    @Test
    public void testClearComment(){
        try{
            Method  method = Config.class.getDeclaredMethod("clearComment",String.class);
            method.setAccessible(true);
            String output ;
            
            output = (String)method.invoke(configIntance, "# abc");
            assertEquals("",output);     
            
            output = (String)method.invoke(configIntance, "abc #cdb");
            assertEquals("abc ",output);
            
            output = (String)method.invoke(configIntance, "abc# cdb #edf");
            assertEquals("abc",output);
            
            output = (String)method.invoke(configIntance, "abc cdb edf");
            assertEquals("abc cdb edf",output);
            
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            throw new RuntimeException(e);
        }
    }
    
    @Test
    public void testGetSection(){
        try{
            Method  method = Config.class.getDeclaredMethod("getSection",String.class,String.class);
            method.setAccessible(true);
            String output ;
            
            output = (String)method.invoke(configIntance, "[section]","default");
            assertEquals("section",output);     
            
            output = (String)method.invoke(configIntance, "abc","default");
            assertEquals("default",output);
            
            output = (String)method.invoke(configIntance, "abc [section] abc", "default");
            assertEquals("section",output);
            
            output = (String)method.invoke(configIntance, "","default");
            assertEquals("default",output);
            
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            throw new RuntimeException(e);
        }
    }
    
    @Test
    public void testParseLine(){
        try{
            Method  method = Config.class.getDeclaredMethod("parseLine",String.class);
            method.setAccessible(true);
            
            String [] output;
            output = (String[])method.invoke(configIntance, "abc=bcd");
            assertEquals("abc",output[0]);     
            assertEquals("bcd",output[1]);   
            
            output = (String[])method.invoke(configIntance, " abc   =  bcd  ");
            assertEquals("abc",output[0]);     
            assertEquals("bcd",output[1]);
            
            output = (String[])method.invoke(configIntance, "");
            assertNull(output[0]);     
            assertNull(output[1]);
            
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            throw new RuntimeException(e);
        }
    }
    
    @Test
    public void testParseFile(){
        try{
            File file = new File("src/test/resources/config_test.ini");
            String absolutePath = file.getAbsolutePath();

            configIntance.parseFile(absolutePath);
            assertEquals("yes",configIntance.getValue("anonymous_access","system"));
            assertEquals("1:/home/ubuntu/workspace:rw",configIntance.getValue("anonymous_dir","system"));
            assertEquals("123456:/home/ubuntu/workspace:rw",configIntance.getValue("john","users"));
            assertEquals("1234567:/home/ubuntu/workspace/src:rw",configIntance.getValue("bob","users"));
            assertNull(configIntance.getValue("bob","users1"));

        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
    
}