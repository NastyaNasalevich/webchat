import org.java_websocket.client.WebSocketClient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ConsoleUtilsTest {

    private ConsoleUtils consoleUtils;

    @Before
    public void setUp() {
        consoleUtils = mock(ConsoleUtils.class);
    }

    @Test
    public void registerClient() throws Exception {
        WebSocketClient webSocketClient = mock(WebSocketClient.class);
        when(consoleUtils.registerUser()).thenReturn(webSocketClient);
        WebSocketClient webSocketClient1 = consoleUtils.registerUser();
        assertEquals(webSocketClient, webSocketClient1);
    }

    @Test
    public void messageSending() throws Exception {
        WebSocketClient webSocketClient = mock(WebSocketClient.class);
        consoleUtils.sendMessage(webSocketClient);
        verify(webSocketClient, never()).close();
    }
}
