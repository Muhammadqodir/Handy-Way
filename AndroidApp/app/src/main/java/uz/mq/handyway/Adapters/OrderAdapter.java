package uz.mq.handyway.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uz.mq.handyway.HandyWayAPI;
import uz.mq.handyway.Models.OrderModel;
import uz.mq.handyway.OrderDetalisActivity;
import uz.mq.handyway.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    ArrayList<OrderModel> models;
    Gson gson;
    static int[] ORDER_STATUS = new int[]
            {R.string.order_status_sended,
                    R.string.order_status_approved,
                    R.string.order_status_declined};
    static int[] ORDER_STATUS_COLORS = new int[]
            {R.color.colorText,
                    R.color.colorSuccess,
                    R.color.colorDanger};

    public OrderAdapter(Context context, ArrayList<OrderModel> models) {
        this.context = context;
        this.models = models;
        gson = new Gson();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvStatus;
        TextView tvDate;
        ImageView ivPreview;
        ImageButton btnEdit;
        LinearLayout llParent;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            ivPreview = (ImageView) itemView.findViewById(R.id.ivPreview);
            btnEdit = (ImageButton) itemView.findViewById(R.id.btnEdit);
            llParent = (LinearLayout) itemView.findViewById(R.id.item_parent);
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
//        position = getItemCount() - position - 1;
        final OrderModel model = models.get(position);
        holder.tvTitle.setText(context.getResources().getString(R.string.order)+" №"+model.getId());
        holder.tvDate.setText(model.getDate());
        holder.tvStatus.setText(ORDER_STATUS[model.getStatus()]);
        holder.tvStatus.setTextColor(context.getResources().getColor(ORDER_STATUS_COLORS[model.getStatus()]));
        Picasso.get().load(HandyWayAPI.BASE_URL+"order_preview_master/get_preview.php?order_id="+model.getId()).error(R.drawable.no_image).into(holder.ivPreview);
        if (model.isEditable()){
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, OrderDetalisActivity.class).putExtra("data", gson.toJson(model)));
                }
            });
        }else{
            holder.btnEdit.setVisibility(View.GONE);
        }
        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, OrderDetalisActivity.class).putExtra("data", gson.toJson(model)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
