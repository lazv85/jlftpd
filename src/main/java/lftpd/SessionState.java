 package lftpd;
 
 import java.util.regex.Pattern;
 import java.util.regex.Matcher;
 
 public class SessionState{
        private String userName;
        private String userSettings;
        private String currentDir;
        private String rootDir;
        private String permissions;
        private String localAddress;
        private String remoteAddress;
        private String typeFile;
        
        public SessionState(String userName,String userSettings, String localAddress, String remoteAddress){
            this.userName = userName;
            this.userSettings = userSettings;
            this.localAddress = localAddress;
            this.remoteAddress = remoteAddress;
            
            Pattern ptr = Pattern.compile("([^:]+):([^:]+):([^:]+)");
            
            Matcher m = ptr.matcher(userSettings);
        
            if(m.find()){
                currentDir = m.group(2);
                rootDir = m.group(2) ;
                permissions = m.group(3);
            }else{
                currentDir = "/";
                rootDir = "/";
                permissions = "r";
            }
        }
        
        public SessionState(){
            
        }
        
        public String getUserName(){
            return userName;
        }
        
        public String getUserSettings(){
            return userSettings;
        }
        
        public String getCurrentDir(){
            return currentDir;
        }
        
        public void setCurrentDir(String currentDir){
            this.currentDir = currentDir;
        }
        
        public String getLocalAddress(){
            return this.localAddress;
        }
        
        public String getRemoteAddress(){
            return this.remoteAddress;
        }
        
        public void setTypeFile(String typeFile){
            this.typeFile = typeFile;
        }
        
        public String getTypeFile(){
            return this.typeFile;
        }
        
        public String getRootDir(){
            return this.rootDir;
        }
}