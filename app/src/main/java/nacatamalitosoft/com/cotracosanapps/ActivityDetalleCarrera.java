package nacatamalitosoft.com.cotracosanapps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import nacatamalitosoft.com.cotracosanapps.Modelos.Carreras;

public class ActivityDetalleCarrera extends AppCompatActivity {

    TextView codigoCarrera, fechaCarrera, placa, montoRecaudado,
    multa, montoRestante, lugarFinal, turno, llegada;

    Carreras carreras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_detalle_carrera);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        carreras = (Carreras)getIntent().getSerializableExtra("carrera");
        codigoCarrera = (TextView)findViewById(R.id.CodigoCarrera);
        fechaCarrera = (TextView)findViewById(R.id.FechaDeCarrera);
        placa = (TextView)findViewById(R.id.Placa);
        montoRecaudado=(TextView)findViewById(R.id.MontoRecaudado);
        multa = (TextView)findViewById(R.id.Multa);
        montoRestante = (TextView)findViewById(R.id.MontoRestante);
        lugarFinal = (TextView)findViewById(R.id.LugarFinal);
        turno = (TextView)findViewById(R.id.TurnoOficial);
        llegada = (TextView)findViewById(R.id.HoraLlegada);

        if(carreras!=null)
        {
            codigoCarrera.setText("Codigo: " + carreras.getCodigoCarrera());
            fechaCarrera.setText("Fecha: " + carreras.getFechaDeCarrera());
            placa.setText("Placa: " + carreras.getVehiculo());
            montoRecaudado.setText("Monto Recaudado: C$"+String.valueOf(carreras.getMontoRecaudado()));
            multa.setText(String.valueOf("Multa: C$"+carreras.getMulta()));
            montoRestante.setText("Monto Restante: C$"+String.valueOf(carreras.getMontoRestante()));
            lugarFinal.setText("Terminó: " + carreras.getLugarFinalRecorrido());
            turno.setText("Turno: " +carreras.getTurno());
            llegada.setText("Llegada: " + carreras.getHoraDeLlegada());
        }
        else
            Toast.makeText(ActivityDetalleCarrera.this, "Carrera no encontrada", Toast.LENGTH_LONG).show();
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
