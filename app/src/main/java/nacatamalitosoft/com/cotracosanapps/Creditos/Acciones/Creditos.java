package nacatamalitosoft.com.cotracosanapps.Creditos.Acciones;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nacatamalitosoft.com.cotracosanapps.Creditos.Adapters.VehiculosAdapter;
import nacatamalitosoft.com.cotracosanapps.Creditos.CajaActivity;
import nacatamalitosoft.com.cotracosanapps.Modelos.Buses;
import nacatamalitosoft.com.cotracosanapps.R;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Creditos extends Fragment {
    ListView listaDetalle;
    ProgressDialog dialog;
    List<Buses> listaBuses;
    Spinner spBuses;
    public Creditos() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_creditos, container, false);
        listaDetalle = view.findViewById(R.id.listaDetalles);
        spBuses = view.findViewById(R.id.spinnerVehiculos);
        dialog = new ProgressDialog(getContext());
        // Obtener los buses.
        new VehiculosTask().execute();
        return view;
    }

    // Cargar los creditos.
    class VehiculosTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Sincronizando vehiculos...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            String uri ="http://cotracosan.tk/ApiVehiculos/getVehiculos";
        StringRequest request = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listaBuses = new ArrayList<>();
                try {
                    // convertir la respuesta a json
                    JSONArray vehiculos = new JSONObject(response).getJSONArray("vehiculos");
                    for (int i = 0; i < vehiculos.length(); i++){
                        JSONObject o = vehiculos.getJSONObject(i);
                        Buses b = new Buses(
                                o.getInt("Id"),
                                o.getInt("SocioId"),
                                o.getString("Placa"),
                                true);
                        listaBuses.add(b);
                    }
                    // Una vez obtenidos todos los buses seteamos el adaptador
                    VehiculosAdapter adapter = new VehiculosAdapter(getContext(), listaBuses);
                    spBuses.setAdapter(adapter);
                    if(dialog.isShowing())
                        dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(getContext(), "Error durante la sincronización", Toast.LENGTH_LONG).show();
            }
        });
            VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
            return null;
        }
    }

}
