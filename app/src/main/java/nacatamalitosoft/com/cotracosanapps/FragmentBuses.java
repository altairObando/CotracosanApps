package nacatamalitosoft.com.cotracosanapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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

import nacatamalitosoft.com.cotracosanapps.Modelos.Buses;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;


public class FragmentBuses extends Fragment {

    public FragmentBuses(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private GridView gridView;
    private GridAdapter adapter;
    private Context context;
    private List<Buses> buses;
    int socioId;
    public String mensaj ="vacio";

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buses, container, false);
        // A la hora de mostrar el fragmento de los buses se puede filtrar por el socioId
        // el valor del socioId es un valor entero que se puede obtener mediante una interfaz
        // que comunique desde MainActivity hacia el fragmentBuses
        socioId = 1; // Valor de prueba
        // Mostrar un cuadro de dialogo para saber si se esta obteniendo la informacion.
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Obteniendo tus vehiculos");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new BusesTask().execute();

        Toast.makeText(getContext(), this.mensaj, Toast.LENGTH_LONG);
       /* ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("M123456");
        arrayList.add("M654321");
        arrayList.add("M136521");
        arrayList.add("M246531");
        arrayList.add("M123456");
        arrayList.add("M123456");
        adapter = new GridAdapter(getContext(), arrayList);
        gridView.setAdapter(adapter);*/


        gridView = view.findViewById(R.id.mainGridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ActivityDiario.class);
                //intent.putExtra("id", buses.get(gridView.getSelectedItemPosition()).getId());
                startActivity(intent);
            }
        });
        return view;
    }

    // Esto no es necesario pero podria ser util
    // Crear una tarea asincronica para aligerar o separar los procesos en el hilo principal
    // Usando un hilo adicional.
    public class BusesTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            String uri = "http://cotracosan.tk/ApiVehiculos/getVehiculosPorSocio?socioId="+socioId;
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
                            Buses bus = new Buses(
                                    o.getInt("Id"),
                                    o.getInt("SocioId"),
                                    o.getString("Placa"),
                                    o.getBoolean("Estado")
                            );
                            // Agregamos el nuevo objeto a la lista.
                            buses.add(bus);
                        }
                        if(buses.size() == 0)
                            Toast.makeText(getContext(),"No posee vehiculos", Toast.LENGTH_SHORT).show();
                    // Estando fuera del for actualizamos el adaptador.
                        GridAdapter adapter = new GridAdapter(getContext(), buses);
                        gridView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mensaj = e.getMessage();
                    }
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });
            // Obtener la instancia unica para las solicitudes HTTP
            VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
            return null;
        }
    }
}
