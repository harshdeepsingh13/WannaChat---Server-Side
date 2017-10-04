package server.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ServerMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/server/fxml/servergui.fxml"));
        primaryStage.setTitle("Wanna Chat Server");
        primaryStage.getIcons().add(new Image("server/resources/images/Wanna_Chat_logo_Server-01.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.setOnCloseRequest(event -> {
            try {
                stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
