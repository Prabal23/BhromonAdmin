package com.example.pranto.rentacaradmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout vehicles = (LinearLayout)findViewById(R.id.info);
        vehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Vehicles.class);
                startActivity(intent);
            }
        });

        LinearLayout packages = (LinearLayout)findViewById(R.id.info1);
        packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Packages.class);
                startActivity(intent);
            }
        });

        LinearLayout advertisement = (LinearLayout)findViewById(R.id.info2);
        advertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AdUpload.class);
                startActivity(intent);
            }
        });

        LinearLayout hotel = (LinearLayout)findViewById(R.id.info3);
        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Hotels.class);
                startActivity(intent);
            }
        });

        LinearLayout room = (LinearLayout)findViewById(R.id.info4);
        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RoomType.class);
                startActivity(intent);
            }
        });
    }
}
