package me.srinjoy;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.time.OffsetDateTime;

import static me.srinjoy.Main.*;

public class Controller implements IPCListener {
    @FXML public Button connect;
    @FXML public Button disconnect;
    @FXML public TextField clientID;
    @FXML public TextField details;
    @FXML public TextField largeImageName;
    @FXML public TextField largeImageText;
    @FXML public TextField smallImageName;
    @FXML public TextField smallImageText;
    private static String detailsString, largeImageNameString, largeImageTextString, smallImageNameString, smallImageTextString;

    @FXML public void setConnect(ActionEvent event){
        if (client.getStatus() == PipeStatus.CONNECTED) client.close();
        detailsString = details.getText();
        largeImageNameString = largeImageName.getText();
        largeImageTextString = largeImageText.getText();
        smallImageNameString = smallImageName.getText();
        smallImageTextString = smallImageText.getText();
        try {
            client = new IPCClient(Long.parseLong(clientID.getText()));
            client.setListener(this);
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML public void setDisconnect(ActionEvent event){
        if (client.getStatus() == PipeStatus.CONNECTED) {
            client.close();
            client = new IPCClient(781938132047757364L);
            detailsString = "Playing Discord RCP app";
            largeImageNameString = "disco";
            largeImageTextString = "Discord RPC";
            smallImageNameString = "";
            smallImageTextString = "";
            client.setListener(this);
            try {
                client.connect();
            } catch (NoDiscordClientException e) {
                e.printStackTrace();
            }
        }
        else System.out.println("Already disconnected");
    }

    @Override
    public void onReady(IPCClient client) {
        largeImageNameString = largeImageNameString.isEmpty() ? "   ": largeImageNameString;
        largeImageTextString = largeImageTextString.isEmpty() ? "   ": largeImageTextString;
        smallImageNameString = smallImageNameString.isEmpty() ? "   ": smallImageNameString;
        smallImageTextString = smallImageTextString.isEmpty() ? "   ": smallImageTextString;
                 builder.setDetails(detailsString)
                        .setLargeImage(largeImageNameString, largeImageTextString)
                        .setSmallImage(smallImageNameString, smallImageTextString)
                        .setStartTimestamp(OffsetDateTime.now());
        client.sendRichPresence(builder.build());
    }
}
