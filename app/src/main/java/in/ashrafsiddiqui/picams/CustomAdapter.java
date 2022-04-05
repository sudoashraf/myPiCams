package in.ashrafsiddiqui.picams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final Context context;
    Activity activity;
    private ArrayList id, alias, ip, web_port, ssh_port, uname, pass;

    CustomAdapter(Activity activity, Context context, ArrayList id, ArrayList alias, ArrayList ip, ArrayList web_port,
                  ArrayList ssh_port, ArrayList uname, ArrayList pass){
        this.activity = activity;
        this.context = context;
        this.id = id;
        this.alias = alias;
        this.ip = ip;
        this.web_port = web_port;
        this.ssh_port = ssh_port;
        this.uname = uname;
        this.pass = pass;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.alias_txt.setText(String.valueOf(alias.get(position)));
        holder.ip_txt.setText("IP: " + String.valueOf(ip.get(position)));
        holder.webport_txt.setText("Web: " + String.valueOf(web_port.get(position)));
        holder.sshport_txt.setText("SSH: " + String.valueOf(ssh_port.get(position)));
        holder.mainLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtra("id", String.valueOf(id.get(position)));
            intent.putExtra("alias", String.valueOf(alias.get(position)));
            intent.putExtra("ip", String.valueOf(ip.get(position)));
            intent.putExtra("web_port", String.valueOf(web_port.get(position)));
            intent.putExtra("ssh_port", String.valueOf(ssh_port.get(position)));
            intent.putExtra("uname", String.valueOf(uname.get(position)));
            intent.putExtra("pass", String.valueOf(pass.get(position)));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLayout;
        TextView alias_txt, ip_txt, webport_txt, sshport_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            alias_txt = itemView.findViewById(R.id.alias_txt);
            ip_txt = itemView.findViewById(R.id.ip_txt);
            webport_txt = itemView.findViewById(R.id.webport_txt);
            sshport_txt = itemView.findViewById(R.id.sshport_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
