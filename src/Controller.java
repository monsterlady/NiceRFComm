import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Scanner;

public class Controller implements Runnable, SerialPortEventListener {
    static CommPortIdentifier portId;
    static Enumeration portList;
    static String com = "COM3";

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
                    System.out.println("Drone COM found :" + com);
                    Controller drone = new Controller();
                    System.out.println("Starting to work");
                    while(true){
                        Scanner scanner = new Scanner(System.in);
                        String hint = "Msg to send: ";
                        System.out.println(hint);
                        String input = scanner.nextLine();
                        drone.outputStream.write(input.getBytes());
                        System.out.println("Msg " + input + " has been sent.");
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
