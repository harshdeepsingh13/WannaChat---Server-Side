package server.threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

/**
 * hey Created by hdsingh2015 on 26-07-2017.
 */
public class MyClientServerHandlingThread extends Thread{
    private MyServerThread myServerThread;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private int mySocketIndex;


    public MyClientServerHandlingThread(MyServerThread myServerThread, int count) {
        super("MyClientServerHandlingThread");
        this.myServerThread = myServerThread;
        socket = myServerThread.getSocket();
        mySocketIndex = count;
        try {
            dataInputStream = new DataInputStream(myServerThread.getSocket().getInputStream());
            dataOutputStream = new DataOutputStream(myServerThread.getSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean isExit = false;
        while(!isExit)
        {
            try {
                String infoFromClient = dataInputStream.readUTF();
                System.out.println("infoFromClient: " + infoFromClient);
                int choice = Integer.parseInt(String.valueOf(infoFromClient.charAt(0)));
                System.out.println("choice: " + choice);


                switch(choice)
                {
                    case 1:
                    {
                        String strArray[] = infoFromClient.split(" ");
                        int indexOFClientToBeClosed = Integer.parseInt(strArray[1]);
                        myServerThread.removeFromMaps(indexOFClientToBeClosed);
                        myServerThread.setLastClosedIndex(indexOFClientToBeClosed);
                        isExit = true;
                        break;
                    }
                    case 2:
                    {
                        String strArray[] = infoFromClient.split("\n");
                        System.out.println(strArray[1] + " " + strArray[2]);
                        String message = strArray[1];
                        ArrayList<Socket> recipientsList = new ArrayList<>();
                        String recipientsArray[] = strArray[2].split(" ");

                        for(String str : recipientsArray)
                        {
                            System.out.println("recipientsArray: " + str);
                            for(Map.Entry<Integer,String> i:myServerThread.getNameMap().entrySet())
                            {
                                int index = -1;
                                if(i.getValue().equals(str)) {
                                    index = i.getKey();
                                    System.out.println("index: " + index);
                                    Socket s = myServerThread.getSocketsMap().get(index);
                                    recipientsList.add(s);
                                }
                            }
                        }
                        System.out.println("recipientsList: " + recipientsList);
                        myServerThread.getMyServerSuperClass().sendingAdditionalData(recipientsList,message,new Integer(mySocketIndex));
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
