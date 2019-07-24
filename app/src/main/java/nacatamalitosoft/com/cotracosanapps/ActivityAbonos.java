package nacatamalitosoft.com.cotracosanapps;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import nacatamalitosoft.com.cotracosanapps.Modelos.Abonos;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class ActivityAbonos extends AppCompatActivity {

    ArrayList<Abonos> listaAbonos;
    RecyclerView recyclerView;
    int idBus;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonos);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        idBus = (int)getIntent().getIntExtra("idBus", 0);

        progressDialog = new ProgressDialog(ActivityAbonos.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Obteniendo los CreditosActivity");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new getAbonos().execute();

        recyclerView = (RecyclerView)findViewById(R.id.ReciclerAbonos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public class getAbonos extends AsyncTask<Void, Void, Void>
    {
        ArrayList<Abonos> listaInterna;
        Abonos abonos;
        @Override
        protected Void doInBackground(Void... voids) {
            String uri = "http://cotracosan.tk/ApiVehiculos/getAbonosPorVehiculo?vehiculoId=" + idBus;
            StringRequest request =  new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray("abonos");
                        listaInterna = new ArrayList<>();
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            Date fecha=null;
                            JSONObject temp = jsonArray.getJSONObject(i);
                            String cadena = temp.getString("Fecha");
                            try{
                                fecha = new SimpleDateFormat("MM/dd/yyyy").parse(cadena);
                            }catch (Exception ex)
                            {
                                ex.getMessage();
                            }
                            abonos = new Abonos(temp.getInt("IdAbono"), temp.getInt("CreditoId"), temp.getInt("VehiculoId"),
                                    temp.getString("Placa"), fecha, temp.getString("CodigoAbono"), temp.getDouble("MontoAbono"),
                                    temp.getBoolean("AbonoAnulado"));

                            listaInterna.add(abonos);
                        }

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                        if(listaInterna.size() ==0)
                            Toast.makeText(getBaseContext(), "No hay Abonos", Toast.LENGTH_LONG ).show();
                        else
                        {
                            AdapterAbonos credito = new AdapterAbonos(listaInterna, ActivityAbonos.this);
                            recyclerView.setAdapter(credito);
                        }


                    }catch (JSONException ex)
                    {
                        Toast.makeText(ActivityAbonos.this, ex.getMessage(), Toast.LENGTH_LONG);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

                    Toast.makeText(ActivityAbonos.this, error.getMessage(), Toast.LENGTH_LONG ).show();
                }
            });
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
