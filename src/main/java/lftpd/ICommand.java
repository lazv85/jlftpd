package lftpd;

public interface ICommand{
    public String getCommand();
    public String getParameter();
    public String getResponse();
    public CommandStatus getStatus();
    public SessionState getSessionState();
}