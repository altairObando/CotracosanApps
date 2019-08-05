package nacatamalitosoft.com.cotracosanapps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityContador extends AppCompatActivity {

    Button btnCredPendiente, btnCredUlt, btnGastos, btnCarreras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador);
        btnCredPendiente = (Button)findViewById(R.id.button1);
        btnCredUlt = (Button)findViewById(R.id.button2);
        btnCarreras = (Button)findViewById(R.id.button3);
        btnGastos = (Button)findViewById(R.id.button4);

        btnCredPendiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityContador.this, ActivityBusesContador.class);
                intent.putExtra("operacion", 1);
                startActivity(intent);
            }
        });

        btnCarreras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityContador.this, ActivityBusesContador.class);
                intent.putExtra("operacion", 2);
                startActivity(intent);
            }
        });

        btnCredUlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityContador.this, ActivityUltimosCreditos.class);
                startActivity(intent);
            }
        });

        btnGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityContador.this, ActivityGastos.class);
                startActivity(intent);
            }
        });
    }
}
