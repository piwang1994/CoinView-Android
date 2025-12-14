/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coinview.R;
import com.example.coinview.model.Cryptocurrency;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CryptocurrencyAdapter extends RecyclerView.Adapter<CryptocurrencyAdapter.CoinViewHolder> {

    private List<Cryptocurrency> coinList = new ArrayList<>();
    private OnItemClickListener listener;
    private DecimalFormat priceFormat = new DecimalFormat("$#,##0.00");
    private DecimalFormat percentFormat = new DecimalFormat("#0.00");

    public interface OnItemClickListener {
        void onItemClick(Cryptocurrency coin);
        void onFavoriteClick(Cryptocurrency coin);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cryptocurrency, parent, false);
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, int position) {
        Cryptocurrency coin = coinList.get(position);
        holder.bind(coin);
    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

    public void submitList(List<Cryptocurrency> newList) {
        coinList.clear();
        coinList.addAll(newList);
        notifyDataSetChanged();
    }

    class CoinViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCoinLogo;
        private TextView tvCoinName;
        private TextView tvCoinSymbol;
        private TextView tvPrice;
        private TextView tvPriceChange;
        private ImageView ivFavorite;

        public CoinViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCoinLogo = itemView.findViewById(R.id.iv_coin_logo);
            tvCoinName = itemView.findViewById(R.id.tv_coin_name);
            tvCoinSymbol = itemView.findViewById(R.id.tv_coin_symbol);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvPriceChange = itemView.findViewById(R.id.tv_price_change);
            ivFavorite = itemView.findViewById(R.id.iv_favorite);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(coinList.get(getAdapterPosition()));
                }
            });

            ivFavorite.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onFavoriteClick(coinList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Cryptocurrency coin) {
            tvCoinName.setText(coin.getName());
            tvCoinSymbol.setText(coin.getSymbol().toUpperCase());
            tvPrice.setText(priceFormat.format(coin.getCurrentPrice()));

            // Set price change
            double priceChange = coin.getPriceChangePercentage24h();
            String changeText = (priceChange >= 0 ? "+" : "") + percentFormat.format(priceChange) + "%";
            tvPriceChange.setText(changeText);

            // Set background and text color based on price change
            if (priceChange >= 0) {
                tvPriceChange.setBackgroundResource(R.drawable.bg_price_change_positive);
                tvPriceChange.setTextColor(itemView.getContext().getColor(R.color.price_up));
            } else {
                tvPriceChange.setBackgroundResource(R.drawable.bg_price_change_negative);
                tvPriceChange.setTextColor(itemView.getContext().getColor(R.color.price_down));
            }

            // Load coin logo
            if (coin.getImage() != null && !coin.getImage().isEmpty()) {
                Picasso.get()
                        .load(coin.getImage())
                        .placeholder(R.drawable.ic_coin_logo)
                        .error(R.drawable.ic_coin_logo)
                        .into(ivCoinLogo);
            }

            // Set favorite icon
            ivFavorite.setImageResource(coin.isFavorite() ?
                    R.drawable.ic_favorite : R.drawable.ic_favorite);
        }
    }
}
