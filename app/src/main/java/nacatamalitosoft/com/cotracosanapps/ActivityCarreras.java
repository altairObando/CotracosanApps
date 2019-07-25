package nacatamalitosoft.com.cotracosanapps;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import nacatamalitosoft.com.cotracosanapps.Modelos.Carreras;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class ActivityCarreras extends AppCompatActivity {
    ArrayList<Carreras> listaCarreras;
    RecyclerView recyclerView;
    int idBus;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carreras);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        idBus = (int)getIntent().getIntExtra("idBus", 0);
        if(idBus!=0)
        {
            progressDialog = new ProgressDialog(ActivityCarreras.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Obteniendo los Creditos");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new getCarreras().execute();

            recyclerView = (RecyclerView)findViewById(R.id.ReciclerCarrera);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));
        }
        else {
            Toast.makeText(ActivityCarreras.this, "No se esta recibiendo al Autobus",
                    Toast.LENGTH_LONG).show();
        }
    }

    public class getCarreras extends AsyncTask<Void, Void, Void>{

        ArrayList<Carreras> listaInterna;
        Carreras temp;

        @Override
        protected Void doInBackground(Void... voids) {
            String uri = "http://cotracosan.tk/ApiCarreras/getCarrerasPorVehiculo?vehiculoId=" + idBus
                    +"&max=50";
            StringRequest request = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONArray array = new JSONObject(response).getJSONArray("carreras");
                        listaInterna = new ArrayList<>();
                        for(int i = 0; i<array.length();i++)
                        {
                            JSONObject jsonObject = array.getJSONObject(i);
                            temp = new Carreras(jsonObject.getInt("Id"), jsonObject.getString("CodigoCarrera"),
                                    jsonObject.getString("FechaDeCarrera"), jsonObject.getDouble("MontoRecaudado"),
                                    jsonObject.getDouble("Multa"), jsonObject.getString("Conductor"),
                                    jsonObject.getInt("ConductorId"), jsonObject.getString("Vehiculo"), jsonObject.getInt("VehiculoId"),
                                    jsonObject.getString("LugarFinalRecorrido"), jsonObject.getString("HoraDeLlegada"), jsonObject.getString("Turno"),
                                    jsonObject.getBoolean("CarreraAnulada"), jsonObject.getDouble("MontoRecaudado")- jsonObject.getDouble("Multa"));
                            listaInterna.add(temp);
                        }
                        if(listaInterna.size()>0)
                        {
                            listaCarreras = listaInterna;
                            AdapterCarreras adapterCarreras =  new AdapterCarreras(listaInterna, ActivityCarreras.this);
                            recyclerView.setAdapter(adapterCarreras);
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                        else
                        {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(ActivityCarreras.this, "No es posible recuperar las carreras",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (JSONException ex){
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(ActivityCarreras.this, "Error durante la sincronización",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ActivityCarreras.this, error.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
            VolleySingleton.getInstance(ActivityCarreras.this).addToRequestQueue(request);
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
