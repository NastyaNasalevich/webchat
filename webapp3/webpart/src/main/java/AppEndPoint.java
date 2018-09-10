import Model.Agent;
import Model.Client;
import Model.User;
import org.apache.log4j.Logger;

import java.util.HashMap;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(
        value = "/{userrole}/chat/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class )
public class AppEndPoint {

    private static final Logger log = Logger.getLogger(AppEndPoint.class);
    private static HashMap<String, Agent> agents = new HashMap<>();
    private static HashMap<String, Client> clients = new HashMap<>();
    private Services services = new Services(agents, clients);
    private final String LEAVE = "/leave";
    private final String EXIT = "/exit";

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("userrole") String userrole) {
        User user;

        if (userrole.equals("client")) {
            Client client = new Client();
            client.setRole("client");
            user = client;
            user.setName(username);
            user.setSession(session);
            clients.put(session.getId(), (Client) user);
        } else {
            Agent agent = new Agent();
            agent.setRole("agent");
            agent.setAvailable(true);
            user = agent;
            user.setName(username);
            user.setSession(session);
            agents.put(session.getId(), (Agent) user);
        }

        Message message = new Message();
        message.setSender(username);
        message.setContent("Connected");
        services.sendMessage(message, user);
        log.info("New web user was connected");
    }

    @OnMessage
    public void onMessage(Session session, Message message){
        String id = session.getId();

        if (message.getContent().equals(LEAVE) && clients.containsKey(id)) {
            message.setSender("");
            message.setContent("You was disconnected!");
            services.sendMessage(message, clients.get(id));
            if (clients.get(id).getInterlocutor() != null) {
                services.disconnectUsers(clients.get(id));
            }
        }

        else if (message.getContent().equals(EXIT)) {
            message.setSender("");
            if (clients.containsKey(id)) {
                services.sendMessage(message, clients.get(id));
                services.disconnectUsers(clients.get(id));
            }
            else {
                services.sendMessage(message, agents.get(id));
                services.disconnectUsers(agents.get(id));
            }
        }

        else if (clients.containsKey(id)) {
            message.setSender(clients.get(id).getName());
            services.sendMessage(message, clients.get(id));
            if (clients.get(id).getInterlocutor() == null) {
                services.appointAgent(agents, clients.get(id));
            }
            if (clients.get(id).getInterlocutor() != null) {
                String idOfInterlocutor = clients.get(id).getInterlocutor().getId();
                services.sendMessage(message, agents.get(idOfInterlocutor));
            }
            else {
                message.setSender("");
                message.setContent("No free interlocutors");
                services.sendMessage(message, clients.get(id));
            }
        }

        else {
            message.setSender(agents.get(id).getName());
            services.sendMessage(message, agents.get(id));
            if (!agents.get(id).isAvailable()) {
                String idOfInterlocutor = agents.get(id).getInterlocutor().getId();
                services.sendMessage(message, clients.get(idOfInterlocutor));
            } else {
                message.setSender("");
                message.setContent("No free interlocutors");
                services.sendMessage(message, agents.get(id));
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        String id = session.getId();

        if (clients.containsKey(id)) {
            log.info(clients.get(id).getName() + " disconnected from chat");
            services.disconnectUsers(clients.get(id));
            clients.remove(id);
        }
        else {
            log.info(agents.get(id).getName() + " disconnected from chat");
            services.disconnectUsers(agents.get(id));
            agents.remove(id);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("error");
    }
}