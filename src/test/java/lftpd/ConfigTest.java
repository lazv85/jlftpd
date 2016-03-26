package lftpd;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
            
        }catch(NoSuchMethodException e){
            throw new RuntimeException(e);
        }catch( IllegalAccessException e){
            throw new RuntimeException(e);
        }catch(InvocationTargetException e){
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
            
        }catch(NoSuchMethodException e){
            throw new RuntimeException(e);
        }catch( IllegalAccessException e){
            throw new RuntimeException(e);
        }catch(InvocationTargetException e){
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
            
        }catch(NoSuchMethodException e){
            throw new RuntimeException(e);
        }catch( IllegalAccessException e){
            throw new RuntimeException(e);
        }catch(InvocationTargetException e){
            throw new RuntimeException(e);
        }
    }
    
}