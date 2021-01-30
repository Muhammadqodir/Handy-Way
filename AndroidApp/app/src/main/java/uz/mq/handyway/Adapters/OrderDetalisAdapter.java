package uz.mq.handyway.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uz.mq.handyway.CartUtils;
import uz.mq.handyway.HandyWayAPI;
import uz.mq.handyway.Models.CartModel;
import uz.mq.handyway.Models.GoodsModel;
import uz.mq.handyway.R;
import uz.mq.handyway.Utils;

public class OrderDetalisAdapter extends RecyclerView.Adapter<OrderDetalisAdapter.CartViewHolder>{
    Context context;
    ArrayList<CartModel> cartModels;
    ArrayList<GoodsModel> goodsModels;
    boolean isEditable;
    Runnable onEmpty;

    public OrderDetalisAdapter(Context context, ArrayList<CartModel> cartModels, ArrayList<GoodsModel> goodsModels, boolean isEditable, Runnable onEmpty) {
        this.context = context;
        this.cartModels = cartModels;
        this.goodsModels = goodsModels;
        this.isEditable = isEditable;
        this.onEmpty = onEmpty;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvQuantityPrice;
        ImageButton btnMinus, btnPlus, btnRemove;
        ImageView ivPic;
        TextView tvQuantity;
        LinearLayout llEdit;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            tvQuantityPrice = (TextView) itemView.findViewById(R.id.tvQuantityPrice);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            btnMinus = (ImageButton) itemView.findViewById(R.id.btnMinus);
            btnPlus = (ImageButton) itemView.findViewById(R.id.btnPlus);
            btnRemove = (ImageButton) itemView.findViewById(R.id.btnRemove);
            ivPic = (ImageView) itemView.findViewById(R.id.ivPic);
            llEdit = (LinearLayout) itemView.findViewById(R.id.llEdit);
        }
    }

    @NonNull
    @Override
    public OrderDetalisAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new OrderDetalisAdapter.CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDetalisAdapter.CartViewHolder holder, final int position) {
        final GoodsModel goodsModel = goodsModels.get(position);
        final CartModel cartModel = cartModels.get(position);
        holder.tvTitle.setText(goodsModel.getTitle());
        holder.tvQuantityPrice.setText(cartModel.getQuantity()+" x "+ Utils.convertPriceToString(cartModel.getPrice())+"\n"+Utils.convertPriceToString(cartModel.getQuantity()*cartModel.getPrice())+" "+context.getResources().getString(R.string.summ));
        holder.tvQuantity.setText(cartModel.getQuantity()+"");
        Picasso.get().load(HandyWayAPI.BASE_URL_MEDIA+goodsModel.getPic_url()).error(R.drawable.no_image).into(holder.ivPic);
        if (isEditable){
            holder.llEdit.setVisibility(View.VISIBLE);
        }else {
            holder.llEdit.setVisibility(View.GONE);
        }
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
                                if (cartModels.size() == 0){
                                    onEmpty.run();
                                }
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
                    cartModels.get(position).setQuantity(quantity);
                    cartModel.setQuantity(quantity);
                    holder.tvQuantityPrice.setText(cartModel.getQuantity()+" x "+ Utils.convertPriceToString(cartModel.getPrice())+"\n"+Utils.convertPriceToString(cartModel.getQuantity()*cartModel.getPrice())+" "+context.getResources().getString(R.string.summ));
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
                    cartModels.get(position).setQuantity(quantity);
                    holder.tvQuantityPrice.setText(cartModel.getQuantity()+" x "+ Utils.convertPriceToString(cartModel.getPrice())+"\n"+Utils.convertPriceToString(cartModel.getQuantity()*cartModel.getPrice())+" "+context.getResources().getString(R.string.summ));
                }else {
                    Toast.makeText(context, context.getResources().getString(R.string.max_quanity)+": "+goodsModel.getMax_quantity(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ArrayList<CartModel> getCartItems(){
        return cartModels;
    }

    public void removeAllItems(){
        cartModels.clear();
        goodsModels.clear();
        notifyDataSetChanged();
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
