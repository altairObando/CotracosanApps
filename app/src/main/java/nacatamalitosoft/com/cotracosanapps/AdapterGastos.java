package nacatamalitosoft.com.cotracosanapps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import nacatamalitosoft.com.cotracosanapps.Modelos.ArticuloSubClass;

public class AdapterGastos extends RecyclerView.Adapter<AdapterGastos.ViewHolderAdapterGastos> {

    public AdapterGastos(ArrayList<ArticuloSubClass> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    private ArrayList<ArticuloSubClass> lista;
    private Context context;
    @NonNull
    @Override
    public ViewHolderAdapterGastos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_articulo_gasto, null, false);
        return  new ViewHolderAdapterGastos(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdapterGastos viewHolderAdapterGastos, int i) {
        viewHolderAdapterGastos.setData(lista.get(i));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolderAdapterGastos extends RecyclerView.ViewHolder {

        TextView codigoArticulo, articulo, gasto;

        public ViewHolderAdapterGastos(@NonNull View itemView) {
            super(itemView);
            codigoArticulo = (TextView)itemView.findViewById(R.id.CodigoArticulo);
            articulo = (TextView)itemView.findViewById(R.id.Articulo);
            gasto = (TextView)itemView.findViewById(R.id.Gasto);
        }

        public  void setData(ArticuloSubClass articuloSubClass)
        {
            codigoArticulo.setText("Codigo: " + articuloSubClass.getCodigo());
            articulo.setText("Articulo: " + articuloSubClass.getDescripcion());
            gasto.setText("Gasto: " + articuloSubClass.getGasto());
        }
    }
}
