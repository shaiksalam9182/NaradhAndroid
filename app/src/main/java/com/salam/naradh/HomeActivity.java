package com.salam.naradh;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvName;
    SharedPreferences sd;
    SharedPreferences.Editor editor;
    String username;
    boolean transactionDone = false;
    boolean backpressed = false;
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sd = getSharedPreferences("Naradh", Context.MODE_PRIVATE);
        editor = sd.edit();

        username = sd.getString("name","");
        email = sd.getString("email","");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setTitle("Home");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        tvName = (TextView)headerView.findViewById(R.id.tv_name);
        tvName.setText(username);

        FragmentManager fm = getSupportFragmentManager();
        MainFragment mf =new MainFragment();
        fm.beginTransaction().replace(R.id.content_main,mf,mf.getTag()).commitAllowingStateLoss();

        navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);

        if (email.equalsIgnoreCase("")){
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            if (transactionDone){
                FragmentManager fm = getSupportFragmentManager();
                MainFragment mf =new MainFragment();
                transactionDone = false;
                fm.beginTransaction().replace(R.id.content_main,mf,mf.getTag()).commitAllowingStateLoss();

            }else {
                if (backpressed){
                    super.onBackPressed();
                }else {
                    backpressed = true;
                    Toast.makeText(HomeActivity.this,"Press once again to exit",Toast.LENGTH_LONG).show();
                }

            }
        }
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fm = getSupportFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            Log.e("emaildetails",email);
            if (email.equalsIgnoreCase("")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Please login to access account section");
                builder.setCancelable(false);
                builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        dialogInterface.dismiss();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }else {
                AccountFragment af = new AccountFragment();
                fm.beginTransaction().replace(R.id.content_main,af,af.getTag()).commitAllowingStateLoss();
                transactionDone = true;
            }



        } else if (id == R.id.nav_notifications) {

            NotificationsFragment nf =new NotificationsFragment();
            fm.beginTransaction().replace(R.id.content_main,nf,nf.getTag()).commitAllowingStateLoss();
            transactionDone = true;

        } else if (id == R.id.nav_feedback) {

            if (email.equalsIgnoreCase("")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Please login to serve you better");
                builder.setCancelable(false);
                builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        dialogInterface.dismiss();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }else {
                FeedbackFragment ff = new FeedbackFragment();
                fm.beginTransaction().replace(R.id.content_main,ff,ff.getTag()).commitAllowingStateLoss();

                transactionDone = true;
            }



        } else if (id == R.id.nav_logout) {

            editor.putString("email","");
            editor.putString("token","");
            editor.putString("name","");
            editor.commit();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            finish();

        }else if (id==R.id.nav_home){
            MainFragment mf =new MainFragment();
            fm.beginTransaction().replace(R.id.content_main,mf,mf.getTag()).commitAllowingStateLoss();
        }else if (id==R.id.nav_login){
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
