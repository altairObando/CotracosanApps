package nacatamalitosoft.com.cotracosanapps.Creditos;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TextView;

import nacatamalitosoft.com.cotracosanapps.R;

public class CajaActivity extends AppCompatActivity {
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caja);
        tabLayout = findViewById(R.id.tabCaja);
        crearTabs();
        crearAdapterTab();
    }

    private void crearAdapterTab() {

    }

    private void crearTabs() {
        TextView tab1 = (TextView) LayoutInflater.from(CajaActivity.this).inflate(R.layout.custom_tab, null);
        tab1.setText("Creditos");
        tab1.setCompoundDrawablesWithIntrinsicBounds(0,android.R.drawable.star_on, 0,0);
        tabLayout.getTabAt(0).setCustomView(tab1);
        TextView tab2 = (TextView) LayoutInflater.from(CajaActivity.this).inflate(R.layout.custom_tab, null);
        tab2.setText("Buscar Credito");
        tab2.setCompoundDrawablesWithIntrinsicBounds(0,android.R.drawable.ic_dialog_info, 0,0);
        tabLayout.getTabAt(1).setCustomView(tab2);
    }
}
