package uz.mq.handyway.Adapters;

import android.content.Context;
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
import java.util.List;

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
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        GoodsModel goodsModel = goodsModels.get(position);
        CartModel cartModel = cartModels.get(position);
        holder.tvTitle.setText(goodsModel.getTitle());
        holder.tvQuantityPrice.setText(cartModel.getQuantity()+" x "+ Utils.convertPriceToString(goodsModel.getPrice())+"\n"+Utils.convertPriceToString(cartModel.getQuantity()*goodsModel.getPrice())+" "+context.getResources().getString(R.string.summ));
        holder.tvQuantity.setText(cartModel.getQuantity()+"");
        Picasso.get().load(HandyWayAPI.BASE_URL_MEDIA+goodsModel.getPic_url()).error(R.drawable.no_image).into(holder.ivPic);
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
