package me.srinjoy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static me.srinjoy.Main.*;

public class Controller {
    @FXML public Button refresh;
    @FXML public Button connect;
    @FXML public Button disconnect;
    @FXML public TextField clientID;
    @FXML public TextField details;
    @FXML public TextField largeImageName;
    @FXML public TextField largeImageText;
    @FXML public TextField smallImageName;
    @FXML public TextField smallImageText;
    @FXML public TextField state;

    @FXML void initialize(){
        disconnect.setDisable(true);
        refresh.setDisable(true);
    }

    @FXML public void setConnect(ActionEvent event){
        try {
            System.out.println(Long.parseLong(clientID.getText()));
        } catch (NumberFormatException e) {
            return;
        }
        lib.Discord_Shutdown();
        lib.Discord_Initialize(clientID.getText(), handlers, false, null);
        clientID.setDisable(true);
        connect.setDisable(true);
        disconnect.setDisable(false);
        refresh.setDisable(false);
        setRefresh(event);
    }
    @FXML public void setRefresh(ActionEvent event) {
        presence.details = details.getText().trim();
        presence.state = state.getText().trim();
        presence.largeImageKey = largeImageName.getText().trim();
        presence.largeImageText = largeImageText.getText().trim();
        presence.smallImageKey = smallImageName.getText().trim();
        presence.smallImageText = smallImageText.getText().trim();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        lib.Discord_UpdatePresence(presence);
    }
    @FXML public void setDisconnect(ActionEvent event){
        lib.Discord_Shutdown();
        lib.Discord_Initialize("781938132047757364", handlers, false, null);
        disconnect.setDisable(true);
        clientID.setDisable(false);
        connect.setDisable(false);
        refresh.setDisable(true);
        presence.details = "Playing Discord RCP app";
        presence.state = "idle";
        presence.largeImageKey = "disco";
        presence.largeImageText = "Discord RPC";
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        lib.Discord_UpdatePresence(presence);
    }
}
