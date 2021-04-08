package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteCarAdapter extends RecyclerView.Adapter<FavouriteCarAdapter.ViewHolder> {

    ArrayList<CarModel> carModelArrayList = new ArrayList<>();
    private Context context;

    // RecyclerView recyclerView;
    public FavouriteCarAdapter(ArrayList<CarModel> carModelArrayList, Context context) {
        this.carModelArrayList = carModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.car_item_layout_homefragment, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context)
                .load(FijiRentalUtils.BaseImageUrl+carModelArrayList.get(position).getItemImage1())
                .centerCrop()
                .placeholder(R.drawable.car1)
                .into(holder.iv_image);

        holder.tv_car_model.setText(carModelArrayList.get(position).getModelYear());
        holder.tv_carname.setText(carModelArrayList.get(position).getItemName());
        holder.tv_number_door.setText(carModelArrayList.get(position).getModelDoors());
        holder.tv_number_seat.setText(carModelArrayList.get(position).getModelSeats());
        holder.tv_price.setText("From $ " + carModelArrayList.get(position).getFixedRentalDeposit() + " per day");

        holder.iv_favourite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_favrorite_selected));

        holder.iv_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removefrom_favourites(carModelArrayList.get(position).getItemId(), position);
                holder.iv_favourite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_favrorite_unselected));
            }
        });

    }


    @Override
    public int getItemCount() {
        return carModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image, iv_favourite;
        public TextView tv_car_model, tv_carname, tv_number_seat, tv_number_door, tv_price;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
            iv_favourite = itemView.findViewById(R.id.iv_favourite);
            tv_car_model = itemView.findViewById(R.id.tv_car_model);
            tv_carname = itemView.findViewById(R.id.tv_carname);
            tv_number_seat = itemView.findViewById(R.id.tv_number_seat);
            tv_number_door = itemView.findViewById(R.id.tv_number_door);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }

    private void removefrom_favourites(String item_id, int postion) {

        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> call = apiService.removefrom_favourites(FijiRentalUtils.getAccessToken(context), item_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);
                        message = jsonobject.optString("message");
                        if (jsonobject.optString("code").matches("200")) {
                            carModelArrayList.remove(postion);
                            notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, context, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, context, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, context, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }
}