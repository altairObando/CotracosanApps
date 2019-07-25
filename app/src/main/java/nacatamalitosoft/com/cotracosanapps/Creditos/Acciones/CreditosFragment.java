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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import nacatamalitosoft.com.cotracosanapps.Creditos.Adapters.DetallesAdapter;
import nacatamalitosoft.com.cotracosanapps.Creditos.Adapters.ListaDetallesAdapter;
import nacatamalitosoft.com.cotracosanapps.Creditos.Adapters.VehiculosAdapter;
import nacatamalitosoft.com.cotracosanapps.Creditos.CajaActivity;
import nacatamalitosoft.com.cotracosanapps.Modelos.Articulos;
import nacatamalitosoft.com.cotracosanapps.Modelos.Buses;
import nacatamalitosoft.com.cotracosanapps.R;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class CreditosFragment extends Fragment {
    ProgressDialog dialog;
    List<Buses> listaBuses;
    Spinner spBuses;
    TextView tvCodigoCredito, totalCredito;
    Button btnBuscar;
    EditText textBusqueda;
    RecyclerView listaDetalles;
    List<Articulos> dataSet;
    DetallesAdapter adapter;
    FloatingActionButton fab;
    public CreditosFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_creditos, container, false);
        setHasOptionsMenu(true);
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
        return view;
    }

    private void setReferences(View view) {
        totalCredito = view.findViewById(R.id.tvTotalCredito);
        tvCodigoCredito = view.findViewById(R.id.tvCodigoCredito);
        spBuses = view.findViewById(R.id.spinnerVehiculos);
        dialog = new ProgressDialog(getContext());
        btnBuscar = view.findViewById(R.id.btnBuscarArticulos);
        textBusqueda = view.findViewById(R.id.etBuscarArticulo);
        listaDetalles = view.findViewById(R.id.listaDetalles);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listaDetalles.setLayoutManager(layoutManager);
        listaDetalles.setHasFixedSize(true);
        listaDetalles.addItemDecoration(new DividerItemDecoration(listaDetalles.getContext(), layoutManager.getOrientation()));

        dataSet = new ArrayList<>();
        adapter = new DetallesAdapter(getActivity(), new ArrayList<Articulos>(), new DetallesAdapter.adapterClick() {
            @Override
            public void onItemDeleteClick(Articulos articulos) {
                dataSet.remove(articulos);
                adapter.updateDataSet(dataSet);
                updateTotalCredito();
            }
        });
        listaDetalles.setAdapter(adapter);
        fab = view.findViewById(R.id.fab);
    }
    void updateTotalCredito(){
        double suma = 0;
        for(Articulos a: dataSet)
            suma += a.getPrecio();
        totalCredito.setText("C$ " + suma);
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
                if(dataSet.contains(articulo)) {
                    precioFinal += dataSet.get(dataSet.indexOf(articulo)).getPrecio();
                    dataSet.remove(articulo);
                }
                articulo.setPrecio(precioFinal);
                dataSet.add(articulo);
                updateTotalCredito();
                adapter.updateDataSet(dataSet);
                try{
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View v = getActivity().getCurrentFocus();
                    if(v == null)
                        v = new View(getActivity());
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_caja, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.menu.menu_caja)
        {
            if(dataSet.size() > 0){

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
