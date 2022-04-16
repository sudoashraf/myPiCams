package in.ashrafsiddiqui.picams;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.jcraft.jsch.Session;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class EditActivity extends AppCompatActivity {

    EditText alias_edit, ip_edit, webport_edit, sshport_edit, uname_edit, pass_edit;
    Button updateBtn, deleteBtn, testBtn, connectBtn;
    Switch sw;
    String id, alias, ip, web_port, ssh_port, uname, pass;
    public TextView testStatus_edit, openingCam;
    MyDatabaseHelper myDB = new MyDatabaseHelper(EditActivity.this);

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        alias_edit = findViewById(R.id.alias_edit);
        ip_edit = findViewById(R.id.ip_edit);
        webport_edit = findViewById(R.id.web_port_edit);
        sshport_edit = findViewById(R.id.ssh_port_edit);
        uname_edit = findViewById(R.id.uname_edit);
        pass_edit = findViewById(R.id.pass_edit);
        sw = findViewById(R.id.showHide_edit);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        testBtn = findViewById(R.id.testBtn_edit);
        testStatus_edit = findViewById(R.id.testStatus_edit);
        connectBtn = findViewById(R.id.connectBtn);
        openingCam = findViewById(R.id.openingCam);

        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if(ab!=null){
            ab.setTitle(alias);
        }

        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                pass_edit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                sw.setText(R.string.hide);
            } else {
                pass_edit.setInputType(129);
                sw.setText(R.string.show);
            }
        });

        updateBtn.setOnClickListener(view -> {
            alias = alias_edit.getText().toString().trim();
            ip = ip_edit.getText().toString().trim();
            web_port = webport_edit.getText().toString().trim();
            ssh_port = sshport_edit.getText().toString().trim();
            uname = uname_edit.getText().toString().trim();
            pass = pass_edit.getText().toString().trim();
            boolean result = readInputs(alias, ip, web_port, ssh_port, uname, pass);
            if(result){
                myDB.updateData(id, alias, ip, web_port, ssh_port, uname, pass);
            }
        });

        deleteBtn.setOnClickListener(view -> confirmDialog());

        testBtn.setOnClickListener(view -> {
            alias = alias_edit.getText().toString().trim();
            ip = ip_edit.getText().toString().trim();
            web_port = webport_edit.getText().toString().trim();
            ssh_port = sshport_edit.getText().toString().trim();
            uname = uname_edit.getText().toString().trim();
            pass = pass_edit.getText().toString().trim();
            boolean result = readInputs(alias, ip, web_port, ssh_port, uname, pass);
            testStatus_edit.setVisibility(View.INVISIBLE);
            if(result){
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            Session connResult = SSHCommand.testConn(uname, pass, ip, Integer.parseInt(ssh_port));
                            runOnUiThread(() -> {
                                if(connResult != null){
                                    testStatus_edit.setText(R.string.connSuccess);
                                    testStatus_edit.setTextColor(Color.parseColor("#6CC04A"));
                                }else {
                                    testStatus_edit.setText(R.string.connFailed);
                                    testStatus_edit.setTextColor(Color.parseColor("#C51A4A"));
                                }
                                testStatus_edit.setVisibility(View.VISIBLE);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }else Toast.makeText(this, "Cannot connect", Toast.LENGTH_SHORT).show();
        });

        connectBtn.setOnClickListener(view -> {
            alias = alias_edit.getText().toString().trim();
            ip = ip_edit.getText().toString().trim();
            web_port = webport_edit.getText().toString().trim();
            ssh_port = sshport_edit.getText().toString().trim();
            uname = uname_edit.getText().toString().trim();
            pass = pass_edit.getText().toString().trim();
            boolean result = readInputs(alias, ip, web_port, ssh_port, uname, pass);
            testStatus_edit.setVisibility(View.INVISIBLE);
            if(result){
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            Session connResult = SSHCommand.testConn(uname, pass, ip, Integer.parseInt(ssh_port));
                            runOnUiThread(() -> {
                                if(connResult != null){
                                    testStatus_edit.setText(R.string.connSuccess);
                                    testStatus_edit.setTextColor(Color.parseColor("#6CC04A"));
                                    openingCam.setVisibility(View.VISIBLE);
                                    Intent camIntent = new Intent(getApplicationContext(), CamActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString("alias", alias);
                                    b.putString("uname", uname);
                                    b.putString("pass", pass);
                                    b.putString("ip", ip);
                                    b.putString("web_port", web_port);
                                    b.putString("ssh_port", ssh_port);
                                    camIntent.putExtras(b);
                                    new AsyncTask<Integer, Void, Void>(){
                                        @Override
                                        protected Void doInBackground(Integer... params) {
                                            try {
                                                boolean connResult = SSHCommand.accessCam(uname, pass, ip, Integer.parseInt(ssh_port), 1);
                                                if(connResult){
                                                    startActivity(camIntent);
                                                }else{
                                                    System.out.println("ERROR Starting Cam");
                                                }
                                            }catch (Exception e){
                                                System.out.println("Exception Starting Cam");
                                            }
                                            return null;
                                        }
                                    }.execute(1);
                                }else {
                                    testStatus_edit.setText(R.string.connFailed);
                                    testStatus_edit.setTextColor(Color.parseColor("#C51A4A"));
                                }
                                testStatus_edit.setVisibility(View.VISIBLE);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }else Toast.makeText(this, "Cannot connect", Toast.LENGTH_SHORT).show();
        });
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("alias") &&
                getIntent().hasExtra("ip") && getIntent().hasExtra("web_port") &&
                getIntent().hasExtra("ssh_port") && getIntent().hasExtra("uname") &&
                getIntent().hasExtra("pass")){
            id = getIntent().getStringExtra("id");
            alias = getIntent().getStringExtra("alias");
            ip = getIntent().getStringExtra("ip");
            web_port = getIntent().getStringExtra("web_port");
            ssh_port = getIntent().getStringExtra("ssh_port");
            uname = getIntent().getStringExtra("uname");
            pass = getIntent().getStringExtra("pass");
            alias_edit.setText(alias);
            ip_edit.setText(ip);
            webport_edit.setText(web_port);
            sshport_edit.setText(ssh_port);
            uname_edit.setText(uname);
            pass_edit.setText(pass);
        }else{
            Toast.makeText(this, "No Data.", Toast.LENGTH_SHORT).show();
        }
    }

    boolean readInputs(String alias, String ip, String web_port, String ssh_port, String uname, String pass){
        if(alias.isEmpty() || ip.isEmpty()  || web_port.isEmpty()  || ssh_port.isEmpty()  || uname.isEmpty()  || pass.isEmpty()){
            Toast.makeText(EditActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!Patterns.IP_ADDRESS.matcher(ip).matches()){
            Toast.makeText(EditActivity.this, "Invalid IP Address.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(0 > Integer.parseInt(web_port) || Integer.parseInt(web_port) > 65535){
            Toast.makeText(EditActivity.this, "Invalid Web Port Number.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(0 > Integer.parseInt(ssh_port) || Integer.parseInt(ssh_port) > 65535){
            Toast.makeText(EditActivity.this, "Invalid SSH Port Number.", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+ alias + "?");
        builder.setMessage("Are you sure you want to delete " + alias +"?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(EditActivity.this);
            myDB.deleteOneRow(id);
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
        });
        builder.create().show();
    }
}