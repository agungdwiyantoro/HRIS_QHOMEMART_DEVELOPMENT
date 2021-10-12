package com.app.mobiledev.apphris.bonus;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.bonus.bonus_promosi_iklan.bonus;
import com.app.mobiledev.apphris.bonus.bonus_promosi_proyek.list_bonus_proyek;
import com.app.mobiledev.apphris.helperPackage.DataSoalSQLite;

public class menu_bonus extends AppCompatActivity {
    private Toolbar mToolbar;
    private CardView cvIklanProyek,cvIklanPromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bonus);
        cvIklanPromo=findViewById(R.id.cvIklanPromo);
        cvIklanProyek=findViewById(R.id.cvIklanProyek);
        mToolbar = findViewById(R.id.toolbar_menu_bonus);
        mToolbar.setTitle("Menu Bonus");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cvIklanProyek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_bonus.this, list_bonus_proyek.class));
                deleteKaryawan();

            }
        });

        cvIklanPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_bonus.this, bonus.class));

            }
        });
    }


    private void deleteKaryawan(){
        DataSoalSQLite getDatabase = new DataSoalSQLite(menu_bonus.this);
        String deleteQuery = "DELETE FROM tblkaryawan";
        SQLiteDatabase DeleteData = getDatabase.getWritableDatabase();
        DeleteData.execSQL(deleteQuery);
        DeleteData.close();
    }
}
