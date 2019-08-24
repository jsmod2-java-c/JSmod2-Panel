package cn.jsmod2.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author magiclu550
 * @author GNX-Susanoo
 */

public class StartController {

    private List<String> strings = new ArrayList();

    private ConsoleOutputStream stream = ConsoleOutputStream.getStream();


    private AtomicInteger integer = new AtomicInteger(0);
    @FXML
    private AnchorPane pane;
    @FXML
    private TextArea consoleTextArea;
    @FXML
    public TextField lineHeightTextField;
    @FXML
    public CheckBox scollCheckBox;
    @FXML
    public Button lineHeightSettingButton;

    @FXML
    public Button send;

    @FXML
    public TextField sendText;

    @FXML
    public Button go;

    @FXML
    public Button back;

    @FXML
    public TextField ipText;

    @FXML
    private void initialize(){
        stream.setTextArea(consoleTextArea);
        new Thread(()->{
            while (true){
                try {
                    Response response = new JsonRequester().append("type","log").sendOut();
                    if (response != null) {
                        if (!response.getMessage().equals("null")) {
                            stream.write(response.getMessage());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @FXML
    public void onIpOK(){
        String text = ipText.getText();
        if(text.contains(":")){
            String[] map = text.split(":");
            Client.getInstance().ipAddress = map[0].trim();
            Client.getInstance().port = Integer.parseInt(map[1]);
        }else{
            Client.getInstance().ipAddress = text;
            Client.getInstance().port = 20020;
        }
        ipText.setDisable(true);
    }

    @FXML
    public void onIpText(){

    }


    @FXML
    public void onSend(ActionEvent event){
        String text = sendText.getText();
        strings.add(text);
        Client.getInstance().setMessage(new JsonRequester().append("type","command").append("command",text).sendOut().getMessage());
        sendText.clear();
        integer.set(0);
    }

    @FXML
    public void onGo(ActionEvent event){
        if(integer.get()>=strings.size()||integer.get()<0){
            integer.set(0);
        }
        sendText.setText(strings.get(integer.getAndIncrement()));
    }

    @FXML
    public void onBack(ActionEvent event){
        if(integer.get()<0||integer.get()>=strings.size()){
            integer.set(strings.size()-1);
        }
        sendText.setText(strings.get(integer.getAndDecrement()));
    }


    @FXML
    public void settingLineHeight(ActionEvent actionEvent) {
        int lineHeight;
        try{
            lineHeight = Integer.valueOf(lineHeightTextField.getText());
        }catch(Exception e){
            lineHeightTextField.setText("长度非纯数字，请重新输入");
            return;
        }
        stream.setLineHeight(lineHeight);
    }

    @FXML
    private void scollCheck(ActionEvent actionEvent) {
        stream.setScroll(!scollCheckBox.isSelected());
    }
}
