package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;

public class TrivialSteg
{

    private String typeOfFile = "";
    public TrivialSteg()
    {

    }

    public String getTypeOfFile() {
        return typeOfFile;
    }

    public void setTypeOfFile(String typeOfFile) {
        this.typeOfFile = typeOfFile;
    }

    public void encodeFile(String path, String original, String ext, String steg, String data)
    {
        BufferedImage imageCopy  = createCopy(getImage(getImagePath(path,original,ext)));
        imageCopy = addData(imageCopy,data);
        createEncodedImage(imageCopy, new File(getImagePath(path,steg,typeOfFile)), typeOfFile);
    }

    public String decodeFile(String path, String name, String ext)
    {
        byte[] decodedFile;
        try
        {
            BufferedImage image  = createCopy(getImage(getImagePath(path,name,ext)));
            decodedFile = decodeText(getByteArray(image));
            return(new String(decodedFile));
        }
        catch(Exception e)
        {
            Utils.errorDialog("No encoded data!");
            return "";
        }
    }

    private BufferedImage addData(BufferedImage image, String text)
    {
        byte img[] = getByteArray(image);
        byte msg[] = text.getBytes();
        byte len[] = convertBits(msg.length);
        try
        {
            encodeText(img, len,  0);
            encodeText(img, msg, 32);
        }
        catch(Exception e)
        {
            Utils.errorDialog("File cannot hold data!");
        }
        return image;
    }

    private BufferedImage createCopy(BufferedImage image)
    {
        BufferedImage new_img  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D	graphics = new_img.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose();
        return new_img;
    }

    private byte[] getByteArray(BufferedImage image)
    {
        WritableRaster raster = image.getRaster();
        DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
        return buffer.getData();
    }

    private byte[] convertBits(int i)
    {
        byte byte3 = (byte)((i & 0xFF000000) >>> 24);
        byte byte2 = (byte)((i & 0x00FF0000) >>> 16);
        byte byte1 = (byte)((i & 0x0000FF00) >>> 8 );
        byte byte0 = (byte)((i & 0x000000FF));
        return(new byte[]{byte3,byte2,byte1,byte0});
    }

    private String getImagePath(String path, String name, String ext)
    {
        return path + "/" + name + "." + ext;
    }

    private BufferedImage getImage(String f)
    {
        BufferedImage image	= null;
        File file = new File(f);
        try
        {
            image = ImageIO.read(file);
        }
        catch(Exception ex)
        {
            Utils.errorDialog("Image could not be read!");
        }
        return image;
    }

    private void createEncodedImage(BufferedImage image, File file, String ext)
    {
        try
        {
            file.delete();
            ImageIO.write(image,ext,file);
        }
        catch(Exception e)
        {
            Utils.errorDialog("File could not be saved!");
        }
    }

    private byte[] encodeText(byte[] image, byte[] addition, int offset)
    {
        Utils.infoDialog("Maximum size of data: " + ((image.length/1024)/8) + " bytes");

        if(addition.length + offset > image.length)
        {
            Utils.errorDialog("File too short!");
            throw new IllegalArgumentException("File too short!");
        }
        for(int i=0; i<addition.length; ++i)
        {
            int add = addition[i];
            for(int bit=7; bit>=0; --bit, ++offset) //ensure the new offset value carries on through both loops
            {
                int b = (add >>> bit) & 1;
                image[offset] = (byte)((image[offset] & 0xFE) | b );
            }
        }
        return image;
    }

    private byte[] decodeText(byte[] image)
    {
        int length = 0;
        int offset  = 32;
        for(int i=0; i<32; ++i)
        {
            length = (length << 1) | (image[i] & 1);
        }

        byte[] result = new byte[length];

        for(int b=0; b<result.length; ++b )
        {
            for(int i=0; i<8; ++i, ++offset)
            {
                result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
            }
        }
        return result;
    }
}
