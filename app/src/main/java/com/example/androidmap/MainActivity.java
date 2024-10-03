package com.example.androidmap;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLima = findViewById(R.id.btnLima);
        btnLima.setOnClickListener(v -> loadFragment(new FragmentLima()));

        Button btnArequipa = findViewById(R.id.btnArequipa);
        btnArequipa.setOnClickListener(v -> loadFragment(new FragmentArequipa()));

        Button btnCusco = findViewById(R.id.btnCusco);
        btnCusco.setOnClickListener(v -> loadFragment(new FragmentCusco()));


        Button btnTrujillo = findViewById(R.id.btnTrujillo);
        btnTrujillo.setOnClickListener(v -> loadFragment(new FragmentTrujillo()));

        Button btnIquitos = findViewById(R.id.btnIquitos);
        btnIquitos.setOnClickListener(v -> loadFragment(new FragmentIquitos()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FrameContenedor, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}