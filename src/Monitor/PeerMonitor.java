package Monitor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Imitates monitor node in the basecamp. Listens to the messages from basecamp.
 */
public class PeerMonitor {

    //Address for receiving messages
    private final static String INET_ADDR = "224.0.0.3";
    //Port for receiving messages
    private final static int PORT = 8888;

    public static void main(String[] args)
            throws IOException {

        //Socket for receiving messages from the basecamp
        InetAddress ia = InetAddress.getByName(INET_ADDR);
        MulticastSocket ms = new MulticastSocket(PORT);
        //Subscribe to the address in order to receive all messages that were sent to that address
        ms.joinGroup(ia);

        //Initialize basecamp listener
        BasecampListenerMonitor basecampListener = new BasecampListenerMonitor(ms);

        Thread basecampList = new Thread(basecampListener);
        //Start basecamp listener
        basecampList.start();

        //Start map application
//        Application.launch(Main.class, args);
    }
}
