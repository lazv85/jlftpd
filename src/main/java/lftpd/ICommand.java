package lftpd;

public interface ICommand{
    public String getCommand();
    public String getParameter();
    public String getResponse();
    public SessionState getSessionState();
    public boolean isNetwork();
    public boolean isData();
    public int getResponseCode();
}