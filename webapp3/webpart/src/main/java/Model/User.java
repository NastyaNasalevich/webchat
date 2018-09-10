package Model;

import javax.websocket.Session;

public class User {

    private String name;
    private Session session;
    private Session interlocutor;
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getInterlocutor() {
        return interlocutor;
    }

    public void setInterlocutor(String userName, Session interlocutor) {
        this.interlocutor = interlocutor;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}