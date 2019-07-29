package nacatamalitosoft.com.cotracosanapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nacatamalitosoft.com.cotracosanapps.Modelos.ConsolidadoCarrerasBus;

public class AdapterConsolidadoCarrera extends BaseAdapter {

    public AdapterConsolidadoCarrera(ArrayList<ConsolidadoCarrerasBus> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    ArrayList<ConsolidadoCarrerasBus> lista;
Context context;

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater =  (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_monto_carrera, null);
        }
        TextView txtPlaca, txtMonto;
        txtPlaca =convertView.findViewById(R.id.textView1);
        txtMonto = convertView.findViewById(R.id.textView2);
        txtPlaca.setText(lista.get(position).getPlaca());
        txtMonto.setText("C$ " +String.valueOf(lista.get(position).getMonto()));

        return convertView;
    }
}
