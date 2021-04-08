package com.app.fijirentalcars.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.fijirentalcars.LetsStartActivity;
import com.app.fijirentalcars.MainActivity;
import com.app.fijirentalcars.ProfileDetailActivity;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.SplashActivity;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    TextView tv_edit_profile, tv_transactionhistory, tv_favourites, tv_account, tv_fijiwork, tv_name, tv_logout, tv_signup, tv_leagal, tv_contact;
    CircleImageView profile_image;
    View view2;
    LinearLayout toolLayout;
    ImageView linearImage;
    TextView linearText;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.e("ProfileFragment", "ProfileFragment");

        initview(view);
        initlistner();


        if (TextUtils.isEmpty(FijiRentalUtils.getAccessToken(getContext()))) {

            tv_signup.setVisibility(View.VISIBLE);
            linearText.setVisibility(View.VISIBLE);
            tv_account.setVisibility(View.GONE);
            tv_transactionhistory.setVisibility(View.GONE);
            tv_favourites.setVisibility(View.GONE);
            tv_logout.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            toolLayout.setVisibility(View.GONE);
            linearImage.setVisibility(View.GONE);


        } else {

            tv_signup.setVisibility(View.GONE);
            linearText.setVisibility(View.GONE);
            tv_account.setVisibility(View.VISIBLE);
            tv_transactionhistory.setVisibility(View.VISIBLE);
            tv_favourites.setVisibility(View.VISIBLE);
            tv_logout.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            toolLayout.setVisibility(View.VISIBLE);
            linearImage.setVisibility(View.VISIBLE);


            if (FijiRentalUtils.isNetworkAvailable(getActivity())) {
                getprofiledetails();
            }
        }


        return view;
    }

    private void initview(View view) {
        profile_image = view.findViewById(R.id.profile_image);
        tv_edit_profile = view.findViewById(R.id.tv_edit_profile);
        tv_transactionhistory = view.findViewById(R.id.tv_transactionhistory);
        tv_favourites = view.findViewById(R.id.tv_favourites);
        tv_name = view.findViewById(R.id.tv_name);
        tv_account = view.findViewById(R.id.tv_account);
        tv_fijiwork = view.findViewById(R.id.tv_fijiwork);
        tv_logout = view.findViewById(R.id.tv_logout);
        tv_signup = view.findViewById(R.id.tv_login_signup);
        view2 = view.findViewById(R.id.view2);
        toolLayout = view.findViewById(R.id.tool_lay);
        linearImage = view.findViewById(R.id.tool_image);
        linearText = view.findViewById(R.id.linear_text);
        tv_leagal = view.findViewById(R.id.tv_leagal);
        tv_contact = view.findViewById(R.id.contact_support);

        if (!TextUtils.isEmpty(FijiRentalUtils.getDisplay_name(getActivity()))) {
            tv_name.setText(FijiRentalUtils.getDisplay_name(getActivity()));
        }

    }

    private void initlistner() {
        tv_edit_profile.setOnClickListener(this);
        tv_transactionhistory.setOnClickListener(this);
        tv_favourites.setOnClickListener(this);
        tv_account.setOnClickListener(this);
        tv_fijiwork.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
        tv_leagal.setOnClickListener(this);
        tv_contact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_edit_profile:

//                FragmentTransaction transactionprofile = getActivity().getSupportFragmentManager().beginTransaction();
//                transactionprofile.replace(R.id.container, EditProfileFragment.newInstance("", ""));
//                transactionprofile.addToBackStack(null);
//                transactionprofile.commit();

                Intent i = new Intent(getContext(), ProfileDetailActivity.class);
                startActivity(i);

                break;
            case R.id.tv_login_signup:

                Intent loginIntent = new Intent(getContext(), LetsStartActivity.class);
                startActivity(loginIntent);

                break;

            case R.id.tv_transactionhistory:

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, TransactionFragment.newInstance("", ""));
                transaction.addToBackStack(null);
                transaction.commit();

                break;

            case R.id.tv_favourites:

                FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.container, FavouritesFragment.newInstance("", ""));
                transaction1.addToBackStack(null);
                transaction1.commit();

                break;

            case R.id.tv_account:

                FragmentTransaction account = getActivity().getSupportFragmentManager().beginTransaction();
                account.replace(R.id.container, AccountFragment.newInstance("", ""));
                account.addToBackStack(null);
                account.commit();
                break;

            case R.id.tv_fijiwork:
                String url = "https://fijirentalcars.siddhidevelopment.com/help/";


                CustomTabsIntent tabsIntent1 = new CustomTabsIntent.Builder().build();
                tabsIntent1.intent.setPackage("com.android.chrome");
                tabsIntent1.launchUrl(getContext(), Uri.parse(url));


                break;
            case R.id.tv_leagal:
                String url2 = "https://fijirentalcars.siddhidevelopment.com/help/";

                CustomTabsIntent tabsIntent = new CustomTabsIntent.Builder().build();
                tabsIntent.intent.setPackage("com.android.chrome");
                tabsIntent.launchUrl(getContext(), Uri.parse(url2));
                break;
            case R.id.contact_support:
                String url3 = "https://fijirentalcars.siddhidevelopment.com/help/";

                CustomTabsIntent tabsIntent3 = new CustomTabsIntent.Builder().build();
                tabsIntent3.intent.setPackage("com.android.chrome");
                tabsIntent3.launchUrl(getContext(), Uri.parse(url3));
                break;


            case R.id.tv_logout:
                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                signOutUser();

//                                FirebaseAuth.getInstance().signOut();
//                                LoginManager.getInstance().logOut();
//
//
//                                SharedPreferences preferences = getActivity().getSharedPreferences("FijiRental", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = preferences.edit();
//                                editor.clear();
//                                editor.apply();
//                                Intent intent = new Intent(getActivity(), SplashActivity.class);
//                                startActivity(intent);
//                                getActivity().finishAffinity();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }

    }

    private void getprofiledetails() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> call = apiService.getprofiledetails(FijiRentalUtils.getAccessToken(getActivity()));


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

                            FijiRentalUtils.savePreferences(FijiRentalUtils.ID, "" + data.optString("ID"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_login, "" + data.optString("user_login"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_nicename, "" + data.optString("user_nicename"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_email, "" + data.optString("user_email"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.display_name, "" + data.optString("display_name"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.accessToken, "" + data.optString("accessToken"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_pic_url, "" + data.optString("user_pic_url"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.roles, "" + data.optString("role"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.about_desc, "" + data.optString("description"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.work, "" + data.optString("work"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.school, "" + data.optString("school"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.language, "" + data.optString("languages"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.about_desc, "" + data.optString("description"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.work, "" + data.optString("work"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.school, "" + data.optString("school"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.language, "" + data.optString("languages"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.push_notification, "" + data.optString("push_notification"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.mail_notification, "" + data.optString("mail_notification"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.is_car_listed, "" + data.optString("iscarlisted"), getActivity());


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, getActivity(), "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, getActivity(), "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, getActivity(), "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }

    private void signOutUser() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> call = apiService.logOut(FijiRentalUtils.getAccessToken(getActivity()));


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

                            FirebaseAuth.getInstance().signOut();
                            LoginManager.getInstance().logOut();


                            SharedPreferences preferences = getActivity().getSharedPreferences("FijiRental", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            Intent intent = new Intent(getActivity(), SplashActivity.class);
                            startActivity(intent);
                            getActivity().finishAffinity();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, getActivity(), "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, getActivity(), "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, getActivity(), "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }
}