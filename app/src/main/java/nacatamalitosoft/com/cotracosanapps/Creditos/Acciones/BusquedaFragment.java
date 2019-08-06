package nacatamalitosoft.com.cotracosanapps.Creditos.Acciones;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nacatamalitosoft.com.cotracosanapps.ActivityCredito;
import nacatamalitosoft.com.cotracosanapps.AdapterCreditosBus;
import nacatamalitosoft.com.cotracosanapps.Modelos.Credito;
import nacatamalitosoft.com.cotracosanapps.Modelos.DetalleDeCredito;
import nacatamalitosoft.com.cotracosanapps.R;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class BusquedaFragment extends Fragment {


    public BusquedaFragment() {
        // Required empty public constructor
    }
    private RecyclerView resultados;
    private EditText txtBusqueda;
    private Button btnBuscar;
    private ProgressDialog progressDialog;
    private List<Credito> listaCredito;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_busqueda, container, false);
        InicializarComponentes(v);
        return v;
    }
    void InicializarComponentes(View v)
    {
        resultados = v.findViewById(R.id.recyclerResultados);
        txtBusqueda = v.findViewById(R.id.txtBuscarCredito);
        btnBuscar = v.findViewById(R.id.btnBuscarCreditos);
        listaCredito = new ArrayList<>();
        progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Obteniendo los Creditos");
        progressDialog.setCancelable(false);
        progressDialog.show();
        // Configurando recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        resultados.setLayoutManager(layoutManager);
        resultados.addItemDecoration(new DividerItemDecoration(resultados.getContext(), layoutManager.getOrientation()));
        new GetCredito().execute();
    }

    public class GetCredito extends AsyncTask<Void, Void, Void> {

        ArrayList<Credito> listaInterna;
        Credito temp;
        ArrayList<DetalleDeCredito> listaDetalle;
        DetalleDeCredito tempDet;
        @Override
        protected Void doInBackground(Void... voids) {
            String uri = "http://cotracosan.tk/ApiCreditos/GetCreditosPorBus";
            StringRequest request = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        //Obteniendo el objeto de la respuesta JSON
                        JSONObject object = new JSONObject(response);
                        //Convirtiendo la respuesta a un arreglo
                        JSONArray jsonArray = object.getJSONArray("Creditos");
                        //Inicializando la nueva lista de creditos
                        listaInterna = new ArrayList<>();
                        //Recorriendo el arreglo de creditos
                        for(int i=0; i<jsonArray.length();i++)
                        {
                            //Obteniendo el objeto del arreglo en la posicion i
                            JSONObject obj = jsonArray.getJSONObject(i);
                            //Convirtiendo en arreglo la lista de detalles
                            JSONArray objDet = obj.getJSONArray("DetallesDeCreditos");
                            //Inicializando la lista de detalles
                            listaDetalle = new ArrayList<>();
                            //Recorriendo la lista de detalles
                            for(int j =0; j<objDet.length();j++)
                            {
                                //Obteniendo el objeto del detalle en la posicion j
                                JSONObject det =  objDet.getJSONObject(j);
                                //Creando un nuevo objeto de detalle
                                tempDet = new DetalleDeCredito(det.getInt("Id"),
                                        det.getInt("ArticuloId"), det.getString("CodigoArticulo"),
                                        det.getString("Articulo"), det.getInt("Cantidad"),
                                        det.getDouble("Precio"));
                                //Agregando a lista de detalles
                                listaDetalle.add(tempDet);
                            }
                            String fechaResult ="";
                            Date fecha = null;
                            try{
                                fechaResult = obj.getString("Fecha").replace("/\"","" ).toString();
                            }catch(Exception e){
                                fechaResult = "07/22/2019";
                            }
                            try {
                                fecha = (Date) new SimpleDateFormat("MM/dd/yyyy").parse(fechaResult);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //Creando un nuevo objeto de Credito
                            temp = new Credito(obj.getInt("Id"), obj.getString("CodigoCredito"),
                                    fecha, obj.getDouble("MontoTotal"),
                                    obj.getDouble("TotalAbonado"), obj.getInt("NumeroAbonos"),
                                    obj.getBoolean("CreditoAnulado"), obj.getBoolean("EstadoDeCredito"),
                                    listaDetalle);
                            //Agregando a la lista de CreditosFragment
                            listaInterna.add(temp);
                        }

                        if(listaInterna.size() ==0)
                            Toast.makeText(getActivity(), "No hay Creditos", Toast.LENGTH_LONG ).show();
                        else
                        {
                            AdapterCreditosBus credito = new AdapterCreditosBus(listaInterna, getActivity());
                            resultados.setAdapter(credito);
                        }
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();


                    }catch (JSONException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
            return null;
        }
    }
}
