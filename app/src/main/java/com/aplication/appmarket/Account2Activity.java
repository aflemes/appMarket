package com.aplication.appmarket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class Account2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String NameUser;
    private String EmailUser;
    private String TokenUser;

    private EditText txtNome;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            NameUser = extras.getString("Nome");
            EmailUser = extras.getString("Email");
            TokenUser = extras.getString("Token");

            txtNome = (EditText) findViewById(R.id.txtNomeAcount);

            if (txtNome != null && NameUser != null) {
                txtNome.setText(NameUser);
                txtNome.setKeyListener(null);
            }

            txtEmail = (EditText) findViewById(R.id.txtEmailAcount);

            if (txtEmail != null && EmailUser != null){
                txtEmail.setText(EmailUser);
                txtEmail.setKeyListener(null);
            }
        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;

        switch (id){
            case R.id.nav_home:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                break;
            case R.id.nav_account:
                intent = new Intent(getApplicationContext(), Account2Activity.class);
                break;
            case R.id.nav_sell:
                intent = new Intent(getApplicationContext(), Sell2Activity.class);
                break;
            case R.id.nav_compras:
                intent = new Intent(getApplicationContext(), ShoppingActivity.class);
                break;
            case R.id.nav_quit:
                logoff();
                return true;
        }

        openActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openActivity(Intent intent){
        intent.putExtra("Nome" ,NameUser);
        intent.putExtra("Email",EmailUser);
        intent.putExtra("Token",TokenUser);

        startActivity(intent);
    }

    private void logoff(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Você tem certeza?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                openLogin();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
