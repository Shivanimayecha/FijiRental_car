package com.app.fijirentalcars;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.fijirentalcars.fragments.EditProfileFragment;
import com.app.fijirentalcars.listners.onFragmentListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar, toolbar1;
    AppBarLayout appBarLayout;
    //    ImageView fab,backBtn;
    FloatingActionButton fab;
    TextView userName, userEmail, aboutUser, userdesc, userwork, userschool, userLang;
    CircleImageView userImage;
    boolean isFragmentLoad = false;

  /*  private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    final Uri imageUri = Uri.parse("http://i.imgur.com/VIlcLfg.jpg");

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private ImageView coverImage;
    private FrameLayout framelayoutTitle;
    private LinearLayout linearlayoutTitle;
    private Toolbar toolbar;
    private TextView textviewTitle;
    private CircleImageView avatar;*/


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void findViews() {
       /* appbar = (AppBarLayout)findViewById( R.id.appbar );
        collapsing = (CollapsingToolbarLayout)findViewById( R.id.collapsing );
        coverImage = (ImageView)findViewById( R.id.imageview_placeholder );
        framelayoutTitle = (FrameLayout)findViewById( R.id.framelayout_title );
        linearlayoutTitle = (LinearLayout)findViewById( R.id.linearlayout_title );
        toolbar = (Toolbar)findViewById( R.id.toolbar1 );
        textviewTitle = (TextView)findViewById( R.id.textview_title );
        avatar = findViewById(R.id.avatar);*/


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    ///  menuItem.setVisible(true);
                } else if (verticalOffset == 0) {
                    //  menuItem.setVisible(false);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   Fresco.initialize(this);
        setContentView(R.layout.activity_profile_detail);
        Log.e("ProfileDetailActivity", "ProfileDetailActivity");

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = findViewById(R.id.floating_action);
        fab.setOnClickListener(this);
//        backBtn =  findViewById(R.id.floating_action);
//        backBtn.setOnClickListener(this);
        setSupportActionBar(toolbar1);
        aboutUser = findViewById(R.id.about_user);
        userdesc = findViewById(R.id.user_desccription);
        userwork = findViewById(R.id.user_work);
        userschool = findViewById(R.id.user_school);
        userLang = findViewById(R.id.user_language);
        userImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.name);
        userEmail = findViewById(R.id.email_id);

//        userName.setText(FijiRentalUtils.getDisplay_name(this));
//        userEmail.setText(FijiRentalUtils.getUser_email(this));

        // getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getprofiledetails();

        //   setContentView(R.layout.activity_main);
        findViews();

       /* toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);

        setSupportActionBar(toolbar);
        startAlphaAnimation(textviewTitle, 0, View.INVISIBLE);

        //set avatar and cover
       // avatar.setImageURI(imageUri);
   //     avatar.setImageResource(R.drawable.profile);
        coverImage.setImageResource(R.drawable.car1);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_action:
                FragmentTransaction transactionprofile = getSupportFragmentManager().beginTransaction();
                onFragmentListner onfragmentListner = new onFragmentListner() {
                    @Override
                    public void onAttach() {
                        fab.setVisibility(View.GONE);
                    }

                    @Override
                    public void onDetach() {
                        fab.setVisibility(View.VISIBLE);
                        if(FijiRentalUtils.isProfileUpdate){
                            getprofiledetails();
                            FijiRentalUtils.isProfileUpdate=false;
                        }
                    }
                };
                transactionprofile.replace(R.id.container, EditProfileFragment.newInstance(onfragmentListner, ""));
                transactionprofile.addToBackStack(null);
                transactionprofile.commit();
                break;
        }
    }


    /*@Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }*/

   /* private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }*/

    private void getprofiledetails() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> call = apiService.getprofiledetails(FijiRentalUtils.getAccessToken(this));


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



                            Glide.with(ProfileDetailActivity.this).load(data.optString("user_pic_url")).placeholder(R.drawable.car1).into(userImage);
                            userName.setText(data.optString("display_name"));
                            userEmail.setText(data.optString("user_registered"));
                            aboutUser.setText("About " + data.optString("first_name"));
                            userdesc.setText(data.optString("description"));
                            userwork.setText(data.optString("work"));
                            userschool.setText(data.optString("school"));
                            userLang.setText(data.optString("languages"));



                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ProfileDetailActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ProfileDetailActivity.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ProfileDetailActivity.this, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }
}