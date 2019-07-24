package nacatamalitosoft.com.cotracosanapps.Creditos.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nacatamalitosoft.com.cotracosanapps.Modelos.Articulos;

public class ListaDetallesAdapter extends BaseAdapter {
    private final List<Articulos> data;
    private Context context;

    public ListaDetallesAdapter(Context context, List<Articulos> data){
        this.data = data;
        this.context = context;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Articulos getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if(view == null)
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, null);
        TextView textView1 = view.findViewById(android.R.id.text1);
        TextView textView2 = view.findViewById(android.R.id.text2);
        textView1.setText(data.get(i).getDescripcion());
        textView2.setText("C$ "+ data.get(i).getPrecio());

        return view;
    }

    public void updateDataSet(List<Articulos> newDataset){
        this.data.clear();
        this.data.addAll(newDataset);
        this.notifyDataSetChanged();
    }
}
