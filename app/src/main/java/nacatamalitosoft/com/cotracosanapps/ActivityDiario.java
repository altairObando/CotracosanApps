package nacatamalitosoft.com.cotracosanapps;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class ActivityDiario extends AppCompatActivity {

    TextView txtAbono, txtCarreras, txtCreditos;
    double carrera, credito, abono;
    int idBus;
    Date hoy = new Date();
    DateFormat fecha = new SimpleDateFormat("MM/dd/yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diario);
        idBus = getIntent().getIntExtra("idBus", 0);
        if(idBus!=0)
        {

            new ObtenerValores().execute();
            txtCarreras =(TextView) findViewById(R.id.txtCarreras);
            txtCreditos = (TextView) findViewById(R.id.txtCreditos);
            txtAbono = (TextView)findViewById(R.id.txtAbonos);
            txtCarreras.setText(String.valueOf(carrera));
            txtCreditos.setText(String.valueOf(credito));
            txtAbono.setText(String.valueOf(abono));

        }
        else
        {
            Toast.makeText(getApplicationContext(), "No se puede hacer la operacion", Toast.LENGTH_LONG).show();
        }


    }

    public class ObtenerValores extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            String uri = "http://cotracosan.tk/ApiVehiculos/getConsolidadoVehiculo?vehiculoId="+ idBus;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        carrera = object.getDouble("carreras");
                        credito = object.getDouble("creditos");
                        abono = object.getDouble("abonos");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            return null;
        }
    }
}
