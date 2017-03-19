package com.example.gavin.applocker;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.app.Activity;
import android.widget.SearchView;
import android.test.ApplicationTestCase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
import static java.lang.Integer.MAX_VALUE;
import static java.security.AccessController.getContext;


public class MainActivity extends Activity {
    ListView lv;
    SearchView sv;
    Model[] appList;
    Model[] searchResult;
    Model[] searchResult2;
    boolean search =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Show alert dialog to the user saying a separate permission is needed
            // Launch the settings activity if the user prefers
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(myIntent);
        } else {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent); }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listView1);
        sv = (SearchView) findViewById(R.id.searchView1);

        final PackageManager pm = getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        List<PackageInfo> installedApps = new ArrayList<PackageInfo>();
        //ActivityManager activity_manager = (ActivityManager) this.getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);

        for(PackageInfo pack : packages) {
            //checks for flags; if flagged, check if updated system app
            if((pack.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                installedApps.add(pack);
                //it's a system app, not interested
            } else if ((pack.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //Discard this one
                //in this case, it should be a user-installed app
            } else if (pack.applicationInfo.loadLabel(pm).toString().contains("com.android")) {
                // discard
            } else if (pack.applicationInfo.loadLabel(pm).toString().equalsIgnoreCase("Applocker")) {
                // discard Applock
            } else {
                installedApps.add(pack);
            }
        }


        Collections.sort(installedApps, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo s1, PackageInfo s2) {
                return s1.applicationInfo.loadLabel(pm).toString().compareToIgnoreCase(s2.applicationInfo.loadLabel(pm).toString());
            }
        });

//        pm.setApplicationEnabledSetting(installedApps);
        appList = new Model[installedApps.size()];
        int i = 0;
        while (i < installedApps.size()) {
            //Log.d("apps", installedApps.get(i).applicationInfo.loadLabel(pm).toString());
            appList[i]= new Model(installedApps.get(i).applicationInfo.loadLabel(pm).toString(),i,pm.getApplicationIcon(installedApps.get(i).applicationInfo),1, installedApps.get(i));
            i++;
        }

        for (i = 0; i < appList.length; i++) {
            appList[i].setValue(load(i));
            //Log.d("load", Integer.toString(appList[i].getValue()));
//            if (appList[i].getValue() == 1) {
//                ComponentName componentName = new ComponentName(appList[i].getInfo()., com.apps.MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
//                p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//            }
//            else {
//                pm.setApplicationEnabledSetting(appList[i].getInfo().packageName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
//            }
        }

        CustomAdapter adapter = new CustomAdapter(this, appList);
        lv.setAdapter(adapter);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query != null){
                    runSearch(query);
                    return true;
                }
                else {
                    return false;
                }
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });
        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                repopulate();
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onPause() {
        for (int i = 0; i < appList.length; i++) {
            save(i, appList[i].getValue());
            //Log.d("save", Integer.toString(appList[i].getValue()));
        }

        (new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {
                            @Override
                            public void run()
                            {
                                //Log.d("current app","1");
                                //Log.d("current app",getTopAppName(getBaseContext()));
                                for (int i = 0; i < appList.length; i++) {
                                    String name = appList[i].getName().replaceAll("\\s+","").toLowerCase();
                                    Log.d(Integer.toString(appList[i].getValue()),name);
                                    if (getTopAppName(getBaseContext()).contains(name) && appList[i].getValue() == 1) {
//                                        int duration = Toast.LENGTH_SHORT;
//                                        Toast toast = Toast.makeText(getApplicationContext(), appList[i].getName() + " is locked", duration);
//                                        toast.show();

                                        AlertDialog alert = Alerts.locked(MainActivity.this);
                                        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                        alert.show();

//                                        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                                        List<ActivityManager.RunningServiceInfo> appProcessInfoList = mActivityManager.getRunningServices(Integer.MAX_VALUE);
//                                        for (int j = 0; j < appProcessInfoList.size(); j++) {
//                                            Log.d(appProcessInfoList.get(j).process,name);
//                                            if (appProcessInfoList.get(j).process.contains(name)) {
//                                                Process.sendSignal(appProcessInfoList.get(j).pid, Process.SIGNAL_QUIT);
//                                            }
//                                            break;
//                                        }
                                    }
                                }

                            }
                        });
                    }
                    catch (InterruptedException e)
                    {
                        // ooops
                    }
            }
        })).start(); // the while thread will start in BG thread

        super.onPause();
    }

    private void save(int index, int value) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("check" + index, value);
        editor.commit();
    }

    private int load(int index) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getInt("check" + index, 0);
    }

    public static String getTopAppName(Context context) {
        String strName = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                strName = getLollipopFGAppPackageName(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strName;
    }

    private static String getLollipopFGAppPackageName(Context ctx) {

        try {
            UsageStatsManager usageStatsManager = (UsageStatsManager) ctx.getSystemService(USAGE_STATS_SERVICE);
            long milliSecs = 60 * 1000;
            Date date = new Date();
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, date.getTime() - milliSecs, date.getTime());
            if (queryUsageStats.size() > 0) {
                //Log.i("LPU", "queryUsageStats size: " + queryUsageStats.size());
            }
            long recentTime = 0;
            String recentPkg = "";
            for (int i = 0; i < queryUsageStats.size(); i++) {
                UsageStats stats = queryUsageStats.get(i);
                if (i == 0 && !"org.pervacio.pvadiag".equals(stats.getPackageName())) {
                    //Log.i("LPU", "PackageName: " + stats.getPackageName() + " " + stats.getLastTimeStamp());
                }
                if (stats.getLastTimeStamp() > recentTime) {
                    recentTime = stats.getLastTimeStamp();
                    recentPkg = stats.getPackageName();
                }
            }
            return recentPkg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void repopulate(){
        search = false;
        CustomAdapter adapter = new CustomAdapter(this, appList);
        lv.setAdapter(adapter);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void runSearch(String s){
        search = true;
        searchResult = new Model[appList.length];
        int offset = 0;
        int newSize=0;
        for(int i = 0; i <(appList.length-offset); i++){
            if(appList[i+offset].getName().toLowerCase().contains(s.toLowerCase())){
                searchResult[i]= new Model(appList[i+offset].getName(),i+offset,appList[i+offset].getIcon(),appList[i+offset].getValue(),appList[i+offset].getInfo());
            }
            else{
                i--;
                offset++;
            }
            newSize=i;
        }
        searchResult2 = new Model[newSize+1];
        for(int i=0; i<newSize+1; i++){
            searchResult2[i]=searchResult[i];
        }
        CustomAdapter newadapter = new CustomAdapter(this, searchResult2);
        lv.setAdapter(newadapter);
    }

    public void clearAll(View v) {
        if(search == false) {
            for (int i = 0; i < appList.length; i++) {
                appList[i].setValue(0);
                final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
                ListView lvout = (ListView) viewGroup.getChildAt(2); //if add anything ontop of listview need to increase 2 by 1 for every item added.
                LinearLayout llout = (LinearLayout) lvout.getChildAt(i);
                CheckBox check = (CheckBox) llout.getChildAt(0);
                check.setChecked(false);
            }
        }
        else{
            for (int i = 0; i < searchResult2.length; i++) {
                searchResult2[i].setValue(0);
                final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
                ListView lvout = (ListView) viewGroup.getChildAt(2); //if add anything ontop of listview need to increase 2 by 1 for every item added.
                LinearLayout llout = (LinearLayout) lvout.getChildAt(i);
                CheckBox check = (CheckBox) llout.getChildAt(0);
                check.setChecked(false);
            }
        }
    }
}