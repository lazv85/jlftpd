package lftpd;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;

public interface INetwork {
    public Socket getSocket() throws UnknownHostException, IOException;
}