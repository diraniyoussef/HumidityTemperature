package com.youssefdirani.HumTemp_v04;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import android.view.Menu;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    //private AppBarConfiguration mAppBarConfiguration;
    final public Toasting toasting = new Toasting( this );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        /* //I commented these just to remove the menu on the top left
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        */
    }

    protected void onResume() {
        super.onResume();

        final ToggleButton tb = findViewById(R.id.toggleButton_internet_local);
        connect(tb);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("humtemp main", "inside tb setonclickListener");
                connect(tb);
            }
        });
        /* //this piece of code didn't work when panel_index was a public variable in HomeFragment
        HomeFragment fragment_home = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.nav_home);
        if( fragment_home != null ) {
            Log.i("Youssef", "fragment accessible and panel index is " + fragment_home.panel_index);
        } else {
            Log.i("Youssef", "fragment is null");
        }
         */
    }

    public MutableLiveData<String> localNotInternet = new MutableLiveData<>(); //this is to propagate the value to the fragment
    public String owner_part = "wehbe";
    //String mob_part = "S7_Edge";
    //String mob_part = "S4";
    public String mob_part = "mob1"; //usr2 is kept in the store
    //String mob_part = "Mom_Tab";
    public String mob_Id = owner_part + ":" + mob_part + ":";

    private void connect( ToggleButton tb ) {
        if( tb == null ) return;
        if(tb.isChecked()) { //internet
            localNotInternet.setValue("internet");
            tb.setTextColor( getTextColorPrimary() );
            Log.i("HumTemp MainAct", "trying local connection");
        } else {//local
            localNotInternet.setValue("local");
            tb.setTextColor(Color.parseColor("#D81B60"));
            Log.i("HumTemp MainAct", "trying internet connection");
        }
    }

    private int getTextColorPrimary() { //getting text_color_primary. I could had simply searched for the color number, but it's ok.
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray arr = this.obtainStyledAttributes(typedValue.data, new int[]{
                android.R.attr.textColorPrimary
        });
        int primaryColor = arr.getColor(0, -1);
        arr.recycle();
        return primaryColor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //This is for the menu on the top right I guess
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public final String panel_index = "charcoal_humidity_panel"; /*this shouldn't be here. Better be in the fragment. but ok.
        I didn't know how to access the fragment, though I tried.
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about_app:
                startActivity(new Intent(getApplicationContext(), AppDescription.class));
                return true;
            case R.id.configuration:
                final Intent intent = new Intent();
                final String  other_panel_index = "-1"; //I can make it any value but -1 is preferred
                intent.putExtra("panelName", getString(R.string.app_name) );
                intent.putExtra("panelIndex", panel_index ); //panel index will be used to set the static IP.
                //if the panel index corresponds to this "other panel" we won't assign then any static IP.
                intent.putExtra("otherPanelIndex", other_panel_index ); /*used to compare the panel index with
                    this extra panel index*/
                intent.setClass(getApplicationContext(), ConfigPanel.class);
                startActivity(intent);
                /*
                //I added this code but it crashes with an NPE
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                Fragment fragment_send = getSupportFragmentManager().findFragmentById(R.id.nav_send);
                HomeFragment fragment_home = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.nav_home);
                ft.hide(fragment_home);
                ft.show(fragment_send);
                ft.commit();
                */
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
    /*
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

     */
        return false; //I added this
    }


}

