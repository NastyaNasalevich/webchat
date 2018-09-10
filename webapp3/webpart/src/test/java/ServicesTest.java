import Model.User;
import org.junit.Before;
import org.junit.Test;

import javax.websocket.Session;
import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ServicesTest {
    private Services services;

    HashMap<String, Services> users;

    @Before
    public void setUp() {
        services = mock(Services.class);
        users = mock(HashMap.class);
    }

    @Test
    public void sendMessage() throws Exception {
        User user = mock(User.class);
        Message message = mock(Message.class);
        Session session = mock(Session.class);
        user.setSession(session);
        services.sendMessage(message,user);
        verify(services).sendMessage(message,user);
    }
}
