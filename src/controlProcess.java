/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package socialpccontroller;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.Post;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author Praveen
 */
public class controlProcess implements Runnable {

    private final String directory="me/statuses";
    public static boolean status=true;
    private FacebookClient fbclient=null;
    private Thread t;
    private String id="";
    private String prefix="";
    private final Pattern pattern;
    private Matcher matcher;
    private final commandMapper cmap;
    private final String appID="xxxxxxxxxxxxxxxxx";//Your App ID came here
    private final String appSecret="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";//Your App Secret Came Here
    private String extendedAccessToken;
    private AccessToken a;
    private Date expiry;
    private File commandLog;
    private FileWriter fw;
    private BufferedWriter bw;
    private boolean filewrite=true;
    private int count=1;
    private boolean isInternetConnected=true;
    public controlProcess(accessToken token,String email) throws FileNotFoundException
    {
        
        fbclient=new DefaultFacebookClient(token.getAccessToken());
        if(!token.isLongLived())
        {   a=fbclient.obtainExtendedAccessToken(appID, appSecret, token.getAccessToken());
        extendedAccessToken=a.getAccessToken();
        expiry=a.getExpires();
            try {
                DatabaseManager db=new DatabaseManager();
                SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                int s=db.updateQuery("update fb_user_tb set token='"+extendedAccessToken+"', LongLived=1, ExpiryDate='"+f.format(expiry)+"' where email='"+email+"'");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(controlProcess.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(controlProcess.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(controlProcess.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(controlProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
          
    }
        
        this.start();
        cmap=new commandMapper();
        prefix="computer";
        pattern=Pattern.compile(prefix+" .*");
    }
    public void start() throws FileNotFoundException
    {
        t=new Thread(this);
        t.start();
         File f=new File("userdata.dat");
           if(f.exists()){
            Scanner scan=new Scanner(new FileInputStream(f));
            while(scan.hasNext())
            {
                id=scan.nextLine();
                String s[]=id.split("-");
                if(s[0].equals("lastid"))
                {
                    id=s[1];
                    break;
                }
            }
           }
        System.out.println("Thread Start");
    }
    @Override
    public void run() {
       while(status)
       {
           System.err.println("Started");
           try{
           Connection<Post> myFeed = fbclient.fetchConnection(directory, Post.class);
           Post commandPost=myFeed.getData().get(0);
           String command=commandPost.getMessage().toLowerCase();
           String id1=commandPost.getId();
           if(!id.equals(id1))
           {
               id=id1;
               matcher=pattern.matcher(command);
               if(matcher.find())
               {
                   
                   try{
                       commandLog = new File("CommandLog.dat");
                       File f=new File("userdata.dat");
                       if (!commandLog.exists()) {
                           commandLog.createNewFile();
                       }
                       if(!f.exists())
                       {
                           f.createNewFile();
                       }
                       fw = new FileWriter(commandLog, true);
                       bw = new BufferedWriter(fw);
                       FileWriter fw1=new FileWriter(f);
                      BufferedWriter bw1=new BufferedWriter(fw1);
                      bw1.write("lastid-"+id);
                       bw1.close();
                       fw1.close();
                       if (filewrite) {
                           bw.write("\n"+new Date().toString() + "\n------------------------------\n");
                       }
                       bw.write(count++ + " " + command + "\n");
                       bw.close();
                       fw.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   cmap.executeCommand(command);
                   if(cmap.publish)
                   {
                       new publishPost(cmap.publishType,fbclient);
                       cmap.publish=false;
                   }
               }
               System.out.println(command);
           }
           else
           {
               System.err.println("Repeat");
           }}
           catch(FacebookNetworkException e)
           {
               System.out.println("FB Network Exception");
               CheckConnection c=new CheckConnection();
               c.start();
              isInternetConnected=false;
               synchronized(c)
               {
                   isInternetConnected=true;
               }
           }
           catch(FacebookOAuthException e)
           {
               status=false;
           }
           try {
               Thread.sleep(2000);
           } catch (InterruptedException ex) {
               Logger.getLogger(controlProcess.class.getName()).log(Level.SEVERE, null, ex);
           }
       }       
       System.err.println("Aborted");
    }
    
    
    
    
}

class CheckConnection extends Thread
{
    
    public boolean isInternetConnected=false;
     public synchronized void run()
    {
        while(!isInternetConnected){
         try {
                URL url = new URL("http://www.google.com");
                HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
                Object objData = urlConnect.getContent();
                isInternetConnected=true;
                this.notifyAll();
            } catch (UnknownHostException e) {
                e.printStackTrace();
             try {
                 Thread.sleep(5000);
             } catch (InterruptedException ex) {
                 Logger.getLogger(CheckConnection.class.getName()).log(Level.SEVERE, null, ex);
             }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   
}
