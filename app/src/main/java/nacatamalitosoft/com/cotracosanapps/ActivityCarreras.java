package nacatamalitosoft.com.cotracosanapps;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import nacatamalitosoft.com.cotracosanapps.Modelos.Carreras;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class ActivityCarreras extends AppCompatActivity {
    ArrayList<Carreras> listaCarreras;
    TextView textView, textView1;
    RecyclerView recyclerView;
    int idBus;
    ProgressDialog progressDialog;
    AlertDialog _dialog;
    int max;
    Button btnDialog;
    DecimalFormat formato = new DecimalFormat("#,###.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carreras);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        idBus = (int)getIntent().getIntExtra("idBus", 0);
        textView = (TextView)findViewById(R.id.textView2);
        textView1 = (TextView)findViewById(R.id.textView3);
        if(idBus!=0)
        {
            progressDialog = new ProgressDialog(ActivityCarreras.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Obteniendo las Carreras");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new getCarreras().execute();

            recyclerView = (RecyclerView)findViewById(R.id.ReciclerCarrera);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));

            btnDialog = (Button)findViewById(R.id.button1);
            btnDialog.setText("CONSULTE CARRERAS");
            btnDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityCarreras.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialogo_cantidad, null);
                    final EditText dialogCan = (EditText)mView.findViewById(R.id.dialogCantidad);
                    mBuilder.setView(mView);
                    mBuilder.setTitle("Ingrese el numero de carreras");
                    mBuilder.setPositiveButton("Consultar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            max = Integer.parseInt(String.valueOf(dialogCan.getText()));
                            progressDialog = new ProgressDialog(ActivityCarreras.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage("Obteniendo los Creditos");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new getCarreras().execute();
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
            if(max==0)
                max=50;

            String uri = "http://cotracosan.somee.com/ApiCarreras/getCarrerasPorVehiculo?vehiculoId=" + idBus
                    +"&max=" + max;
            StringRequest request = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONArray array = new JSONObject(response).getJSONArray("carreras");
                        listaInterna = new ArrayList<>();
                        for(int i = 0; i<array.length();i++)
                        {
                            JSONObject jsonObject = array.getJSONObject(i);
                            String fecha = jsonObject.getString("FechaDeCarrera");
                            String [] fechaArray = fecha.split("/");
                            fecha="";
                            fecha = fechaArray[1] + "-" + fechaArray[0] +"-"+ fechaArray[2];
                                    temp = new Carreras(jsonObject.getInt("Id"), jsonObject.getString("CodigoCarrera"),
                                    fecha, jsonObject.getDouble("MontoRecaudado"),
                                    jsonObject.getDouble("Multa"), jsonObject.getString("Conductor"),
                                    jsonObject.getInt("ConductorId"), jsonObject.getString("Vehiculo"), jsonObject.getInt("VehiculoId"),
                                    jsonObject.getString("LugarFinalRecorrido"), jsonObject.getString("HoraDeLlegada"), jsonObject.getString("Turno"),
                                    jsonObject.getBoolean("CarreraAnulada"), jsonObject.getDouble("MontoRecaudado")- jsonObject.getDouble("Multa"));
                            listaInterna.add(temp);
                        }
                        if(listaInterna.size()>0)
                        {
                            double sum=0, sumMult=0;
                            for (Carreras item:
                                 listaInterna) {
                                sum+=item.getMontoRestante();
                                sumMult+=item.getMulta();
                            }
                            listaCarreras = listaInterna;
                            textView.setText("Monto Recaudado: C$ " + formato.format(sum));
                            textView1.setText("Multas: C$ "+ formato.format(sumMult));
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

    void closeDialog()
    {
        if(_dialog!=null)
            _dialog.dismiss();

    }
}
