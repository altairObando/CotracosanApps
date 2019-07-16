package nacatamalitosoft.com.cotracosanapps;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class AdapterCreditosBus extends RecyclerView.Adapter<AdapterCreditosBus.ViewHolderAdapterCreditosBus> {
    @NonNull
    @Override
    public AdapterCreditosBus.ViewHolderAdapterCreditosBus onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCreditosBus.ViewHolderAdapterCreditosBus viewHolderAdapterCreditosBus, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderAdapterCreditosBus extends RecyclerView.ViewHolder {
        public ViewHolderAdapterCreditosBus(@NonNull View itemView) {
            super(itemView);
        }
    }
}
