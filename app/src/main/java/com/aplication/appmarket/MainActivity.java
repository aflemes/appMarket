package com.aplication.appmarket;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import model.Product;
import model.Upload;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String NameUser;
    private String EmailUser;
    private String TokenUser;
    /**/
    private TextView txtNavEmail;
    private TextView txtNavNome;
    //
    public static int [] prgmImages={R.drawable.ic_quit,R.drawable.common_full_open_on_phone};
    public static String [] prgmNameList={"Let Us C","c++"};
    private ListView listViewProducts;
    private Context context;
    //
    private ArrayList<Upload> uploads;
    private ArrayList<Product> product;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);
        //
        context=this;

        listViewProducts=(ListView) findViewById(R.id.listViewProducts);

        Bundle extras = getIntent().getExtras();

        if (extras != null){
            NameUser  = extras.getString("Nome");
            EmailUser = extras.getString("Email");
            TokenUser = extras.getString("Token");

            dialog = ProgressDialog.show(this, "","Carregando, por favor aguarde...", true);
            loadProducts();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
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

    private void loadProducts(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("product");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sellerToken;
                int qtdeProduct = 0;
                int lgPrincipal = 0;

                product = new ArrayList<Product>();
                Product tmpProduct;


                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    sellerToken = (String) child.child("sellerToken").getValue();

                    tmpProduct = new Product();
                    tmpProduct.setProductTitle((String) child.child("productTitle").getValue());
                    tmpProduct.setProductDescript((String) child.child("productDescript").getValue());
                    tmpProduct.setProductPrice((Double) child.child("productPrice").getValue());

                    try{
                        qtdeProduct = Integer.parseInt(child.child("productQtde").getValue().toString());
                        lgPrincipal = Integer.parseInt(child.child("imgPrincipal").getValue().toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    tmpProduct.setProductQtde(qtdeProduct);
                    tmpProduct.setImgPrincipal(lgPrincipal);

                    tmpProduct.setProductToken((String) child.child("productToken").getValue());
                    tmpProduct.setProductCategory((String) child.child("productCategory").getValue());
                    tmpProduct.setSellerToken((String) child.child("sellerToken").getValue());

                    product.add(tmpProduct);
                }

                if (product != null){
                    if (product.size() > 0)
                        loadListView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadImages(){
        DatabaseReference  myRef = FirebaseDatabase.getInstance().getReference().child("images");
        uploads = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //iterating through all the values in database
                String ProductToken = "";

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ProductToken = postSnapshot.getKey();

                    for (int i = 0; i < product.size(); i++) {
                        //encontrei o anuncio
                        if (ProductToken.equals(product.get(i).getProductToken())){
                            Upload upload = postSnapshot.getValue(Upload.class);
                            uploads.add(upload);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void loadListView(){
        String [] productTitle  = new String[product.size()];
        double [] productPrice  = new double[product.size()];
        int [] productImage     = new int[product.size()];

        for (int i=0 ; i < product.size();i++){
            productTitle[i] = product.get(i).getProductTitle().toString();
            productPrice[i] = product.get(i).getProductPrice();
            productImage[i] = R.drawable.happykoala;
        }


        listViewProducts.setAdapter(new CustomAdapter(this, productTitle,productImage,productPrice));

        dialog.dismiss();
    }

}
