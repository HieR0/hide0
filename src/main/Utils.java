package main;

import javafx.scene.control.Alert;

import java.io.File;

public class Utils {
    public static String getExtension(File f)
    {
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1)
            return s.substring(i+1).toLowerCase();
        return "";
    }
    public static void errorDialog(String info) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Error");
        alert.setContentText(info);
        alert.showAndWait();
    }
    public static void infoDialog(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText(info);
        alert.show();
    }
}
