package nacatamalitosoft.com.cotracosanapps;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nacatamalitosoft.com.cotracosanapps.Modelos.Buses;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class ActivityBusesContador extends AppCompatActivity {
    ProgressDialog progressDialog;
    private GridView gridView;
    int operacion;

    private List<Buses> buses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses_contador);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        operacion =(int)getIntent().getIntExtra("operacion", 0);
        if(operacion!=0)
        {
            progressDialog = new ProgressDialog(ActivityBusesContador.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Obteniendo tus vehiculos");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new BusesTask().execute();
            gridView =(GridView) findViewById(R.id.mainGridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (operacion){
                        case 1:{
                            Intent i = new Intent(ActivityBusesContador.this, ActivityCredito.class);
                            i.putExtra("idBus", buses.get(position).getId());
                            startActivity(i);
                        };break;
                        case 2:{
                            Intent i = new Intent(ActivityBusesContador.this, ActivityCarreras.class);
                            i.putExtra("idBus", buses.get(position).getId());
                            startActivity(i);
                        };break;
                        default:{
                            Toast.makeText(ActivityBusesContador.this, "¡¡¡¡FALTAL ERROR!!!!",
                                    Toast.LENGTH_LONG).show();
                        };break;
                    }
                }
            });
        }
        else
            Toast.makeText(ActivityBusesContador.this, "No es posible realizar la operacion",
                    Toast.LENGTH_LONG).show();
    }

    @SuppressLint("StaticFieldLeak")
    public class BusesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            String uri = "http://cotracosan.somee.com/ApiVehiculos/getVehiculos";
            buses = new ArrayList<>();
            StringRequest request = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Este metodo es llamado cuando la solicitud es completada y se ha recepcionado
                    // Un valor proporcionado por el servidor
                    try {
                        // Convertir la respuesta HTTP/String a un objeto manipulable JSON
                        JSONObject object = new JSONObject(response);
                        // De la respuesta del servidor extraemos el recurso que se requiere iterar.
                        // Donde vehiculos es el nombre del arreglo que entrega el servidor
                        JSONArray data = object.getJSONArray("vehiculos");
                        for (int i = 0; i < data.length(); i++) {
                            // Extraemos cada objeto JSON del arreglo que iteramos
                            JSONObject o = data.getJSONObject(i);
                            // Nueva instancia de vehiculos
                            Buses bus = new Buses(
                                    o.getInt("Id"),
                                    o.getInt("SocioId"),
                                    o.getString("Placa"),
                                    true
                            );
                            // Agregamos el nuevo objeto a la lista.
                            buses.add(bus);
                        }
                        if(buses.size() == 0)
                            Toast.makeText(getBaseContext(),"Error al traer vehiculos", Toast.LENGTH_SHORT).show();
                        // Estando fuera del for actualizamos el adaptador.
                        GridAdapter adapter = new GridAdapter(getApplicationContext(), buses);
                        gridView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });
            // Obtener la instancia unica para las solicitudes HTTP
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
            return null;
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
