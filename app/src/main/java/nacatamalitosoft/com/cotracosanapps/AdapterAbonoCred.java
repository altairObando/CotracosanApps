package nacatamalitosoft.com.cotracosanapps;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import nacatamalitosoft.com.cotracosanapps.Modelos.AbonoSubClass;

public class AdapterAbonoCred extends RecyclerView.Adapter<AdapterAbonoCred.ViewHolder> {

    public AdapterAbonoCred(ArrayList<AbonoSubClass> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    private ArrayList<AbonoSubClass> lista;
    private Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_abono_cred, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
            viewHolder.setData(lista.get(i));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView codigoAbono, codigoCredito, fechaAbono, montoAbono;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            codigoAbono = (TextView)itemView.findViewById(R.id.CodigoAbono);
            codigoCredito = (TextView)itemView.findViewById(R.id.CodigoCredito);
            fechaAbono = (TextView)itemView.findViewById(R.id.FechaAbono);
            montoAbono = (TextView)itemView.findViewById(R.id.MontoAbono);
        }

        public void setData(AbonoSubClass abonos)
        {
            codigoAbono.setText("Abono: " + abonos.getCodigoAbono());
            codigoCredito.setText("Credito: CRED-" +abonos.getCreditoId() );
            fechaAbono.setText("Fecha: " + abonos.getFechaAbono());
            montoAbono.setText("Monto: " + abonos.getMontoAbono());
        }
    }
}
