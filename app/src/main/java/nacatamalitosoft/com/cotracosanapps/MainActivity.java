package nacatamalitosoft.com.cotracosanapps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import nacatamalitosoft.com.cotracosanapps.UserManager.UserDetailsActivity;
import nacatamalitosoft.com.cotracosanapps.localDB.UserSingleton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        // Actualizar con la imagen del perfil
        new updateUserImage().execute();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, UserDetailsActivity.class);
            startActivity(i);
            return true;
        }else if(id == R.id.action_exit){
            // Cambiar el usuario a logged false.
            UserSingleton.CerrarSesion(MainActivity.this);
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Buses) {
            Intent i = new Intent(getApplicationContext(), ActivityBuses.class);
            i.putExtra("operacion", 1);
            i.putExtra("socioId", 1);
            startActivity(i);

        } else if (id == R.id.Carreras) {

            Intent i = new Intent(getApplicationContext(), ActivityBuses.class);
            i.putExtra("operacion", 2);
            i.putExtra("socioId", 1);
            startActivity(i);
        } else if (id == R.id.Creditos) {

            Intent i = new Intent(getApplicationContext(), ActivityBuses.class);
            i.putExtra("operacion", 3);
            i.putExtra("socioId", 1);
            startActivity(i);
        } else if (id == R.id.Abonos) {
            Intent i = new Intent(getApplicationContext(), ActivityBuses.class);
            i.putExtra("operacion", 4);
            i.putExtra("socioId", 1);
            startActivity(i);
        }
        else if(id == R.id.MontoFechas) {
            Intent i =  new Intent(getApplicationContext(), ActivityConsolidadoCarrera.class);
            i.putExtra("socioId", 1);
            startActivity(i);
        }
        else if (id == R.id.TodosCreditosBus) {
            Intent i = new Intent(getApplicationContext(), ActivityBuses.class);
            i.putExtra("operacion", 5);
            i.putExtra("socioId", 1);
            startActivity(i);
        } else if (id == R.id.TodosAbonosSocio) {

        }
        else if(id == R.id.TodosCreditosPendientesSocio)
        {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    public class updateUserImage extends AsyncTask<Void, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(Void... voids) {
            String base64 = UserSingleton.getCurrentUser(getApplicationContext()).getAvatar().split(",")[1];
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            View header = navigationView.getHeaderView(0);
            CircleImageView view = header.findViewById(R.id.imageViewUserImage);
            TextView usuario = header.findViewById(R.id.tvNombreDeUsuario);
            usuario.setText(UserSingleton.getCurrentUser(MainActivity.this).getUsuario());
            view.setImageBitmap(bitmap);
        }
    }
}
