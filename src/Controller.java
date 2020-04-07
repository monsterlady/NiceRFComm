import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Controller implements Runnable, SerialPortEventListener {
    //Address for receiving messages
    private final static String INET_ADDR = "224.0.0.3";
    //Port for receiving messages
    private final static int PORT = 8888;

    private InetAddress ia;

    private DatagramSocket serverSocket;

    static CommPortIdentifier portId;
    static Enumeration portList;
    static String com = "COM3";
    static DataProvider dataProvider;

    Thread controllerThread;
    OutputStream outputStream;
    InputStream inputStream;
    SerialPort serialPort;
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */

    public Controller(){

        try{
            //Socket for sending messages to the basecamp
            //Socket for receiving messages from the basecamp
            ia = InetAddress.getByName(INET_ADDR);
            MulticastSocket ms = new MulticastSocket(PORT);
            //Subscribe to the address in order to receive all messages that were sent to that address
            ms.joinGroup(ia);
            serverSocket = new DatagramSocket();
            dataProvider = new DataProvider();
            // Open the port one time, then init your settings
            serialPort = (SerialPort)portId.open("TEAM3", 2000);
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            // Get the input/output streams for use in the application
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
            // Finally, add event listener
            serialPort.addEventListener(this::serialEvent);
            //
            serialPort.notifyOnDataAvailable(true);
        }catch (Exception e){
            System.out.println(e);
        }

        controllerThread = new Thread(this);
        controllerThread.start();
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] readBuffer = new byte[62];
                try {
                    while (inputStream.available() > 0) {
                        int numBytes = inputStream.read(readBuffer);
                        System.out.print(new String(readBuffer,0,numBytes));

                        //Wrap the message into packet
                        DatagramPacket msgPacket =
                                new DatagramPacket(readBuffer, readBuffer.length,ia,PORT);
                        try {
                            //Broadcast the packet to the basecamp
                            serverSocket.send(msgPacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {System.out.println(e);}
                break;
        }

    }

    public static void main(String[] args) throws IOException {
        portList = CommPortIdentifier.getPortIdentifiers();
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println(portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(com)) {
                    System.out.println("Controller COM found :" + com);
                    Controller drone = new Controller();
                    System.out.println("Starting to work");
                    while(true){
                        Scanner scanner = new Scanner(System.in);
                        String hint = "Msg is able to send: ";
                        System.out.println(hint);
                        String input = scanner.nextLine();
                        String command = dataProvider.filterCommand(input);
                        if(!command.equals("MSG WRONG")){
                            drone.outputStream.write(command.getBytes());
                            System.out.println("Command has been sent. \nWaiting for 99s...");
                            //1%duty
                            try { TimeUnit.SECONDS.sleep(99); ;
                            } catch (InterruptedException ie){}
                        } else {
                            System.out.println("Msg can not send");
                        }
                    }
                }
            }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {System.out.println(e);
    }

}
}
