package com.eaw;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final String url = "http://10.0.2.2/2020_web/public";
    ArrayList<String> navbarURL = new ArrayList<String>();
    ArrayList<String> navbarNAME = new ArrayList<String>();
    ArrayList<String> navbar_account_URL = new ArrayList<String>();
    ArrayList<String> navbar_account_NAME = new ArrayList<String>();
    ArrayList<String> navbar_building_NAME = new ArrayList<String>();
    ArrayList<String> navbar_building_URL = new ArrayList<String>();
    String username;
    Boolean toggleON;
    Menu menu;
    SubMenu subMenu;
    NavigationView navigationView;
    ProgressBar spinner;
    WebView webView;
    ToggleButton accountView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner = (ProgressBar) findViewById(R.id.loadingPanel);
        webView= (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new MyBrowser());
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(true);
        //turn this on if wanted to see the website on zoomed out level
        //webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        textView= (TextView) hView.findViewById(R.id.nav_head_username);

        accountView = (ToggleButton)hView.findViewById(R.id.account_view_icon_button);
        accountView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                menu = navigationView.getMenu();
                menu.clear();
                subMenu.clear();
                toggleON = isChecked;
                if (isChecked){
                    for (int i=0;i<navbar_account_NAME.size();i++) {
                        menu.add(R.id.nav_group,i,Menu.NONE,navbar_account_NAME.get(i));
                    }
                }else{
                    for (int i=0;i<navbarNAME.size();i++) {
                        menu.add(R.id.nav_group,i,Menu.NONE,navbarNAME.get(i));
                    }
                    for (int i=0;i<navbar_building_NAME.size();i++) {
                        subMenu.add(R.id.nav_group,i, Menu.NONE,navbar_building_NAME.get(i));
                    }
                }
            }
        });

        loadURL(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private void loadURL(final String url) {
        spinner.setProgress(0);
        spinner.setVisibility(View.VISIBLE);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                spinner.setProgress(progress);
                if (progress >= 80) {
                    spinner.setVisibility(View.GONE);
                }
            }
        });
        //get data from javascript function
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present. Currently Settings
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (toggleON){
            for (int i=0;i<navbar_account_URL.size();i++){
                if (id==i){
                    loadURL(navbar_account_URL.get(i));
                }
            }
        }else{
            for (int i=0;i<navbarURL.size();i++){
                if (id==i){
                    loadURL(navbarURL.get(i));
                }
            }
            for (int i=0;i<navbar_building_URL.size();i++){
                if (id==i){
                    loadURL(navbar_building_URL.get(i));
                }
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        accountView.setChecked(false);
        return true;
    }

    private class MyBrowser extends WebViewClient {

        public void onPageFinished(WebView view, String url) {
            // do your stuff here
            menu = navigationView.getMenu();
            menu.clear();
            for (int i=0;i<navbarNAME.size();i++) {
                menu.add(R.id.nav_group,i,Menu.NONE,navbarNAME.get(i));
            }
            subMenu = menu.addSubMenu("Buildings");
            subMenu.clear();
            for (int i = 0; i < navbar_building_NAME.size(); i++) {
                subMenu.add(R.id.nav_group,i, subMenu.NONE,navbar_building_NAME.get(i));
            }
            textView.setText(username);
        }
    }

    private class WebAppInterface {
        @JavascriptInterface
        public void sendData(String[] data, String[] data_dropdown, String[] data_building_dropdown) {
            navbarNAME.clear();
            navbarURL.clear();
            navbar_account_NAME.clear();
            navbar_account_URL.clear();
            navbar_building_NAME.clear();
            navbar_building_URL.clear();
            username = data[0];
            System.out.println(data[0]);
            //Get the string value to process
            for (int i=1; i<data.length; i++){
                navbarURL.add(data[i]);
                i++;
                navbarNAME.add(data[i]);
            }
            for (int i=0; i<data_dropdown.length; i++){
                navbar_account_URL.add(data_dropdown[i]);
                i++;
                navbar_account_NAME.add(data_dropdown[i]);
            }
            for (int i=0; i<data_building_dropdown.length; i++){
                navbar_building_URL.add(data_building_dropdown[i]);
                i++;
                navbar_building_NAME.add(data_building_dropdown[i]);
            }
        }
    }
}
