package uz.mq.handyway.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uz.mq.handyway.EditOrderActivity;
import uz.mq.handyway.HandyWayAPI;
import uz.mq.handyway.Models.CartModel;
import uz.mq.handyway.Models.GoodsModel;
import uz.mq.handyway.Models.OrderModel;
import uz.mq.handyway.OrdersActivity;
import uz.mq.handyway.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    ArrayList<OrderModel> models;
    static int[] ORDER_STATUS = new int[]
                    {R.string.order_status_sended,
                    R.string.order_status_sended,
                    R.string.order_status_sended};

    public OrderAdapter(Context context, ArrayList<OrderModel> models) {
        this.context = context;
        this.models = models;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvStatus;
        TextView tvDate;
        ImageView ivPreview;
        ImageButton btnEdit;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            ivPreview = (ImageView) itemView.findViewById(R.id.ivPreview);
            btnEdit = (ImageButton) itemView.findViewById(R.id.btnEdit);
        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        position = getItemCount() - position - 1;
        final OrderModel model = models.get(position);
        holder.tvTitle.setText(context.getResources().getString(R.string.order)+" №"+model.getId());
        holder.tvDate.setText(model.getDate());
        holder.tvStatus.setText(ORDER_STATUS[model.getStatus()]);
        Picasso.get().load(HandyWayAPI.BASE_URL+"order_preview_master/get_preview.php?order_id="+model.getId()).error(R.drawable.no_image).into(holder.ivPreview);
        if (model.isEditable()){
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, EditOrderActivity.class).putExtra("id", model.getId()));
                }
            });
        }else{
            holder.btnEdit.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
