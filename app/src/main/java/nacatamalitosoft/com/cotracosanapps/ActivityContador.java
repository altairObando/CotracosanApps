package nacatamalitosoft.com.cotracosanapps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import nacatamalitosoft.com.cotracosanapps.Creditos.CajaActivity;
import nacatamalitosoft.com.cotracosanapps.Dashboard.ResumenContador;
import nacatamalitosoft.com.cotracosanapps.UserManager.UserDetailsActivity;
import nacatamalitosoft.com.cotracosanapps.localDB.UserSingleton;

public class ActivityContador extends AppCompatActivity {

    Button btnCredPendiente, btnCredUlt, btnGastos, btnCarreras, btnDashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador);
        btnCredPendiente = (Button)findViewById(R.id.button1);
        btnCredUlt = (Button)findViewById(R.id.button4);
        btnCarreras = (Button)findViewById(R.id.button2);
        btnGastos = (Button)findViewById(R.id.button3);

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

        btnDashboard = findViewById(R.id.btn4);
        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityContador.this, ResumenContador.class));
            }
        });
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
            Intent i = new Intent(ActivityContador.this, UserDetailsActivity.class);
            startActivity(i);
            return true;
        }else if(id == R.id.action_exit){
            // Cambiar el usuario a logged false.
            UserSingleton.CerrarSesion(getApplicationContext());
            Intent i = new Intent(ActivityContador.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
