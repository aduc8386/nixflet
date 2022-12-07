package dhcnhn.aduc8386.nixflet.controller.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.model.Cast;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastHolder> {

    private List<Cast> casts;

    public CastAdapter(List<Cast> casts) {
        this.casts = casts;
    }

    @NonNull
    @Override
    public CastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast, parent, false);

        return new CastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastHolder holder, int position) {
        Cast cast = casts.get(position);

        holder.textViewName.setText(cast.getName());
        Glide.with(holder.itemView.getContext())
                .load(String.format("https://image.tmdb.org/t/p/original/%s", cast.getProfilePath()))
                .error(R.drawable.ic_nixflet_text)
                .centerCrop()
                .into(holder.imageViewProfile);
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    static class CastHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewProfile;
        private final TextView textViewName;

        public CastHolder(@NonNull View itemView) {
            super(itemView);

            imageViewProfile = itemView.findViewById(R.id.imageview_item_cast_cast_profile);
            textViewName = itemView.findViewById(R.id.textview_item_cast_cast_name);

        }
    }

}
