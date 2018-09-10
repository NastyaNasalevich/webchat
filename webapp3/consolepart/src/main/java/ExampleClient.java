import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;

public class ExampleClient extends WebSocketClient {

    private static final Logger log = Logger.getLogger(ExampleClient.class);
    private static ConsoleUtils consoleClientUtil = new ConsoleUtils();

    public ExampleClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("New console user was connected");
    }
    @Override
    public void onMessage(String message) {
        JSONObject jsonObject = new JSONObject(message);
        System.out.println(jsonObject.getString("sender") + ":" + jsonObject.getString("content"));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("Closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        log.error("An error occurred:" + ex);
    }

    public static void main(String[] args) {
        try {
            WebSocketClient exemplar = consoleClientUtil.registerUser();
            consoleClientUtil.sendMessage(exemplar);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}