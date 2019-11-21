package nacatamalitosoft.com.cotracosanapps;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import nacatamalitosoft.com.cotracosanapps.Creditos.CajaActivity;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;
import nacatamalitosoft.com.cotracosanapps.localDB.User;
import nacatamalitosoft.com.cotracosanapps.localDB.UserSingleton;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsuario, etPassword;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Chequear si el usuario ya ha inciado sesion.
        if(usuarioEstalogueado()){
            seleccionarMenu(UserSingleton.getCurrentUser(this).getRol());
            finish();
        }else{
            // Buscar las referencias a UI
            etUsuario = findViewById(R.id.etUsuario);
            etPassword = findViewById(R.id.etPassword);
            Button btnLogin = findViewById(R.id.btnLogin);
            Button btnCancelar = findViewById(R.id.btnCancelar);
            dialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IniciarSesion();
                }
            });
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private void IniciarSesion() {
        // Obtener el contenido
        String usuario = etUsuario.getText().toString().trim();
        String clave = etPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(usuario) && ! TextUtils.isEmpty(clave)){
            LoginTask task = new LoginTask(usuario, clave);
            task.execute();
        }else{
            Toast.makeText(getApplicationContext(), "Complete todos los campos de forma correcta", Toast.LENGTH_LONG).show();
        }
    }

    private boolean usuarioEstalogueado() {
        return UserSingleton.getCurrentUser(this) != null;
    }

    void MostrarPantallaPrincipal(){
        Intent i = new Intent(this, MainActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void MostrarPantallaCajero() {
        Intent i = new Intent(this, CajaActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    void MostrarPantallaContador()
    {
        Intent i = new Intent(this, ActivityContador.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @SuppressLint("StaticFieldLeak")
    public class LoginTask extends AsyncTask<Void, Void, Void> {

        private String user;
        private String pass;
        private User userResult;
        boolean login;
        String mensaje;
        LoginTask(String usuario, String password){
            this.user = usuario;
            this.pass = password;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // Url  de la consulta
            String uri = "http://cotracosan.somee.com/ApiAccount/IniciarSesion";
            StringRequest request = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject objectResponse = new JSONObject(response);
                        login = objectResponse.getBoolean("login");
                        mensaje = objectResponse.getString("message");
                        // Datos del usuario logueado.
                        if(login){
                            String id = objectResponse.getString("id");
                            String socioId = objectResponse.getString("socioId");
                            String username = objectResponse.getString("usuario");
                            String email = objectResponse.getString("email");
                            String imagen = objectResponse.getString("imagen");
                            String rol = objectResponse.getString("rol");
                            userResult = new User(id, socioId,username, email, "1", imagen, rol);
                            seleccionarMenu(rol);
                            UserSingleton.GuardarNuevoUsuario(getApplicationContext(), userResult);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                        }dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                    if(dialog.isShowing())
                        dialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", user);
                    params.put("contrasenia", pass);
                    return params;
                }
            };
            // Añadir peticion Http
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
            return null;
        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Autenticando usuario");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    private void seleccionarMenu(String rol) {
        if ("Socio".equals(rol)) {
            MostrarPantallaPrincipal();
        } else if ("Cajero".equals(rol)) {
            MostrarPantallaCajero();
        }
        else if ("Contador".equals(rol) || "Administrador".equals(rol)){
            MostrarPantallaContador();
        }
    }
}
