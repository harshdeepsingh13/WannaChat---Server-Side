package server.threads;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by hdsingh2015 on 26-07-2017.
 */
public class MyServerSuperClass extends Thread{
    private MyServerThread myServerThread;
    private ArrayList<Object> myArrayList = new ArrayList<>();
    private boolean sendAdditionalInfo=false;
    private ArrayList<Socket> recipientsArrayList = new ArrayList<>();
    private String messageToClient;
    private Integer sendersIndex;

    public MyServerSuperClass(MyServerThread myServerThread) {
        super("MyServerSuperClass");
        this.myServerThread = myServerThread;
    }

    @Override
    public void run() {
        int previous = myServerThread.getSocketsMap().size()-1;
        while(true) {
            if (myServerThread.getSocketsMap().size() != previous || sendAdditionalInfo) {
                myArrayList.clear(); myArrayList.add(myServerThread.getNameMap());
                myArrayList.add(myServerThread.getLastClosedIndex());
                if(sendAdditionalInfo)
                {
                    myArrayList.add(Boolean.TRUE);
                    myArrayList.add(sendersIndex);
                    myArrayList.add(messageToClient);
                }
                for (Integer index : myServerThread.getSocketsMap().keySet()) {
                    Socket socket = myServerThread.getSocketsMap().get(index);
                    if(sendAdditionalInfo)
                    {
                        System.out.println("recipientsArrayList: " + recipientsArrayList);
                        if(recipientsArrayList.contains(socket) )
                        try {
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            objectOutputStream.writeObject(myArrayList);
                            objectOutputStream.flush();
                            System.out.println("sendAdditionalInfo Flushed");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("sendersIndex: " + sendersIndex);
                    }
                    else {
                        myArrayList.add(Boolean.FALSE);
                        try {
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            objectOutputStream.writeObject(myArrayList);
                            objectOutputStream.flush();
                            System.out.println("Flushed");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                System.out.println("previous: " + previous);
                System.out.println("myServerThread.getSocketsMap().size(): " + myServerThread.getSocketsMap().size());
                previous=myServerThread.getSocketsMap().size();
                System.out.println("previous: " + previous);
                System.out.println("myServerThread.getSocketsMap().size(): " + myServerThread.getSocketsMap().size());
            }

            recipientsArrayList=null;
            messageToClient = null;
            sendersIndex = null;

            sendAdditionalInfo=false;

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    public void sendingAdditionalData(ArrayList<Socket> recipientsArray, String message, Integer integer)
    {
        recipientsArrayList = recipientsArray;
        messageToClient = message;
        sendersIndex = integer;
        System.out.println("sendersIndex: " + sendersIndex);
        sendAdditionalInfo=true;
    }
}
