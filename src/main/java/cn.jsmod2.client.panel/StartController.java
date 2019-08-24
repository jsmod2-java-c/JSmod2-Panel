package cn.jsmod2.client.panel;

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

    private ConsoleOutputStream stream = new ConsoleOutputStream();


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
    private void initialize(){
        stream.setTextArea(consoleTextArea);
    }

    @FXML
    public void onSend(ActionEvent event){
        String text = sendText.getText();
        strings.add(text);
        Client.getInstance().setMessage(new JsonRequester().append("type","command").append("command",text).toJSON());
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
