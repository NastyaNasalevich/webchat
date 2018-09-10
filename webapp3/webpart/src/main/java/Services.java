import Model.Agent;
import Model.Client;
import Model.User;
import org.apache.log4j.Logger;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.HashMap;

public class Services {

    private static final Logger log = Logger.getLogger(Services.class);
    private HashMap<String, Agent> agents;
    private HashMap<String, Client> clients;

    public Services(HashMap<String, Agent> agents, HashMap<String, Client> clients) {
        this.agents = agents;
        this.clients = clients;
    }

    public void sendMessage(Message message, User addressee){
        try {
            addressee.getSession().getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            log.error(e.getMessage());
        }
    }

    private Agent findAvailableAgent(HashMap<String, Agent> agents) {
        for (Agent agent : agents.values()) {
            if (agent.isAvailable())
                return agent;
        }
        return null;
    }

    public boolean appointAgent(HashMap<String, Agent> agents, Client client) {

        Agent agent = findAvailableAgent(agents);
        if (agent != null) {
            agent.setAvailable(false);
            agent.setInterlocutor(client.getName(),client.getSession());
            client.setInterlocutor(agent.getName(), agent.getSession());

            Message message = new Message();
            message.setSender(client.getName());
            message.setContent("Connected to the chat");
            sendMessage(message, agent);
            message.setSender(agent.getName());
            message.setContent("You can ask your question");
            sendMessage(message, client);
            return true;
        }
        return false;
    }

    public void disconnectUsers(User user)  {
        Message message = new Message();
        message.setSender(user.getName());
        message.setContent("Interlocutor was disconnected!");
        if (user.getInterlocutor() != null) {
            if ("agent".equals(user.getRole())) {
                sendMessage(message, clients.get(user.getInterlocutor().getId()));
                clients.get(user.getInterlocutor().getId()).setInterlocutor(null, null);
            } else {
                String agentId = user.getInterlocutor().getId();
                sendMessage(message, agents.get(agentId));
                agents.get(agentId).setInterlocutor(null, null);
                agents.get(agentId).setAvailable(true);
            }
        }
        user.setInterlocutor(null, null);
    }
}