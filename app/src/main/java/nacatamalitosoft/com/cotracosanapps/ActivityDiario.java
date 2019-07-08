package nacatamalitosoft.com.cotracosanapps;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityDiario extends AppCompatActivity {

    TextView txtAbono, txtCarreras, txtCreditos;
    String carrera;
    int id;
    Date hoy = new Date();
    DateFormat fecha = new SimpleDateFormat("MM/dd/yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diario);
        /*id = savedInstanceState.getInt("id", 0);
        new ObtenerValores().execute();
        txtCarreras =(TextView) finvdViewById(R.id.txtCarreras);
        txtCarreras.setText(carrera);*/

    }

    public class ObtenerValores extends AsyncTask<Void, Void, Void>{
        String uri = "http://cotracosan.tk/ApiVehiculos/getMontoRecaudado?vehiculoId="+ id
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
