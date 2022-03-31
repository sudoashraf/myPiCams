package in.ashrafsiddiqui.picams;

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

    EditText alias_edit, ip_edit, port_edit, uname_edit, pass_edit;
    Button updateBtn, deleteBtn, testBtn, connectBtn;
    Switch sw;
    String id, alias, ip, port, uname, pass;
    TextView testStatus_edit;
    MyDatabaseHelper myDB = new MyDatabaseHelper(EditActivity.this);

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        alias_edit = findViewById(R.id.alias_edit);
        ip_edit = findViewById(R.id.ip_edit);
        port_edit = findViewById(R.id.port_edit);
        uname_edit = findViewById(R.id.uname_edit);
        pass_edit = findViewById(R.id.pass_edit);
        sw = findViewById(R.id.showHide_edit);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        testBtn = findViewById(R.id.testBtn_edit);
        testStatus_edit = findViewById(R.id.testStatus_edit);
        connectBtn = findViewById(R.id.connectBtn);

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
            port = port_edit.getText().toString().trim();
            uname = uname_edit.getText().toString().trim();
            pass = pass_edit.getText().toString().trim();
            boolean result = readInputs(alias, ip, port, uname, pass);
            if(result){
                myDB.updateData(id, alias, ip, port, uname, pass);
            }
        });

        deleteBtn.setOnClickListener(view -> confirmDialog());

        testBtn.setOnClickListener(view -> {
            alias = alias_edit.getText().toString().trim();
            ip = ip_edit.getText().toString().trim();
            port = port_edit.getText().toString().trim();
            uname = uname_edit.getText().toString().trim();
            pass = pass_edit.getText().toString().trim();
            boolean result = readInputs(alias, ip, port, uname, pass);
            System.out.println(result);
            testStatus_edit.setVisibility(View.INVISIBLE);
            if(result){
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            Session connResult = SSHCommand.testConn(uname, pass, ip);
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
            Intent camIntent = new Intent(getApplicationContext(), CamActivity.class);
            Bundle b = new Bundle();
            alias = alias_edit.getText().toString().trim();
            ip = ip_edit.getText().toString().trim();
            port = port_edit.getText().toString().trim();
            uname = uname_edit.getText().toString().trim();
            pass = pass_edit.getText().toString().trim();
            b.putString("alias", alias);
            b.putString("uname", uname);
            b.putString("pass", pass);
            b.putString("ip", ip);
            b.putString("port", port);
            camIntent.putExtras(b);
            new AsyncTask<Integer, Void, Void>(){
                @Override
                protected Void doInBackground(Integer... params) {
                    try {
                        boolean result = SSHCommand.accessCam(uname, pass, ip, 1);
                        if(result){
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
        });
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("alias") &&
                getIntent().hasExtra("ip") && getIntent().hasExtra("port") &&
                getIntent().hasExtra("uname") && getIntent().hasExtra("pass")){

            id = getIntent().getStringExtra("id");
            alias = getIntent().getStringExtra("alias");
            ip = getIntent().getStringExtra("ip");
            port = getIntent().getStringExtra("port");
            uname = getIntent().getStringExtra("uname");
            pass = getIntent().getStringExtra("pass");

            alias_edit.setText(alias);
            ip_edit.setText(ip);
            port_edit.setText(port);
            uname_edit.setText(uname);
            pass_edit.setText(pass);

        }else{
            Toast.makeText(this, "No Data.", Toast.LENGTH_SHORT).show();
        }
    }

    boolean readInputs(String alias, String ip, String port, String uname, String pass){

        if(alias.isEmpty() || ip.isEmpty()  || port.isEmpty()  || uname.isEmpty()  || pass.isEmpty()){
            Toast.makeText(EditActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!Patterns.IP_ADDRESS.matcher(ip).matches()){
            Toast.makeText(EditActivity.this, "Invalid IP Address.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(0 > Integer.parseInt(port) || Integer.parseInt(port) > 65535){
            Toast.makeText(EditActivity.this, "Invalid Port Number.", Toast.LENGTH_SHORT).show();
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