package server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable{
    @FXML private ListView listView;
    @FXML private ImageView logoImageView;

    private ObservableList<Socket> observableList;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logoImageView.setImage(new Image("server/Wanna_Chat_logo_Server-01.png"));
        observableList = FXCollections.observableArrayList();
        listView.setItems(observableList);

        MyServerThread myServerThread = new MyServerThread(this);
        myServerThread.start();
    }
    public void addToOberservableList(Object o)
    {
        observableList.add((Socket) o);
    }
    public void removeFromObservableList(Object o)
    {
        observableList.remove(o);
    }


}
