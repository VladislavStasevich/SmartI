package utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.net.MalformedURLException;

public class ImageFileChooser {
    public static String getImageFilePath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isFile()) {
                    return f.getName().contains(".png")
                            || f.getName().contains(".jpg");
                } else {
                    return true;
                }
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        int intVal = fileChooser.showOpenDialog(null);
        if (intVal == 1) {
            System.out.println("The close button was clicked");
        }
        if (intVal == 0) {
            File file = fileChooser.getSelectedFile();
            try {
                return file.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
