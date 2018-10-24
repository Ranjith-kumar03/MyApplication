package tk.onlinesilkstore.myapplication;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SectionsPagerAdaptor mSectionPagerAdaptor;
    private TabLayout mTablayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mToolbar=findViewById(R.id.main_page_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Chat");

        mViewPager=findViewById(R.id.main_tabpager);
        mSectionPagerAdaptor=new SectionsPagerAdaptor(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdaptor);
        mTablayout=findViewById(R.id.maintabs);
        mTablayout.setupWithViewPager(mViewPager);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null)
        {
            send_to_start();

        }
        else
        {

        }
    }

    private void send_to_start() {
        startActivity(new Intent(MainActivity.this,Login_Activity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.main_logout_btn:
                FirebaseAuth.getInstance().signOut();
                send_to_start();
                return  true;
            case R.id.main_account_setting_btn:
                Intent setting_intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(setting_intent);
                return  true;
            case R.id.main_alluser_btn:
                Intent Allusers_intent=new Intent(MainActivity.this,AllUsers_Activity.class);
                startActivity(Allusers_intent);
                return  true;

                default:
                    return super.onOptionsItemSelected(item);
        }

    }
}
