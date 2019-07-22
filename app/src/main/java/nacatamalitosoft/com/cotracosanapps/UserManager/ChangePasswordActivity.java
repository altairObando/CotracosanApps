package nacatamalitosoft.com.cotracosanapps.UserManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import nacatamalitosoft.com.cotracosanapps.R;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText txtViejaContrasenia, contrasenia1, contrasenia2;
    Button btnCambiar, btnCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        txtViejaContrasenia = findViewById(R.id.etOldPassword);
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
    }
}
