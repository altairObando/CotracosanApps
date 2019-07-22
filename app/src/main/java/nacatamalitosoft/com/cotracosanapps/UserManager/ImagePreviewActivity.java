package nacatamalitosoft.com.cotracosanapps.UserManager;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import nacatamalitosoft.com.cotracosanapps.R;
import nacatamalitosoft.com.cotracosanapps.localDB.UserSingleton;

public class ImagePreviewActivity extends AppCompatActivity {
    Bitmap imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        // Barra para retornar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadImagenPerfil();
    }

    private void loadImagenPerfil() {
        new updateUserImage().execute();
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
    @SuppressLint("StaticFieldLeak")
    public class updateUserImage extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... voids) {
            String base64 = UserSingleton.getCurrentUser(getApplicationContext()).getAvatar().split(",")[1];
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView view = findViewById(R.id.imagenPerfilVistaPrevia);
            view.setImageBitmap(bitmap);
        }
    }
}
