package com.aplication.appmarket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Deal2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView FirstImage;
    private ImageView SecondImage;
    private ImageView ThirdImage;
    private TextView txtTitle;
    private TextView txtQtde;
    private TextView txtCategory;
    private TextView txtDescript;
    private TextView txtPrice;
    private TextView txtAnuncioStatus;
    private Button btnBuy;
    //
    private String NameUser;
    private String EmailUser;
    private String TokenUser;
    private String ProductTitle;
    private String ProductDescript;
    private String ProductQtde;
    private String ProductCategoriy;
    private String ProductPrice;
    private String ProductStatus;
    private String ProductToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal2);
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
        FirstImage  = (ImageView) findViewById(R.id.firstImage);
        SecondImage = (ImageView) findViewById(R.id.secondImage);
        ThirdImage  = (ImageView) findViewById(R.id.thirdImage);
        //
        txtTitle    = (TextView)  findViewById(R.id.txtTitle);
        txtQtde     = (TextView)  findViewById(R.id.txtQtde);
        txtCategory = (TextView)  findViewById(R.id.txtCategoria);
        txtDescript = (TextView)  findViewById(R.id.txtDescription);
        txtAnuncioStatus = (TextView) findViewById(R.id.txtAnuncioStatus);
        btnBuy      = (Button)    findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionaCompra();
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            NameUser = extras.getString("Nome");
            EmailUser = extras.getString("Email");
            TokenUser = extras.getString("Token");
        }

        initActivity();
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

    private void initActivity(){
        //
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            ProductTitle      = extras.getString("ProductTitle");
            ProductDescript   = extras.getString("ProductDescript");
            ProductQtde       = extras.getString("ProductQtde");
            ProductCategoriy  = extras.getString("ProductCategory");
            ProductPrice      = extras.getString("ProductPrice");
            ProductStatus     = extras.getString("ProductStatus");
            ProductToken      = extras.getString("ProductToken");

            txtQtde     = (TextView)  findViewById(R.id.txtQtde);
            txtCategory = (TextView)  findViewById(R.id.txtCategoria);
            txtDescript = (TextView)  findViewById(R.id.txtDescription);
            txtPrice    = (TextView)  findViewById(R.id.txtPreco);

            txtTitle.setText(txtTitle.getText().toString() + ProductTitle);
            txtQtde.setText(txtQtde.getText().toString() + ProductDescript);
            txtCategory.setText(txtCategory.getText().toString() + ProductCategoriy);
            txtDescript.setText(txtDescript.getText().toString() + ProductDescript);
            txtPrice.setText(txtPrice.getText().toString() + ProductPrice);

            switch (ProductToken){
                case "-Ko2kLW6Q1STxPr9xoqR":
                    FirstImage.setImageResource(R.drawable.sleepykoala);
                    SecondImage.setImageResource(R.drawable.koala);
                    ThirdImage.setImageResource(R.drawable.happykoala);
                    break;
                case "-KoFLcVOmpb0oZqPw4d9":
                    FirstImage.setImageResource(R.drawable.s7_edge);
                    SecondImage.setImageResource(R.drawable.s7);
                    break;
                case "-KoFb-kG1dLeN2Z-SnYu":
                    FirstImage.setImageResource(R.drawable.macbook);
                    break;
                default:
                    FirstImage.setImageResource(R.drawable.tenis_nike);
                    break;
            }

            int ProductStatusInt = 0;
            try{
                ProductStatusInt = Integer.parseInt(ProductStatus);
            }catch (Exception e){
                e.printStackTrace();
            }

            if (ProductStatusInt == 1){
                btnBuy.setVisibility(View.INVISIBLE);
            }
            else
                txtAnuncioStatus.setVisibility(View.INVISIBLE);
        }
    }

    private void questionaCompra(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Você tem certeza que deseja comprar este produto?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                setCompra();
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
    private void setCompra(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference();
        HashMap<String, Object> result = new HashMap<>();
        //venda feita com sucesso
        result.put("productStatus", "1");
        result.put("productBuyer ", EmailUser);

        reference.child("product").child(ProductToken).updateChildren(result);

        Toast.makeText(this,"Compra feita com sucsso",Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        openActivity(intent);

    }
}
