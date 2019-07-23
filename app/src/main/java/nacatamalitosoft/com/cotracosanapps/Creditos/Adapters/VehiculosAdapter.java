package nacatamalitosoft.com.cotracosanapps.Creditos.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import nacatamalitosoft.com.cotracosanapps.Modelos.Buses;

public class VehiculosAdapter extends BaseAdapter {
    Context context;
    List<Buses> buses;

    public VehiculosAdapter(Context context, List<Buses> buses) {
        this.context = context;
        this.buses = buses;
    }

    @Override
    public int getCount() {
        return buses.size();
    }

    @Override
    public Buses getItem(int i) {
        return buses.get(i);
    }

    @Override
    public long getItemId(int position) {
        return buses.get(position).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if(view == null)
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, null);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(buses.get(i).getPlaca());
        return view;
    }
}
