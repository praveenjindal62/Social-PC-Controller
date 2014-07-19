/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package socialpccontroller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author Praveen
 */
public class commandMapper {
    private String filename="commandlist.dat";
    private List<String[]> commandSet;
    private Scanner scan;
    private String execute="";
    static boolean publish=false;
    static String publishType="";
    private boolean cmd=false;
    public commandMapper()
    {
        try {
            scan=new Scanner(new FileInputStream("commandlist.dat"));
            commandSet=new ArrayList<String[]>();
            while(scan.hasNext())
            {
                String line=scan.nextLine();
                String c[]=line.split("-");
                commandSet.add(c);
                //System.out.println(c[0]+" "+c[1]);
            }
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch(IOException e)
        {
            
        }
    }
    public String mapCommand(String command)
    {
        String pcmd="";
        boolean commandFound=false;
        for(String[] temp:commandSet)
        {
            if(command.matches(temp[1]))
            {
                commandFound=true;
                int cid=Integer.valueOf(temp[0]);
                switch(cid)
                {
                    case 1:
                        String msg="",time="";
                        Pattern pattern = Pattern.compile("['\"].*['\"]");
                        Matcher matcher = pattern.matcher(command);
                        if (matcher.find())
                        {
                            msg=matcher.group(0);
                            msg=msg.substring(1,msg.length()-1);
                            System.out.println(msg);
                        }
                        pattern = Pattern.compile("\\d{2,3}");
                        matcher = pattern.matcher(command);
                        if (matcher.find())
                        {
                            time=matcher.group(0);
                            System.out.println(time);
                        }
                        pcmd="initshutdown \""+msg+"\" "+time+" force";
                        break;
                    case 2:
                        pcmd="mutesysvolume 1";
                        break;
                    case 3:
                        pcmd="mutesysvolume 0";
                        break;
                    case 4:
                        pcmd="mutesysvolume 2";
                        break;
                    case 5:
                        pcmd="monitor off";
                        break;
                    case 6:
                        pcmd="standby";
                        break;
                    case 7:
                        pcmd="exitwin logoff";
                        break;
                    case 8:
                        pcmd="exitwin reboot";
                        break;
                    case 9:
                        pcmd="abortshutdown";
                        break;
                    case 10:
                        pcmd="hibernate";
                        break;
                    case 11:
                        String vol="";
                        pattern = Pattern.compile("\\d{1,5}");
                        matcher = pattern.matcher(command);
                        if (matcher.find())
                        {
                            vol=matcher.group(0);
                            System.out.println(vol);
                        }
                        pcmd="setvolume 0 "+vol+" "+vol;
                        break;
                    case 12:
                        pcmd="changesysvolume 6553";
                        break;
                    case 13:
                        pcmd="changesysvolume -6553";
                        break;
                    case 14:
                        String infomsg="";
                        pattern = Pattern.compile("[\"'].*[\"']");
                        matcher = pattern.matcher(command);
                        if (matcher.find())
                        {
                            infomsg=matcher.group(0);
                            infomsg=infomsg.substring(1,infomsg.length()-1);
                            System.out.println(infomsg);
                        }
                        pcmd="infobox \""+infomsg+"\" \"SPC Controller\"";
                        break;
                    case 15:
                        String p=System.getProperty("user.home") + File.separatorChar + "My Documents";
                        pcmd="savescreenshot \""+p+"\\spcshot.png\"";
                        publish=true;
                        publishType="Image";
                        break;
                        
                    case 18:
                        publish=true;
                        pcmd="tasklist";
                        cmd=true;
                        break;
                    case 19:
                        String PID="";
                        pattern = Pattern.compile("\\d{1,5}");
                        matcher = pattern.matcher(command);
                        if (matcher.find())
                        {
                            PID=matcher.group(0);
                            System.out.println(PID);
                        }
                        pcmd="killprocess /"+PID;
                        break;
                        
                }
                
            }
            if (commandFound)
                break;
        }
        return pcmd;
    }
    public static void main(String args[])
    {
        String command="screenshot";
        commandMapper m=new commandMapper();
        //System.out.println(m.executeCommand(command));
    }
    public void executeCommand(String command)
    {
        String command1=command.replaceAll("computer ", "");
                   System.out.println(command1);
                   String result=this.mapCommand(command1);
                    
                   String path=System.getProperty("user.dir");
                   path="cmd /c "+path+"\\nircmdc.exe "+result;
                   if(cmd){
                       path="cmd /c tasklist";
                       cmd=false;
                   }
                     System.out.println(path);
                   try {
                       final Process process = Runtime.getRuntime().exec(path);
                       Scanner scan=new Scanner(process.getInputStream());
                       while(scan.hasNext())
                       publishType+=scan.nextLine()+"\n";
                       
                   } catch (IOException ex) {
                       Logger.getLogger(controlProcess.class.getName()).log(Level.SEVERE, null, ex);
                   }
       
    }
    
}
