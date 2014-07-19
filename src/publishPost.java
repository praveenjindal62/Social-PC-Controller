/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package socialpccontroller;

import com.restfb.BinaryAttachment;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Praveen
 */
public class publishPost implements Runnable {

    private FacebookClient fbclient;
    private String postString;
    private int noOfTries=0;
    private boolean postSuccess=false;
    @Override
    public void run() {
        System.out.println("Posting Thread Started");
        while(noOfTries<5 && !postSuccess)
        {
            if(postString.equals("Image"))
            {
                String p=System.getProperty("user.home") + File.separatorChar + "My Documents";
                p+=File.separatorChar+"spcshot.png";
                File f=new File(p);
                InputStream is=null;
                try {
                    BufferedImage bi=ImageIO.read(f);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bi, "png", baos );
                    baos.flush();
                    is=new ByteArrayInputStream(baos.toByteArray());
                } catch (IOException ex) {
                    Logger.getLogger(publishPost.class.getName()).log(Level.SEVERE, null, ex);
                }
                FacebookType publishPhotoResponse = fbclient.publish("me/photos", FacebookType.class,
                  BinaryAttachment.with("spcshot.png",is),
                  Parameter.with("message", "ScreenShot"),Parameter.with("privacy", "{'value':'SELF'}"));
                System.out.println("Image posted");
                postSuccess=true;

            }
            else
            {
                FacebookType publishMessageResponse =
                fbclient.publish("me/feed", FacebookType.class,Parameter.with("message", postString),Parameter.with("privacy", "{'value':'SELF'}"));
                System.out.println("String posted");
                postSuccess=true;
            }
            noOfTries++;
        }
    }
    
    public publishPost(String postString,FacebookClient fbclient)
    {
        this.fbclient=fbclient;
        this.postString=postString;
        Thread t=new Thread(this);
        t.start();
    }
    
}
