package nacatamalitosoft.com.cotracosanapps;

import android.app.AlertDialog;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import nacatamalitosoft.com.cotracosanapps.Modelos.AbonoSubClass;
import nacatamalitosoft.com.cotracosanapps.Modelos.Credito;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class ActivityAbonosCred extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textView, textViewCredito;
    Credito credito;
    AlertDialog _dialog;
    ProgressDialog progressDialog;
    DecimalFormat formato = new DecimalFormat("#,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonos_cred);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        credito = (Credito)getIntent().getSerializableExtra("objetoCredito");

        if(credito!=null)
        {
            textView = (TextView)findViewById(R.id.textView2);
            textViewCredito = (TextView)findViewById(R.id.textView3);
            textViewCredito.setText(credito.getCodigoCredito());
            progressDialog = new ProgressDialog(ActivityAbonosCred.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Obteniendo los Abonos");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new getAbonos().execute();
            recyclerView = (RecyclerView)findViewById(R.id.ReciclerAbonos);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));
        }
        else
            Toast.makeText(ActivityAbonosCred.this, "Falló en relacionar el credito", Toast.LENGTH_LONG).show();

    }


    public class getAbonos extends AsyncTask<Void, Void, Void>
    {
        ArrayList<AbonoSubClass> listaInterna;
        AbonoSubClass abonos;
        @Override
        protected Void doInBackground(Void... voids) {
            String uri = "http://cotracosan.tk/ApiAbonos/AbonosPorCredito?creditoid=" + credito.getId();
            StringRequest request =  new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray("abonos");
                        listaInterna = new ArrayList<>();
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject temp = jsonArray.getJSONObject(i);
                            String cadena = temp.getString("FechaDeAbono");
                            String[] fechaArray = cadena.split("/");
                            String fecha = fechaArray[1] + "-" + fechaArray[0] + "-" + fechaArray[2];

                            /*try{
                                cadena = cadena.replace("/\"","" ).toString();
                            }catch(Exception e){

                                cadena = "07/22/2019";
                            }*/
                            abonos = new AbonoSubClass(temp.getInt("Id"), temp.getInt("CreditoId"), temp.getString("CodigoAbono"),
                                    fecha,  temp.getDouble("MontoDeAbono"));

                            listaInterna.add(abonos);
                        }

                        if(listaInterna.size() ==0)
                            Toast.makeText(ActivityAbonosCred.this, "No hay Abonos", Toast.LENGTH_LONG ).show();
                        else
                        {

                            textView.setText("Total Abonado: C$ " + formato.format(Suma(listaInterna)));
                            AdapterAbonoCred credito = new AdapterAbonoCred(listaInterna, ActivityAbonosCred.this);
                            recyclerView.setAdapter(credito);
                        }

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();


                    }catch (JSONException ex)
                    {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(ActivityAbonosCred.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

                    Toast.makeText(ActivityAbonosCred.this, error.getMessage(), Toast.LENGTH_LONG ).show();
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

    public double Suma(ArrayList<AbonoSubClass> lista)
    {
        double suma = 0;
        for (AbonoSubClass item:
             lista) {
            suma+=item.getMontoAbono();
        }
        return  suma;
    }
}
