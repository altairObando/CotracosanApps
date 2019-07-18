package nacatamalitosoft.com.cotracosanapps;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nacatamalitosoft.com.cotracosanapps.Modelos.Credito;
import nacatamalitosoft.com.cotracosanapps.Modelos.DetalleDeCredito;

public class AdapterCreditosBus extends RecyclerView.Adapter<AdapterCreditosBus.ViewHolderAdapterCreditosBus> {

    public AdapterCreditosBus(ArrayList<Credito> lista) {
        this.lista = lista;
    }
    ArrayList<Credito> lista;

    @NonNull
    @Override
    public AdapterCreditosBus.ViewHolderAdapterCreditosBus onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_bus, null, false);
        return new ViewHolderAdapterCreditosBus(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCreditosBus.ViewHolderAdapterCreditosBus viewHolderAdapterCreditosBus, int i) {
        viewHolderAdapterCreditosBus.setData(lista.get(i));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolderAdapterCreditosBus extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView codigoCredito, montoCredito, fechaCredito, restoCredito;

        public ViewHolderAdapterCreditosBus(@NonNull View itemView) {
            super(itemView);
            imagen = (ImageView) itemView.findViewById(R.id.imagen);
            codigoCredito =(TextView) itemView.findViewById(R.id.CodigoCredito);
            montoCredito = (TextView)itemView.findViewById(R.id.MontoCredito);
            fechaCredito = (TextView) itemView.findViewById(R.id.FechaCredito);
            restoCredito = (TextView) itemView.findViewById(R.id.RestoCredito);
        }

        public void setData(Credito credito) {
            codigoCredito.setText(credito.getCodigoCredito());
            montoCredito.setText(String.valueOf(credito.getMontoTotal()));
            fechaCredito.setText(String.valueOf(credito.getFecha()));
            List<DetalleDeCredito> listaDetalle = credito.getDetallesDeCreditos();

        }
    }
}
