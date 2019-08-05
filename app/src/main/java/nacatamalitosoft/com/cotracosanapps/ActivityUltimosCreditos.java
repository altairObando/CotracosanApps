package nacatamalitosoft.com.cotracosanapps;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ActivityUltimosCreditos extends AppCompatActivity {

    ArrayList<Credito> listaCredito;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    int max=50;
    Button btnConsultar;
    AlertDialog _dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultimos_creditos);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(ActivityUltimosCreditos.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Obteniendo los Creditos");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new getCredito().execute();

        recyclerView  = (RecyclerView) findViewById(R.id.ReciclerId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));

        btnConsultar =(Button) findViewById(R.id.button1);
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityUltimosCreditos.this);
                View mView = getLayoutInflater().inflate(R.layout.dialogo_cantidad, null);
                final EditText dialogCan = (EditText)mView.findViewById(R.id.dialogCantidad);
                mBuilder.setView(mView);
                mBuilder.setTitle("Ingrese el numero de creditos");
                mBuilder.setPositiveButton("Consultar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        max = Integer.parseInt(String.valueOf(dialogCan.getText()));
                        progressDialog = new ProgressDialog(ActivityUltimosCreditos.this);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setMessage("Obteniendo los Creditos");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        new getCredito().execute();
                        closeDialog();
                    }
                });

                mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeDialog();
                    }
                });
                _dialog = mBuilder.show();
            }
        });
    }

    public class getCredito extends AsyncTask<Void, Void, Void> {

        ArrayList<Credito> listaInterna;
        Credito temp;
        ArrayList<DetalleDeCredito> listaDetalle;
        DetalleDeCredito tempDet;
        @Override
        protected Void doInBackground(Void... voids) {
            if(max==0)
                max=50;
            String uri = "http://cotracosan.tk/ApiCreditos/GetUltimosCreditos?max=" + max;
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
                            AdapterCreditosBus credito = new AdapterCreditosBus(listaInterna, ActivityUltimosCreditos.this);
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
            VolleySingleton.getInstance(ActivityUltimosCreditos.this).addToRequestQueue(request);
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

    void closeDialog()
    {
        if(_dialog!=null)
            _dialog.dismiss();

    }

}
