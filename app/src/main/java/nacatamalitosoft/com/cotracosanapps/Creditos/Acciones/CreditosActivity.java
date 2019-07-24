package nacatamalitosoft.com.cotracosanapps.Creditos.Acciones;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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

import nacatamalitosoft.com.cotracosanapps.Creditos.Adapters.ListaDetallesAdapter;
import nacatamalitosoft.com.cotracosanapps.Creditos.Adapters.VehiculosAdapter;
import nacatamalitosoft.com.cotracosanapps.Creditos.CajaActivity;
import nacatamalitosoft.com.cotracosanapps.Modelos.Articulos;
import nacatamalitosoft.com.cotracosanapps.Modelos.Buses;
import nacatamalitosoft.com.cotracosanapps.R;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class CreditosActivity extends Fragment {
    ProgressDialog dialog;
    List<Buses> listaBuses;
    Spinner spBuses;
    TextView tvCodigoCredito;
    Button btnBuscar;
    EditText textBusqueda;
    ListView listaDetalles;
    List<Articulos> dataSet;
    ListaDetallesAdapter adapter;
    FloatingActionButton fab;
    public CreditosActivity() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_creditos, container, false);
        setReferences(view);
        // Obtener los buses.
        new VehiculosTask().execute();
        // Obtener el codigo del credito nuevo
        new CreditosTask().execute();
        // Agregar escucha al boton buscar.
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los parametros de la busqueda
                String parametro = textBusqueda.getText().toString();
                Intent busqueda = new Intent(getActivity(), ResultadosBusquedaActivity.class);
                busqueda.putExtra("parametro", parametro);
                startActivityForResult(busqueda, 0);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Comprobar que existan elementos seleccionados.
                if(dataSet.size() > 0){
                    RealizarCreditoDialog dialog = new RealizarCreditoDialog();
                    dialog.listener = new RealizarCreditoDialog.onItemClickListener() {
                        @Override
                        public void onDialogPositiveClickListener(DialogFragment dialog) {

                        }
                    };
                    dialog.show(getActivity().getSupportFragmentManager(), "ComprobandoCredito");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Agregue un articulo", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private void setReferences(View view) {
        tvCodigoCredito = view.findViewById(R.id.tvCodigoCredito);
        spBuses = view.findViewById(R.id.spinnerVehiculos);
        dialog = new ProgressDialog(getContext());
        btnBuscar = view.findViewById(R.id.btnBuscarArticulos);
        textBusqueda = view.findViewById(R.id.etBuscarArticulo);
        listaDetalles = view.findViewById(R.id.listaDetalles);
        dataSet = new ArrayList<>();
        adapter = new ListaDetallesAdapter(getActivity().getApplicationContext(), new ArrayList<Articulos>());
        listaDetalles.setAdapter(adapter);
        fab = view.findViewById(R.id.fab);
    }

    // Cargar los creditos.
    @SuppressLint("StaticFieldLeak")
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
    // Obtener el ultimo codigo
    @SuppressLint("StaticFieldLeak")
    class CreditosTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.GET, "http://cotracosan.tk/ApiCreditos/GetUltimoCodigo", new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject response = new JSONObject(s);
                        String codigo = response.getString("codigoCredito").split("-")[1];
                        int nuevo = Integer.parseInt(codigo);
                        tvCodigoCredito.setText("Codigo: CRED-"+ (nuevo +1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Error al obtener el codigo del credito", Toast.LENGTH_SHORT).show();
                }
            });
            VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == 0){
                Articulos articulo = (Articulos) data.getExtras().get("articulo");
                int cantidad = data.getExtras().getInt("cantidad");
                double precioFinal = cantidad > 0 ? cantidad * articulo.getPrecio() :articulo.getPrecio();
                articulo.setPrecio(precioFinal);
                dataSet.add(articulo);
                adapter.updateDataSet(dataSet);
            }
        }
    }
}
