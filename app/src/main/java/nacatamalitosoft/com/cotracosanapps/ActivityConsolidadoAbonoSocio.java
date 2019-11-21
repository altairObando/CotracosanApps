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

import nacatamalitosoft.com.cotracosanapps.Modelos.Abonos;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class ActivityConsolidadoAbonoSocio extends AppCompatActivity {

    ArrayList<Abonos> listaAbonos;
    RecyclerView recyclerView;
    Button btnConsulta;
    TextView textView;
    int socioId;
    AlertDialog _dialog;
    ProgressDialog progressDialog;
    String fechaInicio="", fechaFin="";
    DecimalFormat formato = new DecimalFormat("#,###.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonos);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        socioId = (int)getIntent().getIntExtra("socioId", 0);
        textView = (TextView)findViewById(R.id.textView2);
        setDates();
        progressDialog = new ProgressDialog(ActivityConsolidadoAbonoSocio.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Obteniendo los CreditosFragment");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new getAbonos().execute();

        recyclerView = (RecyclerView)findViewById(R.id.ReciclerAbonos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));

        btnConsulta = (Button)findViewById(R.id.button1);
        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityConsolidadoAbonoSocio.this);
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
                            progressDialog = new ProgressDialog(ActivityConsolidadoAbonoSocio.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage("Obteniendo los CreditosFragment");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new getAbonos().execute();
                            closeDialog();
                        }
                        else
                            Toast.makeText(ActivityConsolidadoAbonoSocio.this, "Ingrese la fecha de inicio",
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
    }

    public class getAbonos extends AsyncTask<Void, Void, Void>
    {
        ArrayList<Abonos> listaInterna;
        Abonos abonos;
        @Override
        protected Void doInBackground(Void... voids) {
            String uri = "http://cotracosan.somee.com/ApiSocios/GetAbonosPorSocio?socioId=" + socioId
                    +"&fechaInicio=" +fechaInicio + "&fechaFin="+fechaFin;
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
                            String cadena = temp.getString("FechaDeAbono");
                            try{
                                cadena = cadena.replace("/\"","" ).toString();
                            }catch(Exception e){

                                cadena = "07/22/2019";
                            }
                            try{
                                fecha =(Date) new SimpleDateFormat("MM/dd/yyyy").parse(cadena);
                            }catch (Exception ex)
                            {
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                ex.getMessage();
                            }
                            abonos = new Abonos(temp.getInt("IdAbono"), temp.getInt("CreditoId"), temp.getInt("VehiculoId"),
                                    temp.getString("Placa"), fecha, temp.getString("CodigoAbono"), temp.getDouble("MontoDeAbono"),
                                    temp.getBoolean("AbonoAnulado"));

                            listaInterna.add(abonos);
                        }

                        listaAbonos = listaInterna;

                        if(listaInterna.size() ==0)
                            Toast.makeText(getBaseContext(), "No hay Abonos", Toast.LENGTH_LONG ).show();
                        else
                        {
                            double sumar=0;
                            for (Abonos item:
                                    listaInterna) {
                                sumar += item.getMontoDeAbono();
                            }
                            textView.setText("Total Abonado: C$ " + formato.format(sumar));
                            AdapterAbonos credito = new AdapterAbonos(listaInterna, ActivityConsolidadoAbonoSocio.this);
                            recyclerView.setAdapter(credito);
                        }

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();


                    }catch (JSONException ex)
                    {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(ActivityConsolidadoAbonoSocio.this, ex.getMessage(), Toast.LENGTH_LONG);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

                    Toast.makeText(ActivityConsolidadoAbonoSocio.this, error.getMessage(), Toast.LENGTH_LONG ).show();
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
}
