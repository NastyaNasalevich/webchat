import java.io.IOException;
import java.util.HashMap;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/{userrole}/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndPoint {

    private static HashMap<String, Agent> agents = new HashMap<>();
    private static HashMap<String, Client> clients = new HashMap<>();
    private Utils utils = new Utils(agents, clients);

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("userrole") String userrole) throws IOException, EncodeException {
        ChatUser chatUser;
        switch (userrole) {
            case "client":
                chatUser = utils.registreChatUser(username, userrole, session);
                clients.put(session.getId(), (Client) chatUser);
                break;
            default:
                chatUser = utils.registreChatUser(username, userrole, session);
                agents.put(session.getId(), (Agent) chatUser);
                break;
        }

        Message message = new Message();
        message.setFrom(username);
        message.setContent("Connected!");
        utils.sendMessage(message, chatUser);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        if (clients.containsKey(session.getId())) {
            message.setFrom(clients.get(session.getId()).getName());
            utils.sendMessage(message, clients.get(session.getId()));
            if (clients.get(session.getId()).getUserToSession() == null) {
                utils.tryAssignAgent(agents, clients.get(session.getId()));
            }
            if (clients.get(session.getId()).getUserToSession() != null)
                utils.sendMessage(message, agents.get(clients.get(session.getId()).getUserToSession().getId()));
            else {
                message.setFrom("");
                message.setContent("НЕТ СОБЕСЕДНИКА");
                utils.sendMessage(message, clients.get(session.getId()));
            }
        } else {
            message.setFrom(agents.get(session.getId()).getName());
            if (!agents.get(session.getId()).isAvailable()) {
                String id = agents.get(session.getId()).getInterlocutor().getId();
                utils.sendMessage(message, clients.get(id));
            } else {
                message.setFrom("");
                message.setContent("НЕТ СОБЕСЕДНИКА");
                utils.sendMessage(message, agents.get(session.getId()));
            }
        }
    }
}