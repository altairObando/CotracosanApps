package nacatamalitosoft.com.cotracosanapps;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import nacatamalitosoft.com.cotracosanapps.Modelos.Carreras;

public class AdapterCarreras extends RecyclerView.Adapter<AdapterCarreras.ViewHolderAdapterCarreras>
implements View.OnClickListener{

    public AdapterCarreras(ArrayList<Carreras> listaCarrera, Context context) {
        this.listaCarrera = listaCarrera;
        this.context = context;
    }

    ArrayList<Carreras> listaCarrera;
    private Context context;
    private View.OnClickListener listener;

    @NonNull
    @Override
    public AdapterCarreras.ViewHolderAdapterCarreras onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_carreras, null, false);
        view.setOnClickListener(this);

        return new  ViewHolderAdapterCarreras(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCarreras.ViewHolderAdapterCarreras viewHolderAdapterCarreras, final int i) {
        viewHolderAdapterCarreras.setData(listaCarrera.get(i));
        viewHolderAdapterCarreras.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Carreras carreras = listaCarrera.get(i);
                Intent intent = new Intent(context, ActivityDetalleCarrera.class);
                intent.putExtra("carrera", carreras);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaCarrera.size();
    }

    @Override
    public void onClick(View v) {
        if(listener!=null)
        {
            listener.onClick(v);
        }
    }

    public class ViewHolderAdapterCarreras extends RecyclerView.ViewHolder {
        TextView codigoCarrera, fechaCarrera, montoCarrera, conductor;

        public ViewHolderAdapterCarreras(@NonNull View itemView) {
            super(itemView);
            codigoCarrera = (TextView)itemView.findViewById(R.id.CodigoCarrera);
            fechaCarrera = (TextView)itemView.findViewById(R.id.FechaCarrera);
            montoCarrera = (TextView)itemView.findViewById(R.id.MontoCarrera);
            conductor = (TextView)itemView.findViewById(R.id.Conductor);
        }

        public void setData(Carreras carreras)
        {
            codigoCarrera.setText(carreras.getCodigoCarrera());
            fechaCarrera.setText(carreras.getFechaDeCarrera());
            montoCarrera.setText(String.valueOf(carreras.getMontoRestante()));
            conductor.setText(carreras.getConductor());
        }
    }
}
