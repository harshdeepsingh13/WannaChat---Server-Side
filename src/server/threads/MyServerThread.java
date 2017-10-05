package server.threads;

import javafx.application.Platform;
import server.controller.ServerController;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
        return new ConcurrentHashMap<>(socketsMap);
    }

    public Socket getSocket() {
        return socket;
    }

    public Map<Integer, String> getNameMap() {
        return new ConcurrentHashMap<>(nameMap);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(2000);
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
