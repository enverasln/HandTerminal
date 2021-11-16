package tr.com.cetinkaya.handterminal.restapi;

public interface ResultListener {
    void result(String list);               // Server response
    void connectionLost(String list);       // Server response cancelled
    void loader(boolean visible);           // Loader visibility when server connection
}
