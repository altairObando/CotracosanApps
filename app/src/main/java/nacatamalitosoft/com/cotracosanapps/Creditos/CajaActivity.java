package nacatamalitosoft.com.cotracosanapps.Creditos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nacatamalitosoft.com.cotracosanapps.Creditos.Acciones.Busqueda;
import nacatamalitosoft.com.cotracosanapps.Creditos.Acciones.Creditos;
import nacatamalitosoft.com.cotracosanapps.LoginActivity;
import nacatamalitosoft.com.cotracosanapps.R;
import nacatamalitosoft.com.cotracosanapps.UserManager.UserDetailsActivity;
import nacatamalitosoft.com.cotracosanapps.localDB.UserSingleton;

public class CajaActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caja);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Cotracosan Creditos");
        tabLayout = findViewById(R.id.tabCaja);
        viewPager = findViewById(R.id.viewpager);
        // Adaptar
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        TextView textView1 = (TextView)LayoutInflater.from(CajaActivity.this).inflate(R.layout.custom_tab, null);
        TextView textView2 = (TextView)LayoutInflater.from(CajaActivity.this).inflate(R.layout.custom_tab, null);

        textView1.setText("Creditos");
        textView2.setText("Buscar Creditos");

        textView1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.cart, 0,0);
        textView2.setCompoundDrawablesWithIntrinsicBounds(0, android.R.drawable.ic_menu_search, 0,0);

        tabLayout.getTabAt(0).setCustomView(textView1);
        tabLayout.getTabAt(1).setCustomView(textView2);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Creditos(), "Creditos");
        adapter.addFragment(new Busqueda(), "Buscar Creditos");
        viewPager.setAdapter(adapter);
    }
    public class ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int i) {
            return mFragmentTitleList.get(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(CajaActivity.this, UserDetailsActivity.class);
            startActivity(i);
            return true;
        }else if(id == R.id.action_exit){
            // Cambiar el usuario a logged false.
            UserSingleton.CerrarSesion(CajaActivity.this);
            Intent i = new Intent(CajaActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
