package nacatamalitosoft.com.cotracosanapps;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

import nacatamalitosoft.com.cotracosanapps.Modelos.Credito;
import nacatamalitosoft.com.cotracosanapps.Modelos.DetalleDeCredito;

public class ActivityCredito extends AppCompatActivity {

    ArrayList<Credito> listaCredito;
    RecyclerView recyclerView;
    int idBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listacredito);

        recyclerView  = (RecyclerView) findViewById(R.id.ReciclerId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public class getCredito extends AsyncTask<Void, Void, Void>{

        ArrayList<Credito> listaInterna;
        Credito temp;
        ArrayList<DetalleDeCredito> listaDetalle;
        DetalleDeCredito tempDet;
        @Override
        protected Void doInBackground(Void... voids) {
            String uri = "http://cotracosan.tk/ApiCreditos/GetCreditos?idBus=" + idBus;
            StringRequest request = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{

                        JSONObject object = new JSONObject(response);
                        temp.setId(object.getInt("Id"));
                        temp.setCodigoCredito(object.getString("CodigoCredito"));
                        temp.setFecha(Date.valueOf(object.getString("Fecha")));
                        temp.setMontoTotal(object.getDouble("MontoTotal"));
                        temp.setTotalAbonado(object.getDouble("TotalAbonado"));
                        temp.setNumeroAbonos(object.getInt("NumeroAbonos"));
                        temp.setCreditoAnulado(object.getBoolean("CreditoAnulado"));
                        temp.setEstadoCredito(object.getBoolean("EstadoDeCredito"));

                        JSONObject objecDet = new JSONObject((Map) object.getJSONObject("DetallesDeCreditos"));
                        tempDet.setId(objecDet.getInt("Id"));
                        tempDet.setArticuloId(objecDet.getInt("ArticuloId"));
                        tempDet.setCodigoArticulo(objecDet.getString("CodigoArticulo"));
                        tempDet.setNombreArticulo(objecDet.getString("Articulo"));
                        tempDet.setCantidad(objecDet.getInt("Cantidad"));
                        tempDet.setPrecio(objecDet.getDouble("Precio"));
                        listaDetalle.add(tempDet);



                    }catch (JSONException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            return null;
        }
    }
}
