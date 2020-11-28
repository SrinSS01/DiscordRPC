package me.srinjoy;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

// 781179867479539742
public class Main extends Application {
    public static DiscordRPC lib = DiscordRPC.INSTANCE;
    public static String clientIDString;
    public static final DiscordRichPresence presence = new DiscordRichPresence();
    public static final DiscordEventHandlers handlers = new DiscordEventHandlers();
    static {
        handlers.ready = user -> System.out.println(user.username + " Ready!!!");
        lib.Discord_Initialize(clientIDString = "781938132047757364", handlers,false, null);
        presence.details = "Playing Discord RCP app";
        presence.state = "idle";
        presence.largeImageKey = "disco";
        presence.largeImageText = "Discord RPC";
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        lib.Discord_UpdatePresence(presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()){
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
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
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("logo.png")));
        primaryStage.setOnCloseRequest(event -> {
            lib.Discord_Shutdown();
            System.exit(0);
        });
    }
}
