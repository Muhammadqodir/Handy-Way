package uz.mq.handyway.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uz.mq.handyway.CartUtils;
import uz.mq.handyway.HandyWayAPI;
import uz.mq.handyway.Models.CartModel;
import uz.mq.handyway.Models.GoodsModel;
import uz.mq.handyway.R;
import uz.mq.handyway.Utils;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    Context context;
    ArrayList<CartModel> cartModels;
    ArrayList<GoodsModel> goodsModels;

    public CartAdapter(Context context, ArrayList<CartModel> cartModels, ArrayList<GoodsModel> goodsModels) {
        this.context = context;
        this.cartModels = cartModels;
        this.goodsModels = goodsModels;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvQuantityPrice;
        ImageButton btnMinus, btnPlus, btnRemove;
        ImageView ivPic;
        TextView tvQuantity;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            tvQuantityPrice = (TextView) itemView.findViewById(R.id.tvQuantityPrice);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            btnMinus = (ImageButton) itemView.findViewById(R.id.btnMinus);
            btnPlus = (ImageButton) itemView.findViewById(R.id.btnPlus);
            btnRemove = (ImageButton) itemView.findViewById(R.id.btnRemove);
            ivPic = (ImageView) itemView.findViewById(R.id.ivPic);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        final GoodsModel goodsModel = goodsModels.get(position);
        final CartModel cartModel = cartModels.get(position);
        holder.tvTitle.setText(goodsModel.getTitle());
        holder.tvQuantityPrice.setText(cartModel.getQuantity()+" x "+ Utils.convertPriceToString(goodsModel.getPrice())+"\n"+Utils.convertPriceToString(cartModel.getQuantity()*goodsModel.getPrice())+" "+context.getResources().getString(R.string.summ));
        holder.tvQuantity.setText(cartModel.getQuantity()+"");
        Picasso.get().load(HandyWayAPI.BASE_URL_MEDIA+goodsModel.getPic_url()).error(R.drawable.no_image).into(holder.ivPic);
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.remove_from_cart)
                        .setMessage(R.string.confirm_action)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cartModels.remove(position);
                                goodsModels.remove(position);
                                CartUtils.removeItem(context, cartModel.getId());
                                notifyItemRemoved(position);
                                notifyItemRangeRemoved(position, cartModels.size());
                            }
                        }).setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString()) - 1;
                if (quantity >= goodsModel.getMin_quantity()){
                    holder.tvQuantity.setText(quantity+"");
                    CartUtils.changeItemQuanity(context, cartModel.getId(), quantity);
                }else {
                    Toast.makeText(context, context.getResources().getString(R.string.min_quanity)+": "+goodsModel.getMin_quantity(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString()) + 1;
                if (quantity <= goodsModel.getMax_quantity()){
                    holder.tvQuantity.setText(quantity+"");
                    CartUtils.changeItemQuanity(context, cartModel.getId(), quantity);
                }else {
                    Toast.makeText(context, context.getResources().getString(R.string.min_quanity)+": "+goodsModel.getMin_quantity(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    public void clearCart(){
        cartModels.clear();
        goodsModels.clear();
        notifyDataSetChanged();
    }
}
