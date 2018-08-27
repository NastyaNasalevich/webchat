import javax.websocket.Session;

public class Agent extends ChatUser {

    private boolean available;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    private Session interlocutor;

    public Session getInterlocutor() {
        return interlocutor;
    }

    public void setInterlocutor(String userName, Session interlocutor) {
        this.interlocutor = interlocutor;
    }
}
