package nacatamalitosoft.com.cotracosanapps.UserManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import nacatamalitosoft.com.cotracosanapps.R;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;
import nacatamalitosoft.com.cotracosanapps.localDB.UserSingleton;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText contraseniaVieja, contrasenia1, contrasenia2;
    Button btnCambiar, btnCancelar;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        contraseniaVieja = findViewById(R.id.etOldPassword);
        contrasenia1 = findViewById(R.id.etNewPassword);
        contrasenia2 = findViewById(R.id.etNewMatchPassword);
        btnCambiar = findViewById(R.id.btnCambiarContrasenia);
        btnCancelar = findViewById(R.id.btnCancelarCambio);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contraseniasValidas()){
                    new CambiarContraseniaTask(
                            UserSingleton.getCurrentUser(ChangePasswordActivity.this).getId(),
                            contraseniaVieja.getText().toString(),
                            contrasenia1.getText().toString()
                    ).execute();
                }else{
                    Toast.makeText(ChangePasswordActivity.this, "Contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    contraseniaVieja.setSelection(0, contraseniaVieja.getText().toString().length());
                    contraseniaVieja.requestFocus();
                    contrasenia1.setText("");
                    contrasenia2.setText("");
                }
            }
        });
        progressDialog = new ProgressDialog(ChangePasswordActivity.this);
    }

    private boolean contraseniasValidas() {
        return contrasenia1.getText().toString().equals(contrasenia2.getText().toString());
    }

    @SuppressLint("StaticFieldLeak")
    class CambiarContraseniaTask extends AsyncTask<Void, Void, Void>{
        private final String nuevaContraenia;
        private final String usuario;
        private final String contrasenia;
        private boolean completado = false;
        private String mensaje = "SIN CAMBIOS";

        CambiarContraseniaTask(String usuario, String contra, String nuevaContra) {
            this.usuario = usuario;
            this.contrasenia = contra;
            this.nuevaContraenia = nuevaContra;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String uri = "http://cotracosan.tk/ApiAccount/CambiarContraseña";
            StringRequest request = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject request = new JSONObject(response);
                        completado = request.getBoolean("completado");
                        mensaje = request.getString("mensaje");
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(ChangePasswordActivity.this, mensaje, Toast.LENGTH_LONG).show();
                        if(completado)
                        {
                            contraseniaVieja.setText("");
                            contrasenia1.setText("");
                            contrasenia2.setText("");
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ChangePasswordActivity.this, "Error de conversion", Toast.LENGTH_LONG).show();
                        contraseniaVieja.setSelection(0, contraseniaVieja.getText().toString().length());
                        contraseniaVieja.requestFocus();
                        contrasenia1.setText("");
                        contrasenia2.setText("");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("usuarioId", usuario);
                    params.put("viejaContrasenia", contrasenia);
                    params.put("nuevaContrasenia", nuevaContraenia);
                    return params;
                }
            };
            VolleySingleton.getInstance(ChangePasswordActivity.this).addToRequestQueue(request);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Ejecutando");
            progressDialog.setMessage("Realizando cambio de contraseña.");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }
}
