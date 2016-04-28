package lftpd;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;

public class List extends Command implements ICommand, IData{
    
    public List(String cmd, String param, SessionState session){
        super(cmd, param, session);
        if(param != null){
            if(param.equals("-l")){
                this.param = null;
            }
        }
        responseCode = ResponseCode.CODE_150_File_status_okay;
    }
    
    @Override
    public String getResponse(){
        if(responseCode == ResponseCode.CODE_226_Closing_data_connection){
            return "226 Directory send OK";
        }else if(responseCode == ResponseCode.CODE_451_Requested_action_aborted) {
            return "451 File or Directory is not available.";
        }else if(responseCode == ResponseCode.CODE_426_Connection_closed_aborted){
            return "426 TCP connection has been broken.";
        }else if(responseCode == ResponseCode.CODE_150_File_status_okay){
             return "150 Here comes the directory listing";
        }
        return null;
    }
 
    private String getUnixFileItem(String fileName, String pathToFile) throws Exception{
        Path p = Paths.get(pathToFile + "/" + fileName).normalize();

        PosixFileAttributes attrs = Files.readAttributes(p,PosixFileAttributes.class);
        Set<PosixFilePermission> posixPermissions = Files.getPosixFilePermissions(p);

        String owner = attrs.owner().getName();
        String group = attrs.group().getName();
        String perms = PosixFilePermissions.toString(posixPermissions);

        File cf = new File(p.toString());
        perms = cf.isDirectory() ? ("d"+perms) : ("-"+perms);
                    
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm");
        String linkCount = Files.getAttribute(p, "unix:nlink").toString();
                    
        String item ;
                    
        item = perms + " ";
        item += String.format("%4d", Integer.parseInt(linkCount)) + " ";
        item += owner + " ";
        item += group + " ";
        item += String.format("%12d", cf.length())  + " ";
        item += sdf.format(cf.lastModified()) + " ";
        item += fileName;
        
        return item;
    }
    
    private String getWindowsFileItem(String fileName, String pathToFile) throws Exception{
        Path p = Paths.get(pathToFile + "/" + fileName).normalize();
       
        File cf = new File(p.toString());
        String item ="";
       
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm"); 
        String linkCount = "1";
       
         
        item += cf.isDirectory() ? "d" : "-";
        item += cf.canRead() ? "r" : "-";
        item += cf.canWrite() ? "w" : "-";
        item += "x---- ";
       
        item += String.format("%4d", Integer.parseInt(linkCount)) + " ";
        item += "win win ";
        item += String.format("%12d", cf.length())  + " ";
        item += sdf.format(cf.lastModified()) + " ";
        item += fileName;
        
        return item;
    }
    
    private String getFileItem(String fileName, String pathToFile) throws Exception{
                    
        String item ;
        
        if (session.getSeparator().equals("/")){
           item =  getUnixFileItem(fileName, pathToFile);
        }else{
            item =  getWindowsFileItem(fileName, pathToFile);
        }
        
        return item;
    }   

    private String listFiles(){

        String str = "";
        try{
            Path p;
            
            if(param == null){
                p = Paths.get(session.getCurrentDir()).normalize();
            }else if(param.substring(0,1).equals("/")){
                p = Paths.get(session.getRootDir() + "/" + param).normalize();
            }else{
                p = Paths.get(session.getCurrentDir() +"/" + param).normalize();
            }
            
            File f = new File(p.toString());
            if(f.isDirectory()){
                String []content = f.list();
                for(String i : content){
                    str += getFileItem(i, p.toString()) + "\r\n" ;
                }
            }else{
                str = getFileItem(param,p.toString()) + "\r\n" ;
            }
        }catch(Exception e){
            responseCode = ResponseCode.CODE_451_Requested_action_aborted;
        }
        return str;
    }
    
    @Override
    public void transferData(Socket sock) throws IOException{
        try{
            PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
            
            String content = listFiles();
            
            pw.printf(content);
            content = null;
            responseCode = ResponseCode.CODE_226_Closing_data_connection;
        }catch(Exception e){
            responseCode = ResponseCode.CODE_426_Connection_closed_aborted;
        }
    }
   
}