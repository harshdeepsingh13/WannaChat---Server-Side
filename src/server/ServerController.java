package server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable{
    @FXML private ListView listView;
    @FXML private ImageView logoImageView;
    @FXML private MenuItem aboutMenuItem;

    private ObservableList<Socket> observableList;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logoImageView.setImage(new Image("server/Wanna_Chat_logo_Server-01.png"));
        observableList = FXCollections.observableArrayList();
        listView.setItems(observableList);

        MyServerThread myServerThread = new MyServerThread(this);
        myServerThread.start();

        System.out.println("aboutMenuItem onAction");
        aboutMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/server/Wanna_Chat_logo_Server-01.png"));
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/server/dialogStyle.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("dialogStyle");
            alert.setTitle("About");
            alert.setHeaderText("About WannaChat Server");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/server/About.txt")));

            StringBuffer stringBuffer = new StringBuffer();
            String s;
            try {
                while((s=bufferedReader.readLine())!=null)
                {
                    stringBuffer.append(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            s = stringBuffer.toString();
            alert.setContentText("WannaChat - Server\n" + s);
            alert.getButtonTypes().remove(ButtonType.CANCEL);
            alert.showAndWait();
        });
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
