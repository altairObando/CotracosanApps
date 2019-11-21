package nacatamalitosoft.com.cotracosanapps.Creditos.Acciones;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.EditText;
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
import java.util.Objects;

import nacatamalitosoft.com.cotracosanapps.Creditos.Adapters.ArticulosAdapter;
import nacatamalitosoft.com.cotracosanapps.Modelos.Articulos;
import nacatamalitosoft.com.cotracosanapps.R;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;

public class ResultadosBusquedaActivity extends AppCompatActivity implements ArticulosAdapter.OnItemClickListener {

    RecyclerView recyclerResultados;
    ProgressDialog dialog;
    String parametro;
    List<Articulos> articulos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_busqueda);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(ResultadosBusquedaActivity.this);
        parametro = getIntent().getExtras().getString("parametro");

        recyclerResultados = findViewById(R.id.recyclerResultados);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerResultados.setLayoutManager(layoutManager);
        recyclerResultados.setHasFixedSize(true);
        recyclerResultados.addItemDecoration(new DividerItemDecoration(recyclerResultados.getContext(), layoutManager.getOrientation()));

        new ArticulosTask().execute();
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

    @Override
    public void onItemClick(final Articulos item) {
        CantidadArticulosDialogFragment fragment = new CantidadArticulosDialogFragment();
        fragment.mlistener = new CantidadArticulosDialogFragment.NoticeDialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
                Dialog view = dialog.getDialog();
                EditText txtCantidad = view.findViewById(R.id.dialogCantidad);
                int cant = Integer.parseInt(txtCantidad.getText().toString());
                if(cant > 0){
                    Intent data = new Intent();
                    item.setDescripcion(cant+" Unidades de: " +item.getDescripcion());
                    data.putExtra("articulo", item);
                    data.putExtra("cantidad", cant);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            }
        };
        fragment.show(getSupportFragmentManager(), "Seleccionar_Cantidad");
    }
    @SuppressLint("StaticFieldLeak")
    class ArticulosTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String uri = "http://cotracosan.somee.com/ApiArticulos/getArticulos";
            StringRequest request = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                // Procesando peticion.
                try {
                    JSONArray response = new JSONObject(s).getJSONArray("result");
                    articulos = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject object = response.getJSONObject(i);
                        int id = object.getInt("Id");
                        String cod = object.getString("Codigo");
                        String desc = object.getString("Descripcion");
                        double precio = object.getDouble("Precio");
                        if(desc.contains(parametro) || desc.equals(""))
                            articulos.add(new Articulos(id, cod, desc, precio));
                    }
                    // Creando instancia del adaptador.
                    ArticulosAdapter adapter = new ArticulosAdapter(ResultadosBusquedaActivity.this, articulos, ResultadosBusquedaActivity.this);
                    recyclerResultados.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
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
        });
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Buscando articulos...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }
    }
}
