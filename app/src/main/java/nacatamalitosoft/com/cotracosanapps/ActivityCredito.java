package nacatamalitosoft.com.cotracosanapps;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import nacatamalitosoft.com.cotracosanapps.Modelos.Credito;
import nacatamalitosoft.com.cotracosanapps.Modelos.DetalleDeCredito;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class ActivityCredito extends AppCompatActivity {

    ArrayList<Credito> listaCredito;
    RecyclerView recyclerView;
    TextView textView;
    int idBus;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listacredito);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        idBus = (int)getIntent().getIntExtra("idBus", 0);
        textView = (TextView) findViewById(R.id.textView2);
        progressDialog = new ProgressDialog(ActivityCredito.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Obteniendo los Creditos");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new getCredito().execute();

        recyclerView  = (RecyclerView) findViewById(R.id.ReciclerId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));
    }

    public class getCredito extends AsyncTask<Void, Void, Void>{

        ArrayList<Credito> listaInterna;
        Credito temp;
        ArrayList<DetalleDeCredito> listaDetalle;
        DetalleDeCredito tempDet;
        @Override
        protected Void doInBackground(Void... voids) {
            String uri = "http://cotracosan.tk/ApiCreditos/GetCreditosPorBus?idBus=" + idBus;
            StringRequest request = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{

                        //Obteniendo el objeto de la respuesta JSON
                        JSONObject object = new JSONObject(response);

                        //Convirtiendo la respuesta a un arreglo
                        JSONArray jsonArray = object.getJSONArray("Creditos");


                        //Inicializando la nueva lista de creditos
                        listaInterna = new ArrayList<>();

                        //Recorriendo el arreglo de creditos
                        for(int i=0; i<jsonArray.length();i++)
                        {
                            //Obteniendo el objeto del arreglo en la posicion i
                            JSONObject obj = jsonArray.getJSONObject(i);

                            //Convirtiendo en arreglo la lista de detalles
                            JSONArray objDet = obj.getJSONArray("DetallesDeCreditos");

                            //Inicializando la lista de detalles
                            listaDetalle = new ArrayList<>();

                            //Recorriendo la lista de detalles
                            for(int j =0; j<objDet.length();j++)
                            {
                                //Obteniendo el objeto del detalle en la posicion j
                                JSONObject det =  objDet.getJSONObject(j);

                                //Creando un nuevo objeto de detalle
                                tempDet = new DetalleDeCredito(det.getInt("Id"),
                                        det.getInt("ArticuloId"), det.getString("CodigoArticulo"),
                                        det.getString("Articulo"), det.getInt("Cantidad"),
                                        det.getDouble("Precio"));

                                //Agregando a lista de detalles
                                listaDetalle.add(tempDet);
                            }
                            String fechaResult ="";
                            Date fecha = null;
                            try{
                                fechaResult = obj.getString("Fecha").replace("/\"","" ).toString();
                            }catch(Exception e){
                                fechaResult = "07/22/2019";
                            }
                            try {
                                fecha = (Date) new SimpleDateFormat("MM/dd/yyyy").parse(fechaResult);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //Creando un nuevo objeto de Credito
                            temp = new Credito(obj.getInt("Id"), obj.getString("CodigoCredito"),
                                    fecha, obj.getDouble("MontoTotal"),
                                    obj.getDouble("TotalAbonado"), obj.getInt("NumeroAbonos"),
                                    obj.getBoolean("CreditoAnulado"), obj.getBoolean("EstadoDeCredito"),
                                    listaDetalle);


                            //Agregando a la lista de CreditosFragment
                            listaInterna.add(temp);
                        }

                        if(listaInterna.size() ==0)
                            Toast.makeText(getBaseContext(), "No hay Creditos", Toast.LENGTH_LONG ).show();
                        else
                        {
                            double sum=0;
                            for (Credito item:
                                 listaInterna) {
                                sum+=item.getMontoTotal();
                            }
                            textView.setText("Credito Total: C$" + sum);
                            AdapterCreditosBus credito = new AdapterCreditosBus(listaInterna, ActivityCredito.this);
                            recyclerView.setAdapter(credito);
                        }
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();


                    }catch (JSONException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            VolleySingleton.getInstance(ActivityCredito.this).addToRequestQueue(request);
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
