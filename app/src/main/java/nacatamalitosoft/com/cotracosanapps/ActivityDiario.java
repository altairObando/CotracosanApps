package nacatamalitosoft.com.cotracosanapps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityDiario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diario);

        getSupportActionBar().setTitle("");
    }
}
