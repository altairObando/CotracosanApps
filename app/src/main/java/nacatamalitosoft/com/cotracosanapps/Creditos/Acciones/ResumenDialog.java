package nacatamalitosoft.com.cotracosanapps.Creditos.Acciones;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class ResumenDialog extends DialogFragment {
    public onClickListener listener;
    public static ResumenDialog newInstance(String mensaje){
        ResumenDialog resumen = new ResumenDialog();
        Bundle bundle = new Bundle();
        bundle.putString("mensaje", mensaje);
        resumen.setArguments(bundle);
        return  resumen;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = this.getArguments();
        builder.setMessage(bundle.getString("mensaje"));
        builder.setPositiveButton("Realizar Credito", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onRealizarCreditoClick();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    public interface onClickListener{
        void onRealizarCreditoClick();
    }
}
