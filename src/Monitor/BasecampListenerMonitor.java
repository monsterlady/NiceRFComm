package Monitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * Listens to the messages from the basecamp
 */
public class BasecampListenerMonitor implements Runnable {

    private MulticastSocket ms;

    public BasecampListenerMonitor(MulticastSocket socket) {
        ms = socket;
    }

    @Override
    public void run() {

        while (true) {
            // Receive message from the basecamp
            byte[] buf = new byte[52];

            DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
            try {
                ms.receive(msgPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Translate message to string and print
            String msg = new String(msgPacket.getData());
            System.out.println(msg);

            //get coordinates from message
//            String coordinates = msg.replaceFirst("From drone: ", "");
//            if (coordinates.contains(" ")) {
//                String[] lat_long = coordinates.split(" ");
//                double latitude = Double.parseDouble(lat_long[0]);
//                double longitude = Double.parseDouble(lat_long[1]);
//
//                //set the coordinates and update the view of the map application
////                Main.instance.update(latitude, longitude);
//            }
        }
    }
}
