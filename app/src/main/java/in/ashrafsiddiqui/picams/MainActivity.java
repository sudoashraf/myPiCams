package in.ashrafsiddiqui.picams;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton createBtn;
    MyDatabaseHelper myDB;
    ArrayList<String> id,alias,ip,port,uname,pass;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        createBtn = findViewById(R.id.createBtn);

        Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
        createBtn.setOnClickListener(view -> startActivity(addIntent));

        myDB = new MyDatabaseHelper(MainActivity.this);
        id = new ArrayList<>();
        alias = new ArrayList<>();
        ip = new ArrayList<>();
        port = new ArrayList<>();
        uname = new ArrayList<>();
        pass = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(MainActivity.this,this, id, alias, ip, port, uname, pass);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recreate();
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Pi Cams Found.", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                alias.add(cursor.getString(1));
                ip.add(cursor.getString(2));
                port.add(cursor.getString(3));
                uname.add(cursor.getString(4));
                pass.add(cursor.getString(5));
            }
        }
    }
}