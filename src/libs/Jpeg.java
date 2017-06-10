// Copyright (C) 1998, James R. Weeks and BioElectroMech.
// Visit BioElectroMech at www.obrador.com. Email James@obrador.com.

// This software is based in part on the work of the Independent JPEG Group.
// See license.txt for details about the allowed used of this software.
// See IJGreadme.txt for details about the Independent JPEG Group's license.

package libs; // westfeld

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Jpeg {
    public static void main(final String args[]) {
        Image image = null;
        FileOutputStream dataOut = null;
        File file, outFile;
        JpegEncoder jpg;
        String string = new String();
        int i, Quality = 80;
        if (args.length < 2) {
        }
        if (!args[0].endsWith(".jpg") && !args[0].endsWith(".tif") && !args[0].endsWith(".gif")) {
        }
        if (args.length < 3) {
            string = args[0].substring(0, args[0].lastIndexOf(".")) + ".jpg";
        } else {
            string = args[2];
            if (string.endsWith(".tif") || string.endsWith(".gif")) {
                string = string.substring(0, string.lastIndexOf("."));
            }
            if (!string.endsWith(".jpg")) {
                string = string.concat(".jpg");
            }
        }
        outFile = new File(string);
        i = 1;
        while (outFile.exists()) {
            outFile = new File(string.substring(0, string.lastIndexOf(".")) + i++ + ".jpg");
            if (i > 100) {
                System.exit(0);
            }
        }
        file = new File(args[0]);
        if (file.exists()) {
            try {
                dataOut = new FileOutputStream(outFile);
            } catch (final IOException e) {
            }
            try {
                Quality = Integer.parseInt(args[1]);
            } catch (final NumberFormatException e) {
            }
            image = Toolkit.getDefaultToolkit().getImage(args[0]);
            jpg = new JpegEncoder(image, Quality, dataOut, "");
            jpg.Compress();
            try {
                dataOut.close();
            } catch (final IOException e) {
            }
        } else {
            System.out.println("I couldn't find " + args[0] + ". Is it in another directory?");
        }
        System.exit(0);
    }
}
