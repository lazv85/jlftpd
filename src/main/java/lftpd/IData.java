package lftpd;
import java.net.Socket;
import java.io.IOException;

public interface IData{
    public String transferStatus();
    public void transferData(Socket sock) throws IOException;
}