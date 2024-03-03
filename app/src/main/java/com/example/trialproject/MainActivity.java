package com.example.trialproject;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.trialproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout main_frame;
    private TextView drawerProfileName,drawerProfileText;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);


        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Categories");



         ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,
                 drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
         drawerLayout.addDrawerListener(toggle);
         toggle.syncState();

         drawerProfileName=navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_name);
         drawerProfileText=navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_text_img);

         String name=DbQuery.myProfile.getName();
         drawerProfileName.setText(name);

         drawerProfileText.setText(name.toUpperCase().substring(0,1));

         navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 int id=item.getItemId();
                 if (id==R.id.nav_home)
                 {
                     setFragement(new CategoryFragment());

                 } else if (id==R.id.nav_account) {
                     setFragement(new AccountFragment());
                 }else
                 {
                     setFragement(new LeaderBoardFragment());
                 }

                drawerLayout.closeDrawer(GravityCompat.START);

                 return true;
             }
         });

       // binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

       // setSupportActionBar(binding.appBarMain.toolbar);
        //binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
           // @Override
          //  public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      //  .setAction("Action", null).show();
            //}
       // });

        bottomNavigationView = findViewById(R.id.bottom_nav_bar1);
        main_frame = findViewById(R.id.main_frame);

        bottomNavigationView.setOnItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                int itemId = item.getItemId(); // Get the item ID

                if (itemId == R.id.nav_home1) {
                    setFragement(new CategoryFragment());
                    //bottomNavigationView.setSelectedItemId(R.id.nav_home1);

                } else if (itemId == R.id.nav_leaderboard) {
                   setFragement(new LeaderBoardFragment());
                   // bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);

                } else if (itemId == R.id.nav_account) {
                     setFragement(new AccountFragment());
                   // bottomNavigationView.setSelectedItemId(R.id.nav_account);
                }
            }
        });


       // DrawerLayout drawer = binding.drawerLayout;
        //NavigationView navigationView = binding.navView;
       // mAppBarConfiguration = new AppBarConfiguration.Builder(
              //  R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
               // .setOpenableLayout(drawer)
               // .build();

        setFragement(new CategoryFragment());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setFragement(Fragment fragement)
    {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(main_frame.getId(),fragement);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
}
