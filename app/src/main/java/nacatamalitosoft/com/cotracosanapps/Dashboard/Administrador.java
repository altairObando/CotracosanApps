package nacatamalitosoft.com.cotracosanapps.Dashboard;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import nacatamalitosoft.com.cotracosanapps.MainActivity;
import nacatamalitosoft.com.cotracosanapps.R;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class Administrador extends Fragment {
    public Administrador(){}
    TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8;
    ProgressDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_administrador, container, false);
        txt1 = v.findViewById(R.id.txt1);
        txt2 = v.findViewById(R.id.txt2);
        txt3 = v.findViewById(R.id.txt3);
        txt4 = v.findViewById(R.id.txt4);
        txt5 = v.findViewById(R.id.txt5);
        txt6 = v.findViewById(R.id.txt6);
        txt7 = v.findViewById(R.id.txt7);
        txt8 = v.findViewById(R.id.txt8);
        new UpdateDashboard().execute();
        return v;
    }

    @SuppressLint("StaticFieldLeak")
    public class UpdateDashboard extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "http://cotracosan.somee.com/Home/GetDashboardData";
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject objectResponse = new JSONObject(response);
                        if(objectResponse != null)
                        {
                            // Primer lista.
                            JSONObject vehiculos = objectResponse.getJSONObject("vehiculo");
                            txt1.setText(vehiculos.getString("placa1"));
                            txt2.setText(vehiculos.getString("carreras"));
                            txt3.setText(vehiculos.getString("placa2"));
                            txt4.setText(vehiculos.getString("monto"));
                            JSONObject creditos = objectResponse.getJSONObject("credito");
                            txt5.setText(creditos.getString("nombre1"));
                            txt6.setText(creditos.getString("total1"));
                            txt7.setText(creditos.getString("nombre2"));
                            txt8.setText(creditos.getString("total2"));
                        }
                        if(dialog.isShowing())
                            dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(dialog.isShowing())
                            dialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(getActivity(), "Error al obtener los datos del dashboard!", Toast.LENGTH_SHORT).show();
                }
            });

            VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
            return null;
        }

        @Override
        protected void onPreExecute() {
            if(dialog == null)
            {
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Obteniendo datos");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
            }
            dialog.show();
        }
    }
}
