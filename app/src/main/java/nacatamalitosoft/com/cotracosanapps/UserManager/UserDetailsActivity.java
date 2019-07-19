package nacatamalitosoft.com.cotracosanapps.UserManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import nacatamalitosoft.com.cotracosanapps.R;
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
                // Tomar una foto

            }
        });
        // Lanzar la activdad para el cambio de la contraseña.
        cambiarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cargar el intent con el cambio de contraseña.
            }
        });

    }

    private void setValues() {
        // Cargar la imagen del perfil
        String base64 = currentUser.getAvatar();
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
}
