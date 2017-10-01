package server;

import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hdsingh2015 on 26-07-2017.
 */
public class MyServerThread extends Thread{
    private ServerController serverController;
    private Socket socket;
    private ServerSocket serverSocket;
    private Map<Integer,Socket> socketsMap = new ConcurrentHashMap<>();
    private Map<Integer,String> nameMap = new ConcurrentHashMap<>();
    private ObjectOutputStream objectOutputStream;
    private int count=-1;
    private MyServerSuperClass myServerSuperClass;
    private Integer lastClosedIndex = null;
    public MyServerThread(ServerController serverController) {
        super("MyServerThread");
        this.serverController = serverController;
    }

    public MyServerSuperClass getMyServerSuperClass() {
        return myServerSuperClass;
    }

    public ServerController getServerController() {
        return serverController;
    }

    public Map<Integer, Socket> getSocketsMap() {
//        return socketsMap;
        return new ConcurrentHashMap<>(socketsMap);
    }

    public Socket getSocket() {
        return socket;
    }

    public Map<Integer, String> getNameMap() {
//        return nameMap;
        return new ConcurrentHashMap<>(nameMap);
    }

    @Override
    public void run() {
        try {
//            InetAddress inetAddress = InetAddress.getByName("46.155.17.100");
//            serverSocket = new ServerSocket(2000,20,inetAddress);
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("192.168.1.20",3333));
            boolean flag = true;
            while(true)
            {
                socket = serverSocket.accept();
                Platform.runLater(() -> serverController.addToOberservableList(socket));
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                count++;
                System.out.println("count: " + count);
                giveMapAndIndexToClient();
                DataInputStream d = new DataInputStream(socket.getInputStream());
                if(d.readBoolean()) {
                    nameMap.put(count, d.readUTF());
                }
                socketsMap.put(count,socket);
                System.out.println("socketsMap: " + socketsMap);
                System.out.println("nameMap: " + nameMap);
                MyClientServerHandlingThread myClientServerHandlingThread = new MyClientServerHandlingThread(this,count);
                myClientServerHandlingThread.start();

                if(flag)
                {

                    myServerSuperClass = new MyServerSuperClass(this);
                    myServerSuperClass.start();
                }
                flag=false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void giveMapAndIndexToClient() throws IOException {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(nameMap);
        arrayList.add(count);
        objectOutputStream.writeObject(arrayList);
        objectOutputStream.flush();
    }
    public void removeFromMaps(int index)
    {
        nameMap.remove(index);
        socketsMap.remove(index);
    }
    public Integer getLastClosedIndex() {
        return lastClosedIndex;
    }

    public void setLastClosedIndex(Integer lastClosedIndex) {
        this.lastClosedIndex = lastClosedIndex;
    }

}
