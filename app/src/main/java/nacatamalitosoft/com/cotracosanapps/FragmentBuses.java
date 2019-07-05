package nacatamalitosoft.com.cotracosanapps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;



public class FragmentBuses extends Fragment {


    public FragmentBuses() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private GridView gridView;
    private  GridAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buses, container, false);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("M123456");
        arrayList.add("M654321");
        arrayList.add("M136521");
        arrayList.add("M246531");
        arrayList.add("M123456");
        arrayList.add("M123456");
        gridView = view.findViewById(R.id.mainGridView);
        adapter = new GridAdapter(getContext(), arrayList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ActivityDiario.class);
                startActivity(intent);
            }
        });


        return view;
    }


}
