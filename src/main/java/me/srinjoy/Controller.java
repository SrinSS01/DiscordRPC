package me.srinjoy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

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
    @FXML public Button clear;
    @FXML public ListView<String> history;
    static HashMap<String, HashMap<String, String>> parsedPresenceData = new HashMap<>();
    static File USERDATA = new File(System.getProperty("user.home") + "/UserData");
    static File presenceData = new File(USERDATA, "presenceData.json");
    @SuppressWarnings("unchecked")
    @FXML void initialize(){
        if (!USERDATA.exists()) System.out.println(USERDATA.mkdir());
        System.out.println(USERDATA.getAbsolutePath());
        if (!presenceData.exists()) try {
            System.out.println(presenceData.createNewFile());
            FileWriter writer = new FileWriter(presenceData);
            writer.write("{}");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        try {
            parsedPresenceData = (JSONObject) parser.parse(new FileReader(presenceData));
            parsedPresenceData.forEach((key, __) -> history.getItems().add(key));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        disconnect.setDisable(true);
        refresh.setDisable(true);
        history.setOnKeyPressed(event -> {
            String selectedID = history.getSelectionModel().getSelectedItem();
            if (event.getCode() == KeyCode.DELETE && selectedID != null) {
                parsedPresenceData.remove(selectedID);
                history.getItems().remove(selectedID);
                writeData();
            }
        });
    }

    @FXML public void setClear(ActionEvent event) {
        clientID.clear();
        details.clear();
        state.clear();
        largeImageName.clear();
        largeImageText.clear();
        smallImageName.clear();
        smallImageText.clear();
    }

    @FXML public void onClickHistory(){
        String selectedID = history.getSelectionModel().getSelectedItem();
        if (selectedID == null) return;
        HashMap<String, String> elements = parsedPresenceData.get(selectedID);
        clientID.setText(selectedID);
        details.setText(elements.get("details"));
        state.setText(elements.get("state"));
        largeImageName.setText(elements.get("largeImageKey"));
        largeImageText.setText(elements.get("largeImageText"));
        smallImageName.setText(elements.get("smallImageKey"));
        smallImageText.setText(elements.get("smallImageText"));
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
        history.setDisable(true);
        clear.setDisable(true);
        setRefresh(event);
    }
    @FXML public void setRefresh(ActionEvent event) {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put("details", presence.details = details.getText().trim());
        properties.put("state", presence.state = state.getText().trim());
        properties.put("largeImageKey", presence.largeImageKey = largeImageName.getText().trim());
        properties.put("largeImageText", presence.largeImageText = largeImageText.getText().trim());
        properties.put("smallImageKey", presence.smallImageKey = smallImageName.getText().trim());
        properties.put("smallImageText", presence.smallImageText = smallImageText.getText().trim());
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        lib.Discord_UpdatePresence(presence);
        parsedPresenceData.put(clientID.getText(), properties);
        if (!history.getItems().contains(clientID.getText()))
            history.getItems().add(clientID.getText());
    }
    @FXML public void setDisconnect(ActionEvent event){
        writeData();
        lib.Discord_Shutdown();
        lib.Discord_Initialize("781938132047757364", handlers, false, null);
        disconnect.setDisable(true);
        clientID.setDisable(false);
        connect.setDisable(false);
        refresh.setDisable(true);
        history.setDisable(false);
        clear.setDisable(false);
        presence.details = "Playing Discord RPC app";
        presence.state = "idle";
        presence.largeImageKey = "disco";
        presence.largeImageText = "Discord RPC";
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        lib.Discord_UpdatePresence(presence);
    }
    static void writeData() {
        JSONObject jsonObject = new JSONObject(parsedPresenceData);
        try {
            FileWriter writer = new FileWriter(presenceData);
            writer.write(jsonObject.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
