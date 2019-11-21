package nacatamalitosoft.com.cotracosanapps;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import nacatamalitosoft.com.cotracosanapps.Modelos.ConsolidadoCarrerasBus;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class ActivityConsolidadoCarrera extends AppCompatActivity {

    Button btnConsulta;
    TextView txtTotal;
    GridView gridView;
    ArrayList<ConsolidadoCarrerasBus> buses;
    int socioId;
    AlertDialog _dialog;
    ProgressDialog progressDialog;
    String fechaInicio="", fechaFin="";
    DecimalFormat formato = new DecimalFormat("#,###.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monto_carreras);
        socioId = (int)getIntent().getIntExtra("socioId", 0);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        txtTotal =(TextView)findViewById(R.id.textView2);
        gridView = (GridView)findViewById(R.id.mainGridView);
        btnConsulta = (Button)findViewById(R.id.button1);

        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityConsolidadoCarrera.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_date, null);
                final EditText edtInicio =  (EditText)mView.findViewById(R.id.editText2);
                final EditText edtFinal = (EditText)mView.findViewById(R.id.editText3);
                dialog.setView(mView);
                dialog.setTitle("Consulta entre fechas");
                dialog.setPositiveButton("Consultar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fechaInicio = String.valueOf(edtInicio.getText());
                        fechaFin = String.valueOf(edtFinal.getText());
                        if(fechaInicio!="")
                        {
                            if(fechaFin=="")
                                fechaFin=fechaInicio;
                            progressDialog = new ProgressDialog(ActivityConsolidadoCarrera.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage("Obteniendo el Consolidado");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new BusesTask().execute();
                            closeDialog();
                        }
                        else
                            Toast.makeText(ActivityConsolidadoCarrera.this, "Ingrese la fecha de inicio",
                                    Toast.LENGTH_LONG).show();
                    }
                });

                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeDialog();
                    }
                });

                _dialog = dialog.show();
            }
        });

        if(socioId!=0)
        {
            setDates();
            progressDialog = new ProgressDialog(ActivityConsolidadoCarrera.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Obteniendo tus vehiculos");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new BusesTask().execute();

        }
        else
            Toast.makeText(ActivityConsolidadoCarrera.this, "No es posible realizar la operacion",
                    Toast.LENGTH_LONG).show();

    }

    public class BusesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            String uri = "http://cotracosan.somee.com/ApiVehiculos/getMontoTotalRecaudado?socioId="+socioId
                    +"&fechaInicio=" + fechaInicio + "&fechaFin="+fechaFin;
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
                            ConsolidadoCarrerasBus bus = new ConsolidadoCarrerasBus(
                                    o.getInt("Id"), o.getDouble("MontoRecaudado"),
                                    o.getString("Placa")
                            );
                            // Agregamos el nuevo objeto a la lista.
                            buses.add(bus);
                        }
                        if(buses.size() == 0)
                            Toast.makeText(getBaseContext(),"No posee vehiculos", Toast.LENGTH_SHORT).show();
                        // Estando fuera del for actualizamos el adaptador.
                        AdapterConsolidadoCarrera adapter = new AdapterConsolidadoCarrera(buses,ActivityConsolidadoCarrera.this);
                        gridView.setAdapter(adapter);
                        txtTotal.setText( "Monto Total: C$ " +formato.format(getSumTotalList(buses)));

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

    String getDateLastWeek()
    {
        Date myDate = new Date();

        String temp = new SimpleDateFormat("MM-dd-yyyy").format(myDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Calendar calendar = Calendar.getInstance();
        try{
            calendar.setTime(sdf.parse(temp));
        }catch (Exception ex)
        {
            ex.getMessage();
        }

        calendar.add(Calendar.DAY_OF_MONTH, -7);

        return  sdf.format(calendar.getTime());

    }

    void setDates()
    {
        //Obteniendo las fechas para realizar las consultas
        if(fechaFin=="")
        {
            Date myDate = new Date();
            fechaFin = new SimpleDateFormat("MM-dd-yyyy").format(myDate);
        }
        if(fechaInicio=="")
            fechaInicio=getDateLastWeek();
    }

    void closeDialog()
    {
        if(_dialog!=null)
            _dialog.dismiss();

    }

    double getSumTotalList(ArrayList<ConsolidadoCarrerasBus> lista)
    {
        double valor = 0;
        for (ConsolidadoCarrerasBus item:
             lista) {
            valor += item.getMonto();

        }
        return  valor;
    }
}
