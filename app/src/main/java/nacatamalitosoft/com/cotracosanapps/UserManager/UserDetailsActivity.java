package nacatamalitosoft.com.cotracosanapps.UserManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private ProgressDialog progressDialog;
    private String mCurrentPhotoPath;
    private Bitmap photoBitmap;
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
                        if(tomarFoto.resolveActivity(getPackageManager()) != null)
                        {
                            File photoFile = null;
                            try{
                                photoFile = createImageFile();
                            }catch (IOException ex)
                            {
                                Log.i("Camera Error", "Error al crear el archivo de imagen.");
                            }
                            if(photoFile !=null){
                                tomarFoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                startActivityForResult(tomarFoto, 1888);
                            }
                        }
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
        if(requestCode == 2){
            // de la galeria
            if(resultCode == RESULT_OK ){
                Uri selectedImage = data.getData();
                imagenPerfil.setImageURI(selectedImage);
                // Obteniendo el bmp
                BitmapDrawable drawable = (BitmapDrawable) imagenPerfil.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                new SubirImagenTask(bitmap, 50).execute();
            }
        }if(requestCode == 1888 && RESULT_OK == resultCode){
            try{
                photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                imagenPerfil.setImageBitmap(photoBitmap);
                new SubirImagenTask(photoBitmap, 30).execute();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
        this.progressDialog = new ProgressDialog(UserDetailsActivity.this);
        this.progressDialog.setMessage("Subiendo imagen");
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setCancelable(false);
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

    public class SubirImagenTask extends AsyncTask<Void, Void, Boolean>
    {
        String NAMESPACE = "http://cotracosan.tk/";
        String METHOD_NAME = "CambiarImagen";
        String SOAPAction = "http://cotracosan.tk/CambiarImagen";
        String SURL = "http://cotracosan.tk/Manage.asmx";
        Bitmap bitmap;
        int size;
        boolean result = false;
        byte[] bytes;
        public SubirImagenTask(Bitmap bmp, int size)
        {
            this.bitmap = bmp;this.size = size;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
//        Toast.makeText(context, "Procesando subida de imagen", Toast.LENGTH_SHORT).show();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP, size, outputStream);
            bytes = outputStream.toByteArray();

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            //Request.addProperty("imagen", bytes);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            new MarshalBase64().register(envelope);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(Request);
            HttpTransportSE transportSE = new HttpTransportSE(SURL);
            PropertyInfo p = new PropertyInfo();
            p.setName("imagen");
            p.setValue(bytes);
            p.setType(MarshalBase64.BYTE_ARRAY_CLASS);

            Request.addProperty(p);

            PropertyInfo p2 = new PropertyInfo();
            p2.setName("idUsuario");
            p2.setValue(UserSingleton.getCurrentUser(UserDetailsActivity.this).getId());
            p2.setType("".getClass());
            Request.addProperty(p2);

            try{
                URL uri = new URL(SURL);
                URLConnection connection = uri.openConnection();
                connection.setConnectTimeout(20 * 1000);
                HttpURLConnection urlConnection = (HttpURLConnection)connection;
                if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    urlConnection.disconnect();
                    transportSE.call(SOAPAction, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    result = Boolean.parseBoolean(response.getValue().toString());
                }
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            if(!progressDialog.isShowing())
            {
                progressDialog.setMessage("Subiendo imagen");
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(result)
            {
                Toast.makeText(UserDetailsActivity.this, "Se ha actualizado la imagen", Toast.LENGTH_LONG).show();
                String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
                UserSingleton.ActualizarImagen(base64, UserDetailsActivity.this);
            }else{
                Toast.makeText(UserDetailsActivity.this, "Error al actualizar", Toast.LENGTH_LONG).show();
                setValues();
            }
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "JPG_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );
        File image = File.createTempFile(filename, ".jpg", storageDir);
        mCurrentPhotoPath = "file:"+image.getAbsolutePath();
        return image;
    }

}
