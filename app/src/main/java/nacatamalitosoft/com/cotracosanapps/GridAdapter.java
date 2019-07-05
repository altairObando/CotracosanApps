package nacatamalitosoft.com.cotracosanapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nacatamalitosoft.com.cotracosanapps.Modelos.Buses;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private List<Buses> arrayList;


    public  GridAdapter(Context context, List<Buses> buses)
    {
        this.context = context;
        this.arrayList = buses;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.item_bus,null);
        }
        TextView titulo = (TextView) view.findViewById(R.id.item_titulo);
        titulo.setText(arrayList.get(i).getPlaca());
        return view;
    }
}
