package io.virtualapp.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.remote.InstalledAppInfo;

import java.util.ArrayList;
import java.util.List;

import io.virtualapp.R;
import io.virtualapp.abs.ui.VActivity;
import io.virtualapp.settings.SettingsActivity;

public class MainActivity extends VActivity {

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<InstallApp> list = new ArrayList<>();

        for (InstalledAppInfo app : VirtualCore.get().getInstalledApps(0)) {
            try {
                PackageManager pm = VirtualCore.get().getPackageManager();
                ApplicationInfo info = VirtualCore.get().getPackageManager().getApplicationInfo(app.packageName, 0);
                String lable = info.loadLabel(pm).toString();
                list.add(new InstallApp(app.packageName, lable));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        listView = findViewById(R.id.listview);
        listView.setAdapter(new MAdapter(list, this));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            InstallApp app = list.get(position);
            LoadingActivity.launch(this, app.packageName, 0);
        });
    }

    public void setting(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}

class InstallApp {
    public String packageName;
    public String name;

    public InstallApp(String packageName, String name) {
        this.packageName = packageName;
        this.name = name;
    }
}

class MAdapter extends BaseAdapter {

    private List<InstallApp> mList = new ArrayList<>();
    private LayoutInflater inflater;

    public MAdapter(List<InstallApp> list, Context context) {
        mList.addAll(list);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_test, parent, false);
        }
        InstallApp app = mList.get(position);
        ((TextView) view.findViewById(R.id.name)).setText(app.name);
        return view;
    }
}