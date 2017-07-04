package com.aplication.appmarket;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

//
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;

import model.Product;

public class Sell2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String NameUser;
    private String EmailUser;
    private String TokenUser;
    private String TokenProduct;
    private Uri[] filePath = new Uri[3];

    private Button buttonChoose;
    private Button btnVender;
    private Button btnCancelar;
    private int qtdeImagesUpload;
    //ImageView
    private ImageView FirstimageView;
    private ImageView SecondimageView;
    private ImageView ThirdimageView;
    //RadioButtons
    private RadioButton radioFirst;
    private RadioButton radioSecond;
    private RadioButton radioThird;
    //
    private Spinner spnCategory;
    //
    private EditText txtTitle;
    private EditText txtDescription;
    private EditText txtQtde;
    private EditText txtPrice;
    //firebase storage reference
    private StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST = 234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell2);
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
        buttonChoose    = (Button)    findViewById(R.id.buttonChoose);
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnVender       = (Button)    findViewById(R.id.btnVender);
        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VenderProduto();
            }
        });

        btnCancelar     = (Button)    findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancelar();
            }
        });

        FirstimageView  = (ImageView) findViewById(R.id.firstImage);
        SecondimageView = (ImageView) findViewById(R.id.secondImage);
        ThirdimageView  = (ImageView) findViewById(R.id.thirdImage);
        //
        radioFirst      = (RadioButton) findViewById(R.id.radioFirst);
        radioFirst.setVisibility(View.INVISIBLE);

        radioSecond     = (RadioButton) findViewById(R.id.radioSecond);
        radioSecond.setVisibility(View.INVISIBLE);

        radioThird      = (RadioButton) findViewById(R.id.radioThird);
        radioThird.setVisibility(View.INVISIBLE);

        txtTitle        = (EditText)    findViewById(R.id.txtTitle);
        txtDescription  = (EditText)    findViewById(R.id.txtDescription);
        txtQtde         = (EditText)    findViewById(R.id.txtQtde);
        txtPrice        = (EditText)    findViewById(R.id.txtPrice);
        //
        String[] arraySpinner;
        arraySpinner = new String[] {
                "Acessórios para Veículos","Animais","Antiguidades","Brinquedos e Hobbies","Calçados, Roupas e Bolsas",
                "Celulares e Telefones","Câmeras e Acessórios","Eletrodomésticos","Eletrônicos, Áudio e Vídeo",
                "Esportes e Fitness","Games","Informática","Livros","Músicas"};

        spnCategory = (Spinner) findViewById(R.id.spnCategoria);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arraySpinner);
        spnCategory.setAdapter(adapter);

        //getting firebase storage reference
        storageReference = FirebaseStorage.getInstance().getReference();

        Bundle extras = getIntent().getExtras();

        if (extras != null){
            NameUser  = extras.getString("Nome");
            EmailUser = extras.getString("Email");
            TokenUser = extras.getString("Token");
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

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath[qtdeImagesUpload] = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath[qtdeImagesUpload]);

                switch (qtdeImagesUpload){
                    case 0:
                        FirstimageView.setImageBitmap(bitmap);
                        radioFirst.setVisibility(View.VISIBLE);
                        qtdeImagesUpload++;
                        break;
                    case 1:
                        SecondimageView.setImageBitmap(bitmap);
                        radioSecond.setVisibility(View.VISIBLE);
                        qtdeImagesUpload++;
                        break;
                    case 2:
                        ThirdimageView.setImageBitmap(bitmap);
                        radioThird.setVisibility(View.VISIBLE);
                        qtdeImagesUpload++;
                        break;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        //if there is a file to upload
        for (int i = 0; i < qtdeImagesUpload; i++) {
            if (filePath[i] != null) {
                //displaying a progress dialog while upload is going on
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Salvando...");
                progressDialog.show();

                StorageReference riversRef = storageReference.child("images").child(TokenProduct).child("image" + i);
                riversRef.putFile(filePath[i])
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //if the upload is successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //calculating progress percentage
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            }
                        });
            }
        }
    }

    private void VenderProduto(){
        String key = FirebaseDatabase.getInstance().getReference("product").push().getKey();

        TokenProduct = key;

        uploadFile();
        saveProductSale();
    }

    private void saveProductSale(){
        int imgPrincipal = 0;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Product produto = new Product();

        if (radioFirst.isSelected())
            imgPrincipal = 0;
        else
            if (radioSecond.isSelected())
                imgPrincipal = 1;
            else
                if (radioThird.isSelected())
                    imgPrincipal = 2;

        produto.setImgPrincipal(imgPrincipal);
        produto.setProductToken(TokenProduct);
        produto.setProductTitle(txtTitle.getText().toString());
        produto.setSellerToken(TokenUser);
        produto.setProductDescript(txtDescription.getText().toString());
        produto.setProductCategory(spnCategory.getSelectedItem().toString());

        int qtde = 0;
        double valor = 0;
        try {
            qtde = Integer.parseInt(txtQtde.getText().toString());
            valor = Double.parseDouble(txtPrice.getText().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        produto.setProductQtde(qtde);
        produto.setProductPrice(valor);

        reference.child("product").child(TokenProduct).setValue(produto);
        Toast.makeText(this,"Anúncio feito com sucesso",Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        openActivity(intent);
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

    private void Cancelar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Você tem certeza?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                openMain();
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

    private void openMain(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
