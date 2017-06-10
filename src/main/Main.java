package main;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.text.Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javafx.stage.StageStyle;
import jfxtras.labs.scene.control.window.Window;
import jfxtras.labs.scene.layout.ScalableContentPane;

public class Main extends Application {
    private Desktop desktop = Desktop.getDesktop();
    private TrivialSteg model = new TrivialSteg();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        primaryStage.setTitle("Hide0");
        primaryStage.setScene(new Scene(root, 600, 650));
        primaryStage.show();
        primaryStage.setResizable(false);

        root.getStylesheets().add(
                getClass().getResource("mod.css").toExternalForm());

        ToggleGroup group = new ToggleGroup();
        RadioButton b,b2,b3,b4;
        Text tex = (Text) root.lookup("#tex");
        Text tex2 = (Text) root.lookup("#tex2");
        tex.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        tex.setFill(Color.WHITE);
        tex2.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        tex2.setFill(Color.WHITE);
        b = (RadioButton) root.lookup("#rb");
        b2 = (RadioButton) root.lookup("#rb2");
        b3 = (RadioButton) root.lookup("#rb3");
//        BorderPane borderPane = (BorderPane) root.lookup("#borderPane");
//        ScalableContentPane scaledPane = new ScalableContentPane();
//        borderPane.setCenter(scaledPane);
//        ImageView iv = new ImageView();
//        scaledPane.getChildren().add(iv);
//        scaledPane.prefHeight(453.0);
//        scaledPane.prefWidth(496.0);
        group.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (group.getSelectedToggle().equals(b)) {
                model.setTypeOfFile("jpg");
            } else if(group.getSelectedToggle().equals(b2)) {
                model.setTypeOfFile("png");
            } else if(group.getSelectedToggle().equals(b3)){
                model.setTypeOfFile("bmp");
            }
        });
        b.setToggleGroup(group);
        b2.setToggleGroup(group);
        b3.setToggleGroup(group);
        group.selectToggle(b);

        TextArea tf = (TextArea) root.lookup("#hi");
        Button btn = (Button) root.lookup("#btn");
        Button btn2 = (Button) root.lookup("#btn2");
        ImageView iv = (ImageView) root.lookup("#iv");
        btn.setText("E\nN\nC\nO\nD\nE");
        btn2.setText("D\nE\nC\nO\nD\nE");
        btn.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        btn2.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        final FileChooser fileChooser = new FileChooser();
        String[] filters = {"*.jpg", "*.png", "*.bmp"};
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("IMAGE files (*.png), (*.jpg), (*.bmp)", filters);
        fileChooser.getExtensionFilters().add(extFilter);

        btn.setOnAction(
                e -> {
                    File file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                        String text = tf.getText();
                        if(text.equals("")) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText("You haven't defined the hidden data.");
                            Label label = new Label("Define it here:");

                            TextArea textArea = new TextArea();
                            textArea.setEditable(true);
                            textArea.setWrapText(true);
                            textArea.setMaxWidth(Double.MAX_VALUE);
                            textArea.setMaxHeight(Double.MAX_VALUE);
                            GridPane.setVgrow(textArea, Priority.ALWAYS);
                            GridPane.setHgrow(textArea, Priority.ALWAYS);
                            GridPane expContent = new GridPane();
                            expContent.setMaxWidth(Double.MAX_VALUE);
                            expContent.add(label, 0, 0);
                            expContent.add(textArea, 0, 1);
                            alert.getDialogPane().setContent(expContent);
                            alert.showAndWait();
                            text = textArea.getText();
                        }

                        String ext  = Utils.getExtension(file);
                        String name = file.getName();
                        String path = file.getPath();
                        path = path.substring(0,path.length()-name.length()-1);
                        name = name.substring(0, name.length()-4);
                        String stegan = name + "-code";

                        if(model.getTypeOfFile().equals("jpg")) {
                            EncodeToJPG.encodeFile(path+"\\"+name+"."+ext, text);
                        } else {
                            model.encodeFile(path,name,ext,stegan,text);
                        }

                        Image img = null;
                        try {
                            img = new Image(file.toURI().toURL().toExternalForm());
                        } catch (MalformedURLException e1) {
                            e1.printStackTrace();
                        }
                        iv.setImage(img);
                        tf.clear();
                        tex.setText("Data to be encoded:");
                    }
                });

        btn2.setOnAction(
                e -> {
                    File file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                        String ext  = Utils.getExtension(file);
                        String name = file.getName();
                        String path = file.getPath();
                        path = path.substring(0,path.length()-name.length()-1);
                        name = name.substring(0, name.length()-4);

                        if(ext.equals("jpg")) {
                            DecodeFromJPG.resetTemps();
                            tf.setText(DecodeFromJPG.decodeFile(path+"\\"+name+"."+ext));
                        } else {
                            tf.setText(model.decodeFile(path, name, ext));
                        }

                        Image img = null;
                        try {
                            img = new Image(file.toURI().toURL().toExternalForm());
                        } catch (MalformedURLException e1) {
                            e1.printStackTrace();
                        }
                        iv.setImage(img);
                        tex.setText("Decoded data:");
                    }
                });



    }

    public static void main(String[] args) {
        launch(args);
    }
}
