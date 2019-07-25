package nacatamalitosoft.com.cotracosanapps.Creditos.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nacatamalitosoft.com.cotracosanapps.Modelos.Articulos;
import nacatamalitosoft.com.cotracosanapps.R;

public class DetallesAdapter extends RecyclerView.Adapter<DetallesAdapter.ViewHolder> {
    private final Context context;
    private final List<Articulos> data;
    private adapterClick listener;
    public interface adapterClick{
        void onItemDeleteClick(Articulos articulos);
    }
    public DetallesAdapter(Context context, List<Articulos> data, adapterClick listener){
        this.context = context;
        this.data = data;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.resultados_busqueda_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, final int i) {
        vh.txtDescripcion.setText(data.get(i).getDescripcion());
        vh.txtPrecio.setText("");
        vh.txtPrecio.setCompoundDrawablesRelativeWithIntrinsicBounds(android.R.drawable.ic_delete, 0, 0,0);
        vh.txtCodigo.setText("Sub total: C$ " + data.get(i).getPrecio());
        vh.txtPrecio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onItemDeleteClick(data.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescripcion, txtCodigo, txtPrecio;
        public ViewHolder(@NonNull View view) {
            super(view);
            txtDescripcion = view.findViewById(R.id.tvDescripcionArticulo);
            txtCodigo = view.findViewById(R.id.tvCodigoArticulo);
            txtPrecio = view.findViewById(R.id.tvPrecioArticulo);
            view.setTag(view);
        }
    }

    public void updateDataSet(List<Articulos> newDataset){
        this.data.clear();
        this.data.addAll(newDataset);
        this.notifyDataSetChanged();
    }
}
