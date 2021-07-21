package com.livingstonei2p.yggdrasilroot;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import static com.livingstonei2p.yggdrasilroot.shell.sudo;

import static com.livingstonei2p.yggdrasilroot.shell.run_exec;

public class yggdrasil {
    final static String MainDirectoryYggStr="/sdcard/yggdrasil";
    final static String YggConfPath = MainDirectoryYggStr+"/yggdrasil.conf";
    static void kill(){
	sudo("pkill -9 yggdrasil");
}
    static void install() {

        File confFile = new File(YggConfPath);
        if(!confFile.exists()){
            sudo("YGG","create config");
            sudo(MainActivity.PathFiles+"/yggdrasil -genconf > "+YggConfPath );
        }
//      Log.d("YGG","Run "+MainActivity.PathFiles+"/yggdrasil -useconffile "+YggConfPath);
//	sudo("cp "+MainActivity.PathFiles+"/yggdrasil /system/bin/");
//	sudo("chmod 755 /system/bin/yggdrasil");
    }
    static Process run() throws IOException{
	return sudo("nohup "+MainActivity.PathFiles+"/yggdrasil -logto syslog -useconffile "+YggConfPath+">/sdcard/yggdrasil/log&");	
    }
    static boolean prepareDirectories(){
       if(  sudo("mount -o remount,rw /","mkdir -p /var/run") == null)return false;
        sudo("ls /dev/net/tun || mkdir -p /dev/net");
        sudo("ls /dev/tun && ln -s /dev/tun /dev/net/tun");
        return true;
    }
}
