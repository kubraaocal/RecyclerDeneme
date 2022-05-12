package com.example.recyclerdeneme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button pick;
    TextView textView;

    ArrayList<Uri> uris=new ArrayList<>();
    RecyclerAdapter adapter;

    private static final int Read_Permission=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.totalPhotos);
        recyclerView=findViewById(R.id.recyclerView);
        pick=findViewById(R.id.pick);

        adapter=new RecyclerAdapter(uris);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,4));
        recyclerView.setAdapter(adapter);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
        }

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1&& resultCode== Activity.RESULT_OK){
            if(data.getData()!=null){
                ClipData cd = data.getClipData();

                if ( cd == null ) {
                    Uri uri = data.getData();
                    uris.add(uri);
                }
                else {
                    for (int i = 0; i < cd.getItemCount(); i++) {
                        ClipData.Item item = cd.getItemAt(i);
                        Uri uri = item.getUri();
                        uris.add(uri);
                    }
                }
            //int x=data.getClipData().getItemCount();

            /*for(int i=0;i<x;i++){
                uri.add(data.getClipData().getItemAt(i).getUri());
            }*/

            adapter.notifyDataSetChanged();
            textView.setText("Photo "+uris.size());


            }else if(data.getData()!=null){
                String imageURL=data.getData().getPath();
                uris.add(Uri.parse(imageURL));
            }

        }
    }
}