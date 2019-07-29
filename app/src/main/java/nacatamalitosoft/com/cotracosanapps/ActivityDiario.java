package nacatamalitosoft.com.cotracosanapps;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class ActivityDiario extends AppCompatActivity {

    Button btnConsulta;
    TextView txtAbono, txtCarreras, txtCreditos;
    double carrera, credito, abono;
    String fechaInicio="", fechaFin="";
    AlertDialog _dialog;
    ProgressDialog progressDialog;
    int idBus;
    String onj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diario);
        idBus = getIntent().getIntExtra("idBus", 0);
        setDates();
        if(idBus!=0)
        {

            progressDialog = new ProgressDialog(ActivityDiario.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Obteniendo los Montos");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new ObtenerValores().execute();
            txtCarreras =(TextView) findViewById(R.id.txtCarreras);
            txtCreditos = (TextView) findViewById(R.id.txtCreditos);
            txtAbono = (TextView)findViewById(R.id.txtAbonos);
            btnConsulta= (Button)findViewById(R.id.button1);
            txtCarreras.setText(String.valueOf(carrera));
            txtCreditos.setText(String.valueOf(credito));
            txtAbono.setText(String.valueOf(abono));

            btnConsulta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityDiario.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_date, null);
                    final EditText edtInicio =  (EditText)mView.findViewById(R.id.editText2);
                    final EditText edtFinal = (EditText)mView.findViewById(R.id.editText3);
                    dialog.setView(mView);
                    dialog.setTitle("Consulta entre fechas");
                    dialog.setPositiveButton("Consultar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fechaInicio = ""; fechaFin="";
                            fechaInicio = String.valueOf(edtInicio.getText());
                            fechaFin = String.valueOf(edtFinal.getText());
                            if(fechaInicio!="")
                            {
                                if(fechaFin=="")
                                    fechaFin = fechaInicio;
                                progressDialog = new ProgressDialog(ActivityDiario.this);
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.setMessage("Obteniendo los Montos");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                new ObtenerValores().execute();

                                setText();
                                closeDialog();
                            }
                            else
                                Toast.makeText(ActivityDiario.this, "Ingrese la fecha de Inicio", Toast.LENGTH_LONG)
                                .show();
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
        else
        {
            Toast.makeText(getApplicationContext(), "No se puede hacer la operacion", Toast.LENGTH_LONG).show();
        }


    }

    public class ObtenerValores extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {



            //Realizando la consulta
            String uri = "http://cotracosan.tk/ApiVehiculos/getConsolidadoVehiculo?vehiculoId="+ idBus
                    +"&fechaInicio="+fechaInicio + "&fechaFin=" + fechaFin+"";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        carrera = object.getDouble("carreras");
                        credito = object.getDouble("creditos");
                        abono = object.getDouble("abonos");
                        onj = response;
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onj = error.getMessage();
                }
            });
            String jk = onj;
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            return null;
        }
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

    void closeDialog()
    {
        if(_dialog!=null)
            _dialog.dismiss();

    }



    public boolean compararfechas(String Inicio, String Fin) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        java.util.Date fecha1 = sdf.parse(Inicio, new ParsePosition(0));
        java.util.Date fecha2 = sdf.parse(Fin, new ParsePosition(0));
        if (fecha1.before(fecha2)) {
            return true;
        } else {
            return false;
        }
    }

    public void setText()
    {
        if(progressDialog.isShowing())
            progressDialog.dismiss();

        txtAbono.setText(String.valueOf(abono));
        txtCreditos.setText(String.valueOf(credito));
        txtCarreras.setText(String.valueOf(carrera));
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
}
