package com.example.thb3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Product> listProduct = new ArrayList<Product>();
    private int layout;

    public ProductAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
    }

    public void setData (ArrayList<Product> list){
        this.listProduct = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.listProduct.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);

        TextView tvId, tvName, tvDateImport, tvQuantityWH, tvProducer, tvPrice;
        tvId = (TextView) convertView.findViewById(R.id.tv_id);
        tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvDateImport = (TextView) convertView.findViewById(R.id.tv_dateImport);
        tvQuantityWH = (TextView) convertView.findViewById(R.id.tv_quantityWH);
        tvProducer = (TextView) convertView.findViewById(R.id.tv_producer);
        tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

        Product product = listProduct.get(position);
        tvId.setText(String.valueOf(product.getId()));
        tvName.setText(product.getName().toString());
        tvDateImport.setText(product.getDateImport().toString());
        tvQuantityWH.setText(String.valueOf(product.getQuantityWH()));
        tvProducer.setText(product.getProducer().toString());
        tvPrice.setText(product.getPrice().toString());

        return convertView;
    }
}
