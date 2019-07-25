package nacatamalitosoft.com.cotracosanapps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import nacatamalitosoft.com.cotracosanapps.Modelos.Carreras;

public class ActivityDetalleCarrera extends AppCompatActivity {

    TextView codigoCarrera, fechaCarrera, placa, montoRecaudado,
    multa, montoRestante, lugarFinal, turno, llegada;

    Carreras carreras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_detalle_carrera);
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
            codigoCarrera.setText(carreras.getCodigoCarrera());
            fechaCarrera.setText(carreras.getFechaDeCarrera().replace("/", "-"));
            placa.setText(carreras.getVehiculo());
            montoRecaudado.setText(String.valueOf(carreras.getMontoRecaudado()));
            multa.setText(String.valueOf(carreras.getMulta()));
            montoRestante.setText(String.valueOf(carreras.getMontoRestante()));
            lugarFinal.setText(carreras.getLugarFinalRecorrido());
            turno.setText(carreras.getTurno());
            llegada.setText(carreras.getHoraDeLlegada());
        }
        else
            Toast.makeText(ActivityDetalleCarrera.this, "Carrera no encontrada", Toast.LENGTH_LONG).show();
    }

    void setData()
    {
        codigoCarrera.setText(carreras.getCodigoCarrera());
        fechaCarrera.setText(carreras.getFechaDeCarrera());
        placa.setText(carreras.getVehiculo());
        montoRecaudado.setText(String.valueOf(carreras.getMontoRecaudado()));
        multa.setText(String.valueOf(carreras.getMulta()));
        montoRestante.setText(String.valueOf(carreras.getMontoRestante()));
        lugarFinal.setText(carreras.getLugarFinalRecorrido());
        turno.setText(carreras.getTurno());
        llegada.setText(carreras.getHoraDeLlegada());
    }
}
