package com.app.fijirentalcars;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fijirentalcars.Adapter.TripReviewAdapter;
import com.app.fijirentalcars.Model.PartnerDetail;
import com.app.fijirentalcars.Model.ReviewModel;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.app.fijirentalcars.util.PaginationScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripReviewScreen extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back;
    APIService apiService;
    RecyclerView reviewListView;
    TextView totalPost, avgRating;
    TripReviewAdapter adapter;
    String carItemId;
    int CarReview = 0;
    ProgressDialog progressDialog;
    List<ReviewModel> modelList = new ArrayList<>();
    List<String> responseList = new ArrayList<>();
    private static final int PAGE_START = 1;

    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;

    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;

    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 0;

    // indicates the current page which Pagination is fetching.
    private int currentPage = PAGE_START;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_review_screen);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        apiService = Config.getClient().create(APIService.class);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        initUi();
        getIntentData();
    }

    private void getIntentData() {
        if (getIntent().hasExtra(FijiRentalUtils.CarIntent)) {


            carItemId = getIntent().getStringExtra(FijiRentalUtils.CarIntent);
            FijiRentalUtils.Logger("TAGS", "Car vale " + carItemId);

            CarReview = getIntent().getIntExtra(FijiRentalUtils.CarReviewIntent, 0);

            avgRating.setText(String.valueOf(CarReview));
            isLoading = true;

            currentPage=PAGE_START;
            getTripReview(carItemId);
        }
    }

    private void getTripReview(String itemId) {

        HashMap<String, String> data = new HashMap<>();
        progressDialog.show();

        data.put("item_page_id", itemId);
        Call<ResponseBody> call = apiService.getReviewList(FijiRentalUtils.getAccessToken(this), data);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";

                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);
                        message = jsonobject.optString("message");
                        if (jsonobject.optString("code").matches("200")) {
                            JSONObject data = jsonobject.optJSONObject("data");
                            TOTAL_PAGES = Integer.parseInt(data.optString("max_num_pages"));
                            JSONArray data_array = data.optJSONArray("posts");
                            totalPost.setText(data.optString("totalreview") + " reviews");
                            passdata(data_array);

                        }
                    } catch (JSONException e) {
                        isLoading = false;
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, TripReviewScreen.this, "0");
                    }
                } catch (Exception e) {
                    isLoading = false;
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, TripReviewScreen.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoading = false;
                progressDialog.dismiss();
            }
        });

    }

    private void passdata(JSONArray array) {
        modelList.clear();
        isLoading = false;
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.optJSONObject(i);
            ReviewModel reviewModel = new ReviewModel();
            reviewModel.setWpcr3ReviewIp(object.optString("wpcr3_review_ip"));
            reviewModel.setWpcr3ReviewPost(object.optString("wpcr3_review_post"));
            reviewModel.setWpcr3ReviewName(object.optString("wpcr3_review_name"));
            reviewModel.setWpcr3ReviewEmail(object.optString("wpcr3_review_email"));
            reviewModel.setWpcr3ReviewRating(object.optString("wpcr3_review_rating"));
            reviewModel.setWpcr3ReviewTitle(object.optString("wpcr3_review_title"));
            reviewModel.setWpcr3ReviewWebsite(object.optString("wpcr3_review_website"));
            reviewModel.setEditLock(object.optString("_edit_lock"));
            reviewModel.setEditLast(object.optString("_edit_last"));
            reviewModel.setContent(object.optString("content"));
            reviewModel.setPostDate(object.optString("post_date"));
            reviewModel.setId(object.optInt("id"));
            reviewModel.setStars(object.optString("stars"));
            reviewModel.setWpcr3ReviewAdminResponse(object.optString("wpcr3_review_admin_response"));
            JSONArray array1 = object.optJSONArray("wpcr3_custom_fields");
            responseList.clear();
            for (int j = 0; j < array1.length(); j++) {
                responseList.add(array1.optString(j));
            }
            reviewModel.setWpcr3CustomFields(responseList);

            JSONObject partnerObject = object.optJSONObject("user");
            if (partnerObject != null) {

                PartnerDetail partnerDetail = new PartnerDetail();
                partnerDetail.setUserPicUrl(partnerObject.optString("user_pic_url"));
                partnerDetail.setFirstName(partnerObject.optString("first_name"));
                partnerDetail.setLastName(partnerObject.optString("last_name"));
                partnerDetail.setID(partnerObject.optInt("ID"));

                reviewModel.setUser(partnerDetail);
            }


            modelList.add(reviewModel);
        }

        adapter.notifyDataSetChanged();

    }

    private void initUi() {
        iv_back = findViewById(R.id.iv_back);
        reviewListView = findViewById(R.id.trip_review_list);
        avgRating = findViewById(R.id.avg_rating);
        totalPost = findViewById(R.id.total_review);
        iv_back.setOnClickListener(this);
        reviewListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new TripReviewAdapter(this, modelList);
        reviewListView.setAdapter(adapter);

        reviewListView.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) reviewListView.getLayoutManager()) {
            @Override
            protected void loadMoreItems() {
                    isLoading = true;
                    //Increment page index to load the next one
                    currentPage += 1;
                    getTripReview(carItemId);

            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return (TOTAL_PAGES==currentPage);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            onBackPressed();
        }
    }
}