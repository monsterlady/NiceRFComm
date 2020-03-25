import javax.comm.CommPortIdentifier;
import javax.comm.SerialPortEvent;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Drone extends Controller {
    private static String com = "COM3";
    private static String tempCommand = "&^%#";

    public static void main(String[] args) throws IOException {
        portList = CommPortIdentifier.getPortIdentifiers();
        portId = (CommPortIdentifier) portList.nextElement();
        System.out.println(portId.getName());
        if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            if (portId.getName().equals(com)) {
                System.out.println("Drone COM found :" + com);
                Drone drone = new Drone();
                System.out.println("Starting to work");
                while(true){
                    if(!tempCommand.isEmpty()){
                        String measurment = dataProvider.probType(tempCommand);
                        measurment = DataProvider.encrypt(measurment);
                        drone.outputStream.write(measurment.getBytes());
                        System.out.println("Command has been sent.\nWaiting for 99s...");
                        //1%duty
                        try { TimeUnit.SECONDS.sleep(99); ;
                        } catch (InterruptedException ie){}
                        tempCommand = "";
                    } else {
                        System.out.println("Waiting for command");
                        try { TimeUnit.SECONDS.sleep(10); ;
                        } catch (InterruptedException ie){}
                    }
                }
            }
        }
    }
    public Drone() {
        super();
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] readBuffer = new byte[62];
                try {
                    while (inputStream.available() > 0) {
                        int numBytes = inputStream.read(readBuffer);
                        String command = new String(readBuffer,0,numBytes);
                        //Only for test
                        System.out.print(command);
                        DataProvider dataProvider = new DataProvider();
                        tempCommand = dataProvider.convertCommand(command);
                    }
                } catch (IOException e) {System.out.println(e);}
                break;
        }
    }

    @Override
    public void run() {
        super.run();
    }
}
