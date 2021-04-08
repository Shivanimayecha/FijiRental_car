package com.app.fijirentalcars.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.app.fijirentalcars.NotifiactionSetting;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView tv_email;
    LinearLayout editEmail, editPassword,editTransmission,editNotification;
    ImageView iv_back;

    public AccountFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        tv_email = view.findViewById(R.id.tv_email);
        iv_back = view.findViewById(R.id.iv_back);
        editEmail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);
        editTransmission = view.findViewById(R.id.lin_transmission);
        editNotification = view.findViewById(R.id.ll_notification);

        editEmail.setOnClickListener(this);
        editPassword.setOnClickListener(this);
        editTransmission.setOnClickListener(this);
        editNotification.setOnClickListener(this);

        if (!TextUtils.isEmpty(FijiRentalUtils.getUser_email(getActivity()))) {
            tv_email.setText(FijiRentalUtils.getUser_email(getActivity()));
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit_email) {
            showEditEmailDialog();
        } else if (v.getId() == R.id.edit_password) {
            showEditPasswordDialog();
        }else if (v.getId() == R.id.lin_transmission) {
            showTransmissionDialog();
        }else if (v.getId() == R.id.ll_notification) {
            Intent i=new Intent(getContext(), NotifiactionSetting.class);
            startActivity(i);
        }
    }

    private void showTransmissionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_transmission, null);
        builder.setView(dialogView);

        RadioGroup radioGroup=dialogView.findViewById(R.id.et_tranmission);

        RadioButton yesBtn,noBtn;
        yesBtn=radioGroup.findViewById(R.id.yes_btn);
        noBtn=radioGroup.findViewById(R.id.no_btn);

        noBtn.setChecked(true);


        TextView cancelBtn, okBtn;


        cancelBtn = dialogView.findViewById(R.id.cancel_btn);
        okBtn = dialogView.findViewById(R.id.ok_btn);

        AlertDialog dialog = builder.create();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getContext(), "clicke "+(yesBtn.isChecked()), Toast.LENGTH_SHORT).show();


//                RequestBody body;
//                MultipartBody.Builder builder = new MultipartBody.Builder();
//                builder.setType(MultipartBody.FORM);
//
//
//                builder.addFormDataPart("user_id", FijiRentalUtils.getId(getContext()));
////                builder.addFormDataPart("password", newPass.getText().toString());
//                builder.addFormDataPart("is_edit", "true");
//
//                body = builder
//                        .build();
//                updateprofile(body);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);


        dialog.show();

    }

    private void showEditPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_password_dialog, null);
        builder.setView(dialogView);

        EditText oldPass, newPass, renewPass;

        oldPass = dialogView.findViewById(R.id.et_old_pass);
        newPass = dialogView.findViewById(R.id.et_new_pass);
        renewPass = dialogView.findViewById(R.id.et_new_pass_re);

        TextView cancelBtn, okBtn;


        cancelBtn = dialogView.findViewById(R.id.cancel_btn);
        okBtn = dialogView.findViewById(R.id.ok_btn);

        AlertDialog dialog = builder.create();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(oldPass.getText().toString())) {
                    Toast.makeText(getContext(), "Empty old password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newPass.getText().toString())) {
                    Toast.makeText(getContext(), "Empty new password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(renewPass.getText().toString())) {
                    Toast.makeText(getContext(), "Empty re enter new password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newPass.getText().toString().equalsIgnoreCase(renewPass.getText().toString())){
                    Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!oldPass.getText().toString().trim().equalsIgnoreCase(FijiRentalUtils.getOldPass(getContext()))){
                    Toast.makeText(getContext(), "Invalid old password", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestBody body;
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);


                builder.addFormDataPart("user_id", FijiRentalUtils.getId(getContext()));
                builder.addFormDataPart("password", newPass.getText().toString());
                builder.addFormDataPart("is_edit", "true");

                body = builder
                        .build();
                updateprofile(body);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);


        dialog.show();
    }

    private void showEditEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_email_dialog, null);
        builder.setView(dialogView);

        EditText email = dialogView.findViewById(R.id.et_email);
        TextView cancelBtn, okBtn;

        email.setText(FijiRentalUtils.getUser_email(getActivity()));

        cancelBtn = dialogView.findViewById(R.id.cancel_btn);
        okBtn = dialogView.findViewById(R.id.ok_btn);

        AlertDialog dialog = builder.create();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(getContext(), "Empty email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!FijiRentalUtils.isValidEmail(email.getText().toString().trim())) {
                    Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.getText().toString().equalsIgnoreCase(FijiRentalUtils.getUser_email(getContext()))) {
                    dialog.dismiss();
                    return;
                }

                RequestBody body;
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);


                builder.addFormDataPart("user_id", FijiRentalUtils.getId(getContext()));
                builder.addFormDataPart("email", email.getText().toString().trim());
                builder.addFormDataPart("is_edit", "true");

                body = builder
                        .build();
                updateprofile(body);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);


        dialog.show();
    }


    private void updateprofile(RequestBody body) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);


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

                            updateDataView();
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

    private void updateDataView() {
        if (!TextUtils.isEmpty(FijiRentalUtils.getUser_email(getActivity()))) {
            tv_email.setText(FijiRentalUtils.getUser_email(getActivity()));
        }
    }

}
