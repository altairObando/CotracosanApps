package nacatamalitosoft.com.cotracosanapps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Objects;

import nacatamalitosoft.com.cotracosanapps.Modelos.Credito;

public class DetalleCredito extends AppCompatActivity {

    Credito credito;
    TextView codigoCredito, fechaCredito, montoCredito;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_credito);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        codigoCredito = (TextView)findViewById(R.id.txtCodigoCredito);
        fechaCredito = (TextView)findViewById(R.id.txtFechaCredito);
        montoCredito = (TextView)findViewById(R.id.txtMontoCredito);
        listView = (ListView)findViewById(R.id.listView);

        credito = (Credito)getIntent().getSerializableExtra("objetoCredito");
        if(credito!=null)
        {
            codigoCredito.setText(credito.getCodigoCredito());
            fechaCredito.setText(String.valueOf(credito.getFecha()));
            montoCredito.setText(String.valueOf(credito.getMontoTotal()));

            AdapterDetalleCredito adapter = new AdapterDetalleCredito(this, credito.getDetallesDeCreditos());
            listView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
