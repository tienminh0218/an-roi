package com.example.thb3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ArrayList<Product> listProduct, listProduct2;
    ListView lvProduct;
    EditText txtId, txtName, txtDateImport, txtQuantityWH, txtProducer, txtPrice;
    Button btnAdd, btnStc;
    ProductAdapter adapter;
    int index;
    boolean isStc = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtName = (EditText) findViewById(R.id.edt_mainName);
        txtDateImport = (EditText) findViewById(R.id.edt_mainDateImport);
        txtQuantityWH = (EditText) findViewById(R.id.edt_mainQuantityWH);
        txtProducer = (EditText) findViewById(R.id.edt_mainProducer);
        txtPrice = (EditText) findViewById(R.id.edt_mainPrice);
        btnAdd = (Button) findViewById(R.id.btn_submit);
        btnStc = (Button) findViewById(R.id.btn_tk);

        listProduct = new ArrayList<Product>();
        lvProduct = (ListView) findViewById(R.id.lv_Product);

        adapter = new ProductAdapter(
                MainActivity.this,
                R.layout.item_product
        );
        lvProduct.setAdapter(adapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data2");
        if (bundle != null) {
            String p = bundle.getString("position");
            Product productUpToDate = (Product) bundle.getSerializable("productUpToDate");
            listProduct = (ArrayList<Product>) bundle.getSerializable("list_product_2");

//            listProduct.set(Integer.parseInt(p), productUpToDate);
            adapter.setData(listProduct);
        }

        /// events
        btnAdd.setOnClickListener((View v) -> {
            String name = txtName.getText().toString();
            String dateImport = txtDateImport.getText().toString();
            int quantityWH = Integer.parseInt(txtQuantityWH.getText().toString());
            String producer = txtProducer.getText().toString();
            String price = txtPrice.getText().toString();
            Product newProduct = new Product(generateId(), name, dateImport, quantityWH, producer, price);

            listProduct.add(newProduct);
            adapter.setData(listProduct);
        });

        btnStc.setOnClickListener(v -> {
            if(isStc) {
                btnStc.setText("Thống Kê");
                adapter.setData(listProduct);
                isStc = false;
            } else {
                btnStc.setText("Quay Lai");
                stcProducts(adapter);
                isStc = true;
            }
        });


        lvProduct.setOnItemClickListener((parent, view, position, id) -> {
//            index = position;
//            txtName.setText(listProduct.get(position).getName());
//            txtDateImport.setText(listProduct.get(position).getDateImport());
//            txtQuantityWH.setText(String.valueOf(listProduct.get(position).getQuantityWH()));
//            txtProducer.setText(listProduct.get(position).getProducer());
//            txtPrice.setText(listProduct.get(position).getPrice());
//
//            Product productUpdate = new Product(
//                    listProduct.get(position).getId(),
//                    listProduct.get(position).getName(),
//                    listProduct.get(position).getDateImport(),
//                    listProduct.get(position).getQuantityWH(),
//                    listProduct.get(position).getProducer(),
//                    listProduct.get(position).getPrice()
//            );

            Intent i = new Intent(MainActivity.this, MainActivity2.class);
            Bundle bundleUpdate = new Bundle();
            bundleUpdate.putSerializable("list_product", listProduct);
            bundleUpdate.putString("position", String.valueOf(position));
            i.putExtra("data", bundleUpdate);
            startActivity(i);
        });

        lvProduct.setOnItemLongClickListener((parent, view, position, id) -> {
            String name = listProduct.get(position).getName();
            listProduct.remove(position);
            adapter.setData(listProduct);
            Toast.makeText(MainActivity.this, "Xoá thành công " + name,
                    Toast.LENGTH_LONG).show();
            return false;
        });

        txtDateImport.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    txtDateImport.setText(current);
                    txtDateImport.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void stcProducts(ProductAdapter adapter){
        listProduct2 = new ArrayList<Product>();
        for (int i = 0; i < listProduct.size(); i++) {
                if (listProduct.get(i).getQuantityWH() < 5){
                    Product newProduct = new Product(listProduct.get(i).getId(),
                            listProduct.get(i).getName(),
                            listProduct.get(i).getDateImport(),
                            listProduct.get(i).getQuantityWH(),
                            listProduct.get(i).getProducer(),
                            listProduct.get(i).getPrice());
                    listProduct2.add(newProduct);
                }
            }
        adapter.setData(listProduct2);
    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }



}