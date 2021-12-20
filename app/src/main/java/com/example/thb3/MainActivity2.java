package com.example.thb3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {
    ArrayList<Product> listProduct;
    Product product;
    String position;
    Button btnUpdate;
    ListView lvProduct;
    ProductAdapter adapter;
    EditText txtId, txtName, txtDateImport, txtQuantityWH, txtProducer, txtPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        txtId = (EditText) findViewById(R.id.edt_main2Id);
        txtName = (EditText) findViewById(R.id.edt_main2Name);
        txtDateImport = (EditText) findViewById(R.id.edt_main2DateImport);
        txtQuantityWH = (EditText) findViewById(R.id.edt_main2QuantityWH);
        txtProducer = (EditText) findViewById(R.id.edt_main2Producer);
        txtPrice = (EditText) findViewById(R.id.edt_main2Price);
        btnUpdate = (Button) findViewById(R.id.btn_main2Update);

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
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));
                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
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


        lvProduct = (ListView) findViewById(R.id.lv_Product2);
        adapter = new ProductAdapter(
                MainActivity2.this,
                R.layout.item_product
        );
        lvProduct.setAdapter(adapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        if (bundle != null) {
            position = bundle.getString("position");
            product = (Product) bundle.getSerializable("product");
            listProduct = (ArrayList<Product>) bundle.getSerializable("list_product");

            adapter.setData(listProduct);

            txtId.setText(listProduct.get(Integer.valueOf(position)).getId());
            txtName.setText(listProduct.get(Integer.valueOf(position)).getName());
            txtDateImport.setText(listProduct.get(Integer.valueOf(position)).getDateImport());
            txtQuantityWH.setText(String.valueOf(listProduct.get(Integer.valueOf(position)).getQuantityWH()));
            txtProducer.setText(listProduct.get(Integer.valueOf(position)).getProducer());
            txtPrice.setText(listProduct.get(Integer.valueOf(position)).getPrice());
        }

        btnUpdate.setOnClickListener(v -> {
            Product newProduct = new Product(
                    txtId.getText().toString(),
                    txtName.getText().toString(),
                    txtDateImport.getText().toString(),
                    Integer.parseInt(txtQuantityWH.getText().toString()),
                    txtProducer.getText().toString(),
                    txtPrice.getText().toString()
            );

            listProduct.set(Integer.valueOf(position), newProduct);
            Intent i = new Intent( MainActivity2.this,MainActivity.class);
            Bundle bundle2 = new Bundle();
            bundle2.putSerializable("list_product_2",listProduct);
            i.putExtra("data2", bundle2);
            startActivity(i);
        });

    }
}