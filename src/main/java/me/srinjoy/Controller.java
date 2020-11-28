package me.srinjoy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static me.srinjoy.Main.*;

public class Controller{
    @FXML public Button connect;
    @FXML public Button disconnect;
    @FXML public TextField clientID;
    @FXML public TextField details;
    @FXML public TextField largeImageName;
    @FXML public TextField largeImageText;
    @FXML public TextField smallImageName;
    @FXML public TextField smallImageText;
    @FXML public TextField state;

    @FXML public void setConnect(ActionEvent event){
        try {
            System.out.println(Long.parseLong(clientID.getText()));
        } catch (NumberFormatException e) {
            return;
        }
        if (!clientIDString.equals(clientID.getText())) {
            lib.Discord_Shutdown();
            lib.Discord_Initialize(clientID.getText(), handlers, false, null);
            clientIDString = clientID.getText();
        }
        presence.details = details.getText();
        presence.state = state.getText();
        presence.largeImageKey = largeImageName.getText();
        presence.largeImageText = largeImageText.getText();
        presence.smallImageKey = smallImageName.getText();
        presence.smallImageText = smallImageText.getText();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        lib.Discord_UpdatePresence(presence);
    }
    @FXML public void setDisconnect(ActionEvent event){
        if (!clientIDString.equals("781938132047757364")){
            lib.Discord_Shutdown();
            lib.Discord_Initialize("781938132047757364", handlers, false, null);
            clientIDString = "781938132047757364";
        }
        presence.details = "Playing Discord RCP app";
        presence.state = "idle";
        presence.largeImageKey = "disco";
        presence.largeImageText = "Discord RPC";
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        lib.Discord_UpdatePresence(presence);
    }
}
