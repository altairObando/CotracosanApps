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
import java.util.Objects;

public class ActivityDiario extends AppCompatActivity {

    TextView txtAbono, txtCarreras, txtCreditos;
    String carrera;
    int idBus;
    Date hoy = new Date();
    DateFormat fecha = new SimpleDateFormat("MM/dd/yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diario);

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            idBus = Integer.parseInt(Objects.requireNonNull(extras.getString("idBus")));
            new ObtenerValores().execute();
            txtCarreras =(TextView) findViewById(R.id.txtCarreras);
            txtCarreras.setText(carrera);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No se puede hacer la operacion", Toast.LENGTH_LONG);
        }


    }

    public class ObtenerValores extends AsyncTask<Void, Void, Void>{
        String uri = "http://cotracosan.tk/ApiVehiculos/getMontoRecaudado?vehiculoId="+ idBus
                + "&fecha=" + fecha.format(hoy);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    carrera = object.getString("Monto");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
