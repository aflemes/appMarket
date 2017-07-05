package com.aplication.appmarket;

/**
 * Created by skyli on 02/07/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import model.Product;
import model.Upload;

public class CustomAdapter extends BaseAdapter{
    String [] title;
    double [] price;

    private List<Upload> uploads;
    private Context context;
    private int[] imageIco;
    private static LayoutInflater inflater=null;
    private ArrayList<Product> product;

    public CustomAdapter(Context context, String[] title, int[] imageIco, double[] price, ArrayList<Product> product) {
        // TODO Auto-generated constructor stub
        this.title     = title;
        this.imageIco  = imageIco;
        this.price     = price;
        this.product   = product;

        this.context  = context;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView txtTitle;
        ImageView imageIco;
        TextView txtPrice;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.custom_listview, null);
        holder.txtTitle = (TextView)  rowView.findViewById(R.id.listview_item_title);
        holder.imageIco = (ImageView) rowView.findViewById(R.id.listview_image);
        holder.txtPrice = (TextView)  rowView.findViewById(R.id.listview_item_price);

        holder.txtTitle.setText(title[position]);
        holder.imageIco.setImageResource(imageIco[position]);
        //Glide.with(context).load(upload.getUrl()).into(holder.imageIco);
        holder.txtPrice.setText("R$ " + String.valueOf(price[position]));

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Deal2Activity.class);
                intent.putExtra("ProductTitle",title[position]);
                intent.putExtra("ProductPrice",String.valueOf(price[position]));
                intent.putExtra("ProductQtde",String.valueOf(product.get(position).getProductQtde()));

                intent.putExtra("ProductCategory",String.valueOf(product.get(position).getProductCategory()));
                intent.putExtra("ProductDescript",String.valueOf(product.get(position).getProductDescript()));
                intent.putExtra("ProductToken"   ,String.valueOf(product.get(position).getProductToken()));
                intent.putExtra("ProductStatus"  ,String.valueOf(product.get(position).getProductStatus()));

                context.startActivity(intent);
            }
        });
        return rowView;
    }

}