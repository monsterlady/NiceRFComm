import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Scanner;

public class SimpleWrite {
    static Enumeration portList;
    static CommPortIdentifier portId;
    static String messageString = "";
    static SerialPort serialPort;
    static OutputStream outputStream;

    public static void main(String[] args) throws IOException{
        portList = CommPortIdentifier.getPortIdentifiers();
        int i;
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println(portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals("COM3")) {
                    System.out.println("Ready to send msg...");
                    // if (portId.getName().equals("/dev/term/a")) {
                    try {
                        serialPort = (SerialPort)
                                portId.open("TEAM3",2000);
                    } catch (PortInUseException e) {System.out.println(e);}
                    try {
                        outputStream = serialPort.getOutputStream();
                    } catch (IOException e) {System.out.println(e);}
                    try {
                        serialPort.setSerialPortParams(9600,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {System.out.println(e);}
                   while(true){
                       Scanner scanner = new Scanner(System.in);
                       String hint = "Msg to send: ";
                       System.out.println(hint);
                       String input = scanner.nextLine();
                       outputStream.write(input.getBytes());
                       System.out.println("Msg " + input + " has been sent.");
                   }
                }
            }
        }
    }
}

