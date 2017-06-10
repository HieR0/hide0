package main;

import libs.JpegEncoder;
import libs.Bmp;

import java.awt.*;
import java.io.*;

public class EncodeToJPG {

    public static void encodeFile(String fileName, String data) {
        Image image = null;
        FileOutputStream dataOut = null;
        BufferedWriter writer = null;
        File file, outFile, temp = null;
        JpegEncoder jpg;
        int i, Quality = 80;

        String comment = "";
        String password = "abc123";
        String inFileName = fileName;
        String outFileName = inFileName.substring(0, inFileName.lastIndexOf(".")) + "-code.jpg";
        String embFileName = "";

        outFile = new File(outFileName);
        //file = new File(inFileName);
        try {
            dataOut = new FileOutputStream(outFile);
        } catch (final IOException e) {
        }

        if (inFileName.endsWith(".bmp")) {
            final Bmp bmp = new Bmp(inFileName);
            image = bmp.getImage();
        } else {
            image = Toolkit.getDefaultToolkit().getImage(inFileName);
        }
        jpg = new JpegEncoder(image, Quality, dataOut, comment);

        try {
            temp = File.createTempFile("code", ".txt");
            writer = new BufferedWriter( new FileWriter(temp));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally
        {
            if ( writer != null)
                try {
                    writer.close( );
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        try {
            if (embFileName == null) {
                jpg.Compress();
            } else {
                jpg.Compress(new FileInputStream(temp), password);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        } catch (final Error e) {
        }
            try {
                dataOut.close();
            } catch (final IOException e) {

            }
    }
}
