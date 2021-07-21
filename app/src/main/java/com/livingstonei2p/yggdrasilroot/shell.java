package com.livingstonei2p.yggdrasilroot;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
public class shell
{
    public static Process run_exec(String command){
	Process p = null;
        try {
             p= new ProcessBuilder()
                    .command("/sbin/su")
                    .redirectErrorStream(true).start();
        } catch (IOException exception) {
	    try{
             p=  new ProcessBuilder()
                    .command("su")
                    .redirectErrorStream(true).start();
	    }catch(IOException exception1){
	            exception1.printStackTrace();
	    }
        }
	OutputStream out = p.getOutputStream();
	command+="\n\n";
	try{
		out.write(command.getBytes());	
	}catch(IOException e){}
	return p;
    }
    public static Process sudo(String...strings) {
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            for (String s : strings) {
                outputStream.writeBytes(s+"\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outputStream.close();
            return su;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
