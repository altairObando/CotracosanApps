package nacatamalitosoft.com.cotracosanapps.UserManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import nacatamalitosoft.com.cotracosanapps.R;
import nacatamalitosoft.com.cotracosanapps.Web.VolleySingleton;
import nacatamalitosoft.com.cotracosanapps.localDB.User;
import nacatamalitosoft.com.cotracosanapps.localDB.UserSingleton;

public class UserDetailsActivity extends AppCompatActivity {

    private CircleImageView imagenPerfil;
    private ImageView optionsCamera;
    private TextView detalleUsuario, detalleCorreo, cambiarContrasenia;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        // Obtener la barra de herramientas desde el main activity
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        loadReferences();
        currentUser = UserSingleton.getCurrentUser(this);
        setValues();
        // Mostrar una actividad para visualizar una imagen.
        imagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imagePreview = new Intent(UserDetailsActivity.this, ImagePreviewActivity.class);
                startActivity(imagePreview);
            }
        });
        // Mostrar una actividad para cambiar la imagen.
        optionsCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(UserDetailsActivity.this);
                dialog.setContentView(R.layout.dialog_chosser);
                dialog.setTitle("Foto del perfil");
                LinearLayout rowCamara = dialog.findViewById(R.id.dialogCamara);
                LinearLayout rowGaleria = dialog.findViewById(R.id.dialogGaleria);
                rowCamara.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Tomar una foto
                        Intent tomarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(tomarFoto, 1);
                        dialog.dismiss();
                    }
                });
                rowGaleria.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // seleccionar desde la galeria
                        Intent seleccionarImagen = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(seleccionarImagen, 2);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        // Lanzar la activdad para el cambio de la contraseña.
        cambiarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio = new Intent(UserDetailsActivity.this, ChangePasswordActivity.class);
                startActivity(cambio);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode>=1 && requestCode <=2){
            if(resultCode == RESULT_OK){
                Uri selectedImage = data.getData();
                imagenPerfil.setImageURI(selectedImage);
                // Obteniendo el bmp
                BitmapDrawable drawable = (BitmapDrawable) imagenPerfil.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                new updateUserImage(bitmap).execute();
            }
        }
    }

    private void setValues() {
        // Cargar la imagen del perfil
        String base64 = currentUser.getAvatar().split(",")[1];
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imagenPerfil.setImageBitmap(bitmap);
        // Cargar informacion de detalles.
        detalleUsuario.setText(currentUser.getUsuario());
        detalleCorreo.setText(currentUser.getEmail());
    }

    private void loadReferences() {
        this.imagenPerfil = findViewById(R.id.imagenDetallePerfil);
        this.optionsCamera = findViewById(R.id.ivCameraOptions);
        this.detalleUsuario = findViewById(R.id.tvUsuarioDetalles);
        this.detalleCorreo = findViewById(R.id.tvCorreoDetalles);
        this.cambiarContrasenia = findViewById(R.id.tvCambiarContraseña);
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
    public class updateUserImage extends AsyncTask<Void, Void, Void>{
        private Bitmap bitmap;
        public updateUserImage(Bitmap bitmap){
            this.bitmap = bitmap;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] bytes = outputStream.toByteArray();
            final String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
            StringRequest request = new StringRequest(Request.Method.POST, "http://cotracosan.tk/ApiAccount/CambiarImagen", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(UserDetailsActivity.this, "Imagen actualizada", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UserDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    super.getParams();
                    Map<String, String> params = new HashMap<>();
                    params.put("idUsuario", currentUser.getId());
                    params.put("imagen", "data:image/jpeg;base64,"+temp);
                    return params;
                }
            };

            VolleySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
            return null;
        }
    }
}
