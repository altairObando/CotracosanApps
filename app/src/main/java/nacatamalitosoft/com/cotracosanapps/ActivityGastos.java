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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import nacatamalitosoft.com.cotracosanapps.Modelos.ArticuloSubClass;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class ActivityGastos extends AppCompatActivity {

    Button btnConsultar;
    RecyclerView recyclerView;
    TextView textView;
    ProgressDialog progressDialog;
    AlertDialog _dialog;

    String fechaInicio="", fechaFin="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        btnConsultar = (Button)findViewById(R.id.button1);
        textView  = (TextView)findViewById(R.id.textView2);
        setDates();
        progressDialog = new ProgressDialog(ActivityGastos.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Obteniendo los Gastos");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new getGastos().execute();

        recyclerView = (RecyclerView)findViewById(R.id.ReciclerId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityGastos.this);
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
                            progressDialog = new ProgressDialog(ActivityGastos.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage("Obteniendo los Gastos");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new getGastos().execute();
                            closeDialog();
                        }
                        else
                            Toast.makeText(ActivityGastos.this, "Ingrese la fecha de inicio",
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

    public class  getGastos extends AsyncTask<Void, Void, Void>{

        ArrayList<ArticuloSubClass> listaArticulo;
        ArticuloSubClass articulo;
        @Override
        protected Void doInBackground(Void... voids) {
            String uri = "http://cotracosan.tk/ApiArticulos/getGastosPorArticulos?"
                    +"&fechaInicio=" +fechaInicio + "&fechaFin="+fechaFin;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("gastoPorRubro");
                        listaArticulo =  new ArrayList<>();
                        for(int i = 0; i<array.length(); i++)
                        {
                            JSONObject temp = array.getJSONObject(i);
                            articulo =  new ArticuloSubClass(temp.getString("CodigoDeArticulo"),
                                    temp.getString("DescripcionDeArticulo"),
                                    temp.getDouble("Gasto"));

                            listaArticulo.add(articulo);
                        }

                        if(listaArticulo.size()>0)
                        {
                            textView.setText("Gasto Total: C$ " + String.valueOf(SumaTotal(listaArticulo)));
                            AdapterGastos adapterGastos = new AdapterGastos(listaArticulo, ActivityGastos.this);
                            recyclerView.setAdapter(adapterGastos);
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                        else
                        {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(ActivityGastos.this, "No hay Gastos", Toast.LENGTH_LONG ).show();
                        }

                    }catch (JSONException ex)
                    {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                        Toast.makeText(ActivityGastos.this, ex.getMessage(), Toast.LENGTH_LONG ).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

                    Toast.makeText(ActivityGastos.this, error.getMessage(), Toast.LENGTH_LONG ).show();
                }
            });
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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

    double SumaTotal(ArrayList<ArticuloSubClass> list)
    {
        double sum = 0;
        for (ArticuloSubClass item:
             list) {
            sum+=item.getGasto();
        }
        return  sum;
    }

    void closeDialog()
    {
        if(_dialog!=null)
            _dialog.dismiss();

    }
}
