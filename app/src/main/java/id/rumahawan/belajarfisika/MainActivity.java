package id.rumahawan.belajarfisika;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.student:
                fragment = new StudentFragment();
                getSupportActionBar().setTitle("Student");
                break;
            case R.id.lesson:
                fragment = new LessonFragment();
                getSupportActionBar().setTitle("Lesson");
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Student");

        loadFragment(new StudentFragment());
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        mDrawerlayout = findViewById(R.id.drawerContainer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.inflateHeaderView(R.layout.drawer_header);

        View headerLayout = navigationView.getHeaderView(0);
        TextView tvHeaderName = headerLayout.findViewById(R.id.tvHeaderName);
        tvHeaderName.setText("User Name");
        TextView tvHeaderEmail = headerLayout.findViewById(R.id.tvHeaderEmail);
        tvHeaderEmail.setText("user@gmail.com");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Loged Off
                if (item.getItemId() == R.id.logout) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    mDrawerlayout.closeDrawers();
                }
                return false;
            }
        });
    }
}
