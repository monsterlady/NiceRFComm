# NiceRF LoRa 611AES Communication 

## Set up LP2303 USB Cable

First we need to download the drive for USB cable 

[PL2303(10/27/2008)](https://drive.google.com/open?id=1M2Ghhlmp14jnRkRSPhWgqDUL2pH-cWbH)

After installed the drive, run "***devmgmt. msc***" in CMD, find the COM that your NiceRf connected to.

![In this case, *COM3* is the port NiceRf using](https://i.loli.net/2020/03/05/8VUJSGdoD9pwf5t.png)

***Don't continue if you can't find the COM niceRF connected to.***

## Set up Java SDK
 In order to avoid the conflict between 64 bits system and 32 bits compiler, Java 1.8 32 bits SDK recommended here.
 
 [Java 1.8 32bits SDK](https://drive.google.com/open?id=1SgkkbfbyQfqYOayK5sad947gG1jacfUn)
 
 *Please skip this step if you are using Java sdk 1.8*
 
 Since we are going to read Data from and write Data to Serial Port devices like : GSM modem, RFID Reader and some other devices that specifically made to Serial ports.
 So in that case we need to install Java communication API(Javax.comm package)
 
 [Javax.comm](https://drive.google.com/open?id=1ZlIsZ9v4s5ZUW43unl1OfAx-s8cpCTLc)
 
 Once you download this File you will get a rar File named "javax.comm.zip". 
 
 Unzip it to certain folder
 
 We will need to copy these files in ur folder.
 
 ![](https://i.loli.net/2020/03/05/apBhGEbyl3PjQgA.png)
 
 Go to the SDK downloaded before, the default path would be 
 
 ***C:\Program Files (x86)\Java\jdk1.8.0_241\jre***
 
 ### win32com.dll
 
 Make sure that ***win32com.dll*** (comes with javax.com) is in the ***- jre\bin*** directory.
 
 ![1.png](https://i.loli.net/2020/03/05/XjgdJBkl7M1wKbs.png)
 
 
 ### java.comm.properties
 
Make sure that ***java.comm.properties*** (comes with javax.com) is in the ***- jre/lib*** directory.

![2.png](https://i.loli.net/2020/03/05/IV4qPxXzH2Wr7sv.png)

### comm.jar

Make sure that ***comm.jar*** (comes with javax.com) is in the ***- jre/lib/ext*** directory.

![3.png](https://i.loli.net/2020/03/05/C7lTHgrxhb9R8ji.png)

---

# Project configuration

Clone project from *https://github.com/monsterlady/NiceRFComm.git*

Import javax.comm from the folder which you unzip the *Javax.comm.zip* to libraries in **Project Structure**

![4.png](https://i.loli.net/2020/03/05/bKlq7fRHZQOegmV.png)

Move to ***SimpleRead.java*** and ***SimpleWrite.java***, modify the COM to your NiceRF connect with.

You have to work with another NiceRF to test if it works

    //In SimpleRead
    ...
    if (portId.getName().equals("COM7"))
     ...
    //replace COM7 with COM3 in my case
    
    //The configuration of NiceRf
    //Baud rate=9600 bps; Data bit=8 bits Stop bit:1 Parity bit: none
    ...
    serialPort.setSerialPortParams(9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
    ...

Note: *Check if the band, channel, rate, NET ID has set to the same, only in this way module can communicate properly*

***Congratulations! You have now successfully set up your own NiceRf!***
