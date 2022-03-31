package in.ashrafsiddiqui.picams;

import android.annotation.SuppressLint;
import android.database.Cursor;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.jcraft.jsch.Session;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class AddActivity extends AppCompatActivity {

    EditText alias_input, ip_input, port_input, uname_input, pass_input;
    TextView testStatus;
    Switch sw;
    Button addBtn, testBtn;
    String alias, ip, port, uname, pass;
    MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        alias_input = findViewById(R.id.alias_input);
        ip_input = findViewById(R.id.ip_input);
        port_input = findViewById(R.id.port_input);
        uname_input = findViewById(R.id.uname_input);
        pass_input = findViewById(R.id.pass_input);
        sw = findViewById(R.id.showHide);
        addBtn = findViewById(R.id.addBtn);
        testBtn = findViewById(R.id.testBtn);
        testStatus = findViewById(R.id.testStatus);

        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                pass_input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                sw.setText(R.string.hide);
            } else {
                pass_input.setInputType(129);
                sw.setText(R.string.show);
            }
        });

        addBtn.setOnClickListener(view -> {
            alias = alias_input.getText().toString().trim();
            ip = ip_input.getText().toString().trim();
            port = port_input.getText().toString().trim();
            uname = uname_input.getText().toString().trim();
            pass = pass_input.getText().toString().trim();
            boolean result = readInputs(alias, ip, port, uname, pass);
            if(result){ notifyDialog(); }
        });

        testBtn.setOnClickListener(view -> {
            alias = alias_input.getText().toString().trim();
            ip = ip_input.getText().toString().trim();
            port = port_input.getText().toString().trim();
            uname = uname_input.getText().toString().trim();
            pass = pass_input.getText().toString().trim();
            boolean result = readInputs(alias, ip, port, uname, pass);
            testStatus.setVisibility(View.INVISIBLE);
            if(result){
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            Session connResult = SSHCommand.testConn(uname, pass, ip);
                            runOnUiThread(() -> {
                                if(connResult != null){
                                    testStatus.setText(R.string.connSuccess);
                                    testStatus.setTextColor(Color.parseColor("#6CC04A"));
                                }else {
                                    testStatus.setText(R.string.connFailed);
                                    testStatus.setTextColor(Color.parseColor("#C51A4A"));
                                }
                                testStatus.setVisibility(View.VISIBLE);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
        });
    }

    boolean readInputs(String alias, String ip, String port, String uname, String pass){

        if(alias.isEmpty() || ip.isEmpty()  || port.isEmpty()  || uname.isEmpty()  || pass.isEmpty()){
            Toast.makeText(AddActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!Patterns.IP_ADDRESS.matcher(ip).matches()){
            Toast.makeText(AddActivity.this, "Invalid IP Address.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(0 > Integer.parseInt(port) || Integer.parseInt(port) > 65535){
            Toast.makeText(AddActivity.this, "Invalid Port Number.", Toast.LENGTH_SHORT).show();
            return false;
        }else return uniqueData(alias, ip, port, uname, pass);
    }

    boolean uniqueData(String alias, String ip, String port, String uname, String pass) {
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount()==0){
            return true;
        }else{
            while(cursor.moveToNext()){
                if(alias.equals(cursor.getString(1)) && ip.equals(cursor.getString(2)) &&
                        port.equals(cursor.getString(3)) && uname.equals(cursor.getString(4)) &&
                        pass.equals(cursor.getString(5))){
                    Toast.makeText(AddActivity.this, "Cam with same details already exists.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    void notifyDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create "+ alias + "?");
        builder.setMessage("This will create a new Cam " + alias);
        builder.setPositiveButton("Yes", (dialogInterface, i) -> myDB.addPiCam(alias, ip, port, uname, pass));
        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.create().show();
    }
}