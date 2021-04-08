package com.app.fijirentalcars.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.fijirentalcars.R;
import com.app.fijirentalcars.listners.onFragmentListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText ed_firstname, ed_lastname, ed_email, ed_languages;
    LinearLayout ll_submit;
    static onFragmentListner onfragmentListner;
    ImageView ivBack;

    public EditProfileFragment() {
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
    public static EditProfileFragment newInstance(onFragmentListner param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        onfragmentListner = param1;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onfragmentListner.onAttach();
    }

    @Override
    public void onDetach() {
        onfragmentListner.onDetach();
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("AccountFragment", "AccountFragment");
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);

        initview(view);
        return view;
    }

    private void initview(View view) {
        ed_firstname = view.findViewById(R.id.ed_firstname);
        ed_lastname = view.findViewById(R.id.ed_lastname);
        ed_email = view.findViewById(R.id.ed_email);
        ed_languages = view.findViewById(R.id.languages);
        ll_submit = view.findViewById(R.id.ll_submit);
        ivBack = view.findViewById(R.id.iv_back);

        FijiRentalUtils.Logger("TAGS"," data "+((FijiRentalUtils.getAbout_desc(getContext()).trim())));

        if (!TextUtils.isEmpty(FijiRentalUtils.getAbout_desc(getContext()).trim())) {
            ed_firstname.setText(FijiRentalUtils.getAbout_desc(getContext()));
        }
        if (!TextUtils.isEmpty(FijiRentalUtils.getWork(getContext()).trim())) {
            ed_lastname.setText(FijiRentalUtils.getWork(getContext()));
        }
        if (!TextUtils.isEmpty(FijiRentalUtils.getSchool(getContext()).trim())) {
            ed_email.setText(FijiRentalUtils.getSchool(getContext()));
        }
        if (!TextUtils.isEmpty(FijiRentalUtils.getLanguage(getContext()).trim())) {
            ed_languages.setText(FijiRentalUtils.getLanguage(getContext()));
        }


//        ed_lastname.setText(lastname);
//        ed_email.setText(FijiRentalUtils.getUser_email(getActivity()));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (TextUtils.isEmpty(ed_firstname.getText().toString().trim())) {
//                    Toast.makeText(getActivity(), "Enter First Name", Toast.LENGTH_SHORT).show();
//                } else if (TextUtils.isEmpty(ed_lastname.getText().toString().trim())) {
//                    Toast.makeText(getActivity(), "Enter Last Name", Toast.LENGTH_SHORT).show();
//                } else if (TextUtils.isEmpty(ed_email.getText().toString().trim())) {
//                    Toast.makeText(getActivity(), "Enter Email", Toast.LENGTH_SHORT).show();
//                } else {
                updateprofile(ed_firstname.getText().toString().trim(),
                        ed_lastname.getText().toString().trim(),
                        ed_email.getText().toString().trim(),
                        FijiRentalUtils.getId(getActivity()),
                        "");
//                }
            }
        });
    }

    private void updateprofile(String first_name, String last_name, String email, String user_id, String user_pic_url) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);

        RequestBody body;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (TextUtils.isEmpty(ed_firstname.getText().toString().trim())) {

        } else {
            builder.addFormDataPart("description", ed_firstname.getText().toString().trim());
        }

        if (TextUtils.isEmpty(ed_lastname.getText().toString().trim())) {

        } else {
            builder.addFormDataPart("work", ed_lastname.getText().toString().trim());
        }

        if (TextUtils.isEmpty(ed_email.getText().toString().trim())) {

        } else {
            builder.addFormDataPart("school", ed_email.getText().toString().trim());
        }

        if (TextUtils.isEmpty(ed_languages.getText().toString().trim())) {

        } else {
            builder.addFormDataPart("languages", ed_languages.getText().toString().trim());
        }

        builder.addFormDataPart("user_id", FijiRentalUtils.getId(getContext()));
        builder.addFormDataPart("is_edit", "true");

        body = builder
                .build();

        Call<ResponseBody> call = apiService.updateprofile(FijiRentalUtils.getAccessToken(getActivity()), body);

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

                        JSONObject data = jsonobject.optJSONObject("data");
                        message = jsonobject.optString("message");
                        if (data.optString("status").matches("200")) {

                            FijiRentalUtils.isProfileUpdate=true;

                            FijiRentalUtils.savePreferences(FijiRentalUtils.accessToken, "" + data.optString("accessToken"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.roles, "" + data.optString("role"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.ID, "" + data.optString("ID"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_login, "" + data.optString("user_login"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_nicename, "" + data.optString("user_nicename"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_email, "" + data.optString("user_email"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_pic_url, "" + data.optString("user_pic_url"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_activation_key, "" + data.optString("user_activation_key"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_status, "" + data.optString("user_status"), getActivity());
                            FijiRentalUtils.savePreferences(FijiRentalUtils.display_name, "" + data.optString("display_name"), getActivity());

                            getFragmentManager().popBackStack();
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
