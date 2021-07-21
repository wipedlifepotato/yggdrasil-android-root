package com.livingstonei2p.yggdrasilroot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

import com.livingstonei2p.yggdrasilroot.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import android.widget.Button;
import java.io.FileInputStream;

import static com.livingstonei2p.yggdrasilroot.shell.sudo;

public class MainActivity extends AppCompatActivity {
    private Thread logger;
    private ContextWrapper c;
    private Process yggProc;
    static String PathFiles;
    static String logBuf;
//    BufferedReader Logger;
    File MainDirectoryYgg = new File(yggdrasil.MainDirectoryYggStr);
    void installXD() throws IOException {
            AssetManager am = this.getApplicationContext().getAssets();
            InputStream yggdrasil_bin = am.open("yggdrasil");
            File outFile = new File(PathFiles + "/yggdrasil");
            OutputStream out = new FileOutputStream(outFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = yggdrasil_bin.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            outFile.setExecutable(true);
            Runtime.getRuntime().exec("chmod 7555"+PathFiles+"/yggdrasil");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        c = new ContextWrapper(this);
        PathFiles = c.getFilesDir().getPath();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView status_ygg = findViewById(R.id.yggstatus);
	Button killBut = findViewById(R.id.killer);
	killBut.setOnClickListener(new View.OnClickListener() {
    	@Override
    	public void onClick(View v) {
        	yggdrasil.kill();
		//this.finishAffinity();
		finishAndRemoveTask();
    	}
	});
        try {
            installXD();
        }catch(Exception e){
            status_ygg.setText("yggdrasil bin not installed");
            return;
        }
        try {
            if (
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.INTERNET},
                        1);
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_NETWORK_STATE},
                        1);
            }

        } catch (Throwable e) {
            status_ygg.setText(e.toString());
        }
        //
        Process s = null;
        try {
            if(MainDirectoryYgg.mkdir()){
                status_ygg.setText("Init directories");
            }else{
                status_ygg.setText("Cant create directory: "+yggdrasil.MainDirectoryYggStr);
                s = sudo("mkdir /sdcard/"+yggdrasil.MainDirectoryYggStr);
            }
        }catch(Exception e){
            status_ygg.setText("break... break... can't create /sdcard/"+yggdrasil.MainDirectoryYggStr);
            if(s != null ) status_ygg.setText("Created yet;");
        }
        if(yggdrasil.prepareDirectories()){
	    yggdrasil.install();
            try {
                yggProc = yggdrasil.run();
                while (yggProc == null) {
                    yggProc = yggdrasil.run();
                    status_ygg.setText("Can't run yggdrasil");
                }
                status_ygg.setText("");
            }catch(Exception e){
                status_ygg.setText(e.toString());
            }
           // Logger = new BufferedReader(new InputStreamReader(yggProc.getInputStream()));
	     
            logger = new Thread(new Runnable() {
                public void run() {
                }
            });
           // logger.start();

        }else{
            status_ygg.setText("cant prepare dirs for yggdrasil");
        }
    }
}
