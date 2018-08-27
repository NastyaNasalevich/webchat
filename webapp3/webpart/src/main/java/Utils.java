import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;

public class Utils {

    private HashMap<String, Agent> agents;
    private HashMap<String, Client> clients;

    public Utils(HashMap<String, Agent> agents, HashMap<String, Client> clients) {
        this.agents = agents;
        this.clients = clients;
    }

    public ChatUser registreChatUser(String name, String role, Session session) {

        ChatUser chatUser;

        switch (role) {
            case "client":
                Client client = new Client();
                client.setRole("client");
                chatUser = client;
                break;
            default:
                Agent agent = new Agent();
                agent.setRole("agent");
                agent.setAvailable(true);
                chatUser = agent;
                break;
        }

        chatUser.setName(name);
        chatUser.setSession(session);

        return chatUser;
    }

    private Agent findAvailableAgent(HashMap<String, Agent> agents) {
        for (Agent agent : agents.values()) {
            if (agent.isAvailable())
                return agent;
        }
        return null;
    }

    public boolean tryAssignAgent(HashMap<String, Agent> agents, ChatUser chatUser) throws IOException, EncodeException {

        Agent agent = findAvailableAgent(agents);
        if (agent != null) {
            agent.setAvailable(false);
            //agent.setUserToSession(chatUser.getSession());
            agent.setInterlocutor(chatUser.getName(),chatUser.getSession());
            chatUser.setUserToSession(agent.getSession());
            Message message = new Message();
            message.setFrom(chatUser.getName());
            message.setContent("connected to the chat");
            sendMessage(message, agent);
            message.setFrom(" ");
            message.setContent("connected");
            sendMessage(message, chatUser);
            return true;
        }
        return false;
    }

    public void sendMessage(Message message, ChatUser chatUser) throws IOException, EncodeException {
        chatUser.getSession().getBasicRemote().sendObject(message);
    }
}