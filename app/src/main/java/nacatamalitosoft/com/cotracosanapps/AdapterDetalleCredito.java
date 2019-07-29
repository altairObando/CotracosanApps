package nacatamalitosoft.com.cotracosanapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nacatamalitosoft.com.cotracosanapps.Modelos.DetalleDeCredito;

public class AdapterDetalleCredito extends BaseAdapter {

    public AdapterDetalleCredito(Context context, ArrayList<DetalleDeCredito> listaDetalle) {
        this.context = context;
        this.listaDetalle = listaDetalle;
    }

    private Context context;
    private ArrayList<DetalleDeCredito> listaDetalle;

    @Override
    public int getCount() {
        return listaDetalle.size();
    }

    @Override
    public Object getItem(int position) {
        return listaDetalle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_detallecredito, null);
        }

        TextView codigoArticulo, nombreArticulo, cantidad, precio, subTotal;
        codigoArticulo = (TextView)convertView.findViewById(R.id.CodigoAbono);
        nombreArticulo = (TextView)convertView.findViewById(R.id.CodigoCredito);
        cantidad = (TextView)convertView.findViewById(R.id.FechaAbono);
        precio = (TextView)convertView.findViewById(R.id.P);
        subTotal = (TextView)convertView.findViewById(R.id.SubTotal);

        codigoArticulo.setText("Codigo: " + listaDetalle.get(position).getCodigoArticulo());
        nombreArticulo.setText("Articulo: " +listaDetalle.get(position).getNombreArticulo());
        cantidad.setText("Cantidad: " + listaDetalle.get(position).getCantidad());
        precio.setText("Precio: C$ " + listaDetalle.get(position).getPrecio());
        subTotal.setText("C$ "+String.valueOf(listaDetalle.get(position).getPrecio()*listaDetalle.get(position).getCantidad()));

        return convertView;
    }
}
