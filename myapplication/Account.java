package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;


public class Account extends AppCompatActivity implements AccountFragment.onSomeEventListener {

    private TabLayout tab;

    @Override
    public void someEvent(String s) {
        Intent intent = new Intent(this, SavedMovies.class);
        Bundle b = new Bundle();
        b.putString("type", s);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        if(findViewById(R.id.container) != null ) {

            tab = findViewById(R.id.tab);
            tab.addTab(tab.newTab().setText("Account"));
            tab.addTab(tab.newTab().setText("Edit Account"));
            AccountFragment acf = new AccountFragment();
            startFragment(acf);


            tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    int position = tab.getPosition();

                    if (position == 0) {
                        startFragment(new AccountFragment());
                    } else if (position == 1) {
                        startFragment(new EditAccountFragment());
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });


        } else{

            AccountFragment f1 = new AccountFragment();
            EditAccountFragment f2 = new EditAccountFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frag1, f1).replace(R.id.frag2, f2);
            ft.commit();
        }


    }

    public void startFragment(Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, f);
        ft.commit();
    }


}


