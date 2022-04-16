package in.ashrafsiddiqui.picams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class CamActivity extends AppCompatActivity {

    String alias, ip, web_port, ssh_port, uname, pass;
    WebView CamView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        alias = getIntent().getExtras().getString("alias");
        uname = getIntent().getExtras().getString("uname");
        pass = getIntent().getExtras().getString("pass");
        ip = getIntent().getExtras().getString("ip");
        web_port = getIntent().getExtras().getString("web_port");
        ssh_port = getIntent().getExtras().getString("ssh_port");

        ActionBar ab = getSupportActionBar();
        if(ab!=null){
            ab.setTitle(alias + " Live Feed!");
        }

        CamView = findViewById(R.id.CamView);

        CamView.setWebViewClient(new WebViewClient());
        CamView.getSettings().setLoadWithOverviewMode(true);
        CamView.getSettings().setUseWideViewPort(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CamView.loadUrl("http://" + ip + ":" + web_port + "/index.html");
        if(CamView.getParent() != null){
            ((ViewGroup)CamView.getParent()).removeView(CamView);
        }
        setContentView(CamView);
}

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    boolean result = SSHCommand.accessCam(uname, pass, ip, Integer.parseInt(ssh_port), 0);
                    if(result){
                        System.out.println("Stopped Cam");
                    }else{
                        System.out.println("ERROR Stopping Cam");
                    }
                }catch (Exception e){
                    System.out.println("Exception Stopping Cam");
                }
                return null;
            }
        }.execute(1);
        finish();
    }
}