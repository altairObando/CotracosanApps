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

public class ArticulosAdapter extends RecyclerView.Adapter<ArticulosAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(Articulos item);
    }

    private final Context context;
    private final List<Articulos> data;
    private final OnItemClickListener listener;

    public ArticulosAdapter(Context context, List<Articulos> data, OnItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticulosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.resultados_busqueda_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticulosAdapter.ViewHolder vh, final int i) {
        vh.txtDescripcion.setText(data.get(i).getDescripcion());
        vh.txtCodigo.setText("Codigo: " + data.get(i).getCodigo());
        vh.txtPrecio.setText("C$ " + data.get(i).getPrecio());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(data.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescripcion, txtCodigo, txtPrecio;
        public ViewHolder(@NonNull View view) {
            super(view);
            txtDescripcion = view.findViewById(R.id.tvDescripcionArticulo);
            txtCodigo = view.findViewById(R.id.tvCodigoArticulo);
            txtPrecio = view.findViewById(R.id.tvPrecioArticulo);
            view.setTag(view);
        }
    }
}
