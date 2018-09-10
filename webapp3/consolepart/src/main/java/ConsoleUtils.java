import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class ConsoleUtils {

    private static final Logger log = Logger.getLogger(ConsoleUtils.class);

    public WebSocketClient registerUser() throws IOException {
        WebSocketClient exemplar = null;
        String role = null;
        System.out.println("Enter your name");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String user = in.readLine();

        while (!"agent".equals(role) && !"client".equals(role)) {
            System.out.println("Enter your role(only client or agent)");
            role = in.readLine();
        }

        try {
            exemplar = new ExampleClient(new URI("ws://localhost:8080/" + role + "/chat/" + user + "/"));
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
        }

        exemplar.connect();
        return exemplar;
    }

    public void sendMessage(WebSocketClient exemplar) throws IOException{
        String message;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            message = in.readLine();
            if (message.equals("/exit")) {
                exemplar.close();
                break;
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("content", message);
            exemplar.send(jsonObject.toString());
        }
    }
}
