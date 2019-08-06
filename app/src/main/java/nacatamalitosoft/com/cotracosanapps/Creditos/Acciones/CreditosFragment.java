package nacatamalitosoft.com.cotracosanapps.Creditos.Acciones;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nacatamalitosoft.com.cotracosanapps.Creditos.Adapters.DetallesAdapter;
import nacatamalitosoft.com.cotracosanapps.Creditos.Adapters.VehiculosAdapter;
import nacatamalitosoft.com.cotracosanapps.Creditos.DetalleCreditoViewModel;
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
    static double total = 0;
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // verificar que se haya seleccionado almenos un articulo.
                if(dataSet.size() > 0){
                    final Buses bus = (Buses)(spBuses.getSelectedItem());
                    updateTotalCredito();
                    ResumenDialog dialog = ResumenDialog.newInstance("Aplicar el credito de: C$ "+ total + "" +
                            "\nal vehiculo con placa: " +bus.getPlaca()+" ? ");
                    dialog.listener = new ResumenDialog.onClickListener() {
                        @Override
                        public void onRealizarCreditoClick() {
                            new AgregarCreditoTask(bus.getId()).execute();
                        }
                    };
                    dialog.show(getFragmentManager(), "acreditar");
                }
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
        fab = view.findViewById(R.id.fab);
        /* Seccion para los detalles */
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
        /* Fin de los detalles */
    }
    void updateTotalCredito(){
        total = 0;
        for(Articulos a: dataSet)
            total += a.getPrecio();
        totalCredito.setText("C$ " + total);
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
    @SuppressLint("StaticFieldLeak")
    class AgregarCreditoTask extends AsyncTask<Void, Void, Void>{
        private final int vehiculoId;
        private List<DetalleCreditoViewModel> detalle;
        AgregarCreditoTask(int vehiculoId){
            detalle = new ArrayList<>();
            this.vehiculoId = vehiculoId;
        }
        String creditoUrlContent;
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, creditoUrlContent, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        boolean succes = object.getBoolean("success");
                        String mensaje = object.getString("message");
                        Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                        if(succes)
                            clearControls();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error al deserializar respuesta", Toast.LENGTH_LONG).show();
                    }
                    if(dialog.isShowing())
                        dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if(dialog.isShowing())
                        dialog.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("Id", ""+0);
                    parametros.put("CodigoCredito", tvCodigoCredito.getText().toString().split(":")[1].trim());
                    parametros.put("MontoTotal", ""+total);
                    parametros.put("EstadoDeCredito", "True");
                    parametros.put("FechaDeCredito", "2019-07-26");
                    parametros.put("CreditoAnulado", "False");
                    parametros.put("VehiculoId", ""+vehiculoId);

                    // Creando objeto para el detalle
                    JSONArray jsonArray = new JSONArray();
                    for (int i =0; i < detalle.size(); i++){
                        try {
                            JSONObject object = new JSONObject();
                            object.put("Id", detalle.get(i).getId());
                            object.put("Cantidad", detalle.get(i).getCantidad());
                            object.put("CreditoId", detalle.get(i).getCreditoId());
                            object.put("ArticuloId", detalle.get(i).getArticuloId());
                            jsonArray.put(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    parametros.put("DetalleCredito", jsonArray.toString());
                    return parametros;
                }
            };
            VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Efectuando nuevo credito...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.show();

            //Configurando url.
            creditoUrlContent = "http://cotracosan.tk/ApiCreditos/AddCredito";
            // Crear la lista del detalle
            for (Articulos a: dataSet){
                DetalleCreditoViewModel model = new DetalleCreditoViewModel(0, a.Cantidad,0,a.getId());
                detalle.add(model);
            }
        }
    }

    private void clearControls() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == 0){
                Articulos articulo = (Articulos) data.getExtras().get("articulo");
                int cantidad = data.getExtras().getInt("cantidad");
                updateDetalles(articulo, cantidad);
                updateTotalCredito();
                textBusqueda.setText("");
                try{
                    textBusqueda.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(textBusqueda.getWindowToken(), 0);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    void updateDetalles(Articulos articulos, int cantidad){
        // Establecer el valor cantidad
        articulos.Cantidad = cantidad;
        boolean updated = false;
        // Buscar el articulo
        for (int i =0; i< dataSet.size() && !updated; i++ ){
            // Si existe en el dataset
            if(dataSet.get(i).getId() == articulos.getId()){
                double nuevoPrecio = dataSet.get(i).getPrecio() + (articulos.getPrecio() * cantidad);
                dataSet.get(i).setPrecio(nuevoPrecio);
                dataSet.get(i).Cantidad+= cantidad;
                updated = true;
            }
        }
        // Si no se ha actualizado un articulo
        if(!updated)
        {
            articulos.setPrecio(articulos.getPrecio() * cantidad);
            dataSet.add(articulos);
        }
        // actualizamos el adapter
        adapter.updateDataSet(dataSet);
    }
}
