package nacatamalitosoft.com.cotracosanapps;

import android.content.Context;
import android.content.Intent;
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

public class AdapterCreditosBus extends RecyclerView.Adapter<AdapterCreditosBus.ViewHolderAdapterCreditosBus> implements View.OnClickListener{

    private Context context;
    public AdapterCreditosBus(ArrayList<Credito> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }
    ArrayList<Credito> lista;
    private  View.OnClickListener listener;
    @NonNull
    @Override
    public AdapterCreditosBus.ViewHolderAdapterCreditosBus onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_credito, null, false);
        view.setOnClickListener(this);
        return new ViewHolderAdapterCreditosBus(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCreditosBus.ViewHolderAdapterCreditosBus viewHolderAdapterCreditosBus, final int i) {
        viewHolderAdapterCreditosBus.setData(lista.get(i));
        viewHolderAdapterCreditosBus.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Credito credito = lista.get(i);
                Intent intent = new Intent(context, DetalleCredito.class);
                intent.putExtra("objetoCredito", credito);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public  void setOnClickListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null)
        {
            listener.onClick(v);
        }
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
            montoCredito.setText("C$ "+String.valueOf(credito.getMontoTotal()));
            fechaCredito.setText(String.valueOf(credito.getFecha()));
            List<DetalleDeCredito> listaDetalle = credito.getDetallesDeCreditos();

        }
    }
}
