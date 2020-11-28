package me.srinjoy;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.OffsetDateTime;

public class Main extends Application {

    static IPCClient client = new IPCClient(781938132047757364L);
    static RichPresence.Builder builder = new RichPresence.Builder()
                                .setDetails("Playing Discord RCP app")
                                .setStartTimestamp(OffsetDateTime.now())
                                .setLargeImage("disco", "Discord RPC");
    static {
        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                client.sendRichPresence(builder.build());
            }
        });
        try {
            client.connect();
        } catch (NoDiscordClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("Discord RCP");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Closing...");
            if (client != null && client.getStatus() == PipeStatus.CONNECTED) {
                client.close();
                System.out.println("Client Disconnected");
            }
        });
    }
}
