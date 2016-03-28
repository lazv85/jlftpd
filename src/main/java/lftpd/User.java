package lftpd;

public class User extends Command implements ICommand{
    public User(String cmd, String param){
        super(cmd, param);
    }
}