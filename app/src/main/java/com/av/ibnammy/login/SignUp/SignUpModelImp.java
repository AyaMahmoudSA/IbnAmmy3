package com.av.ibnammy.login.SignUp;


import com.av.ibnammy.networkUtilities.GetCallback;
import com.av.ibnammy.networkUtilities.ApiClient;
import com.av.ibnammy.networkUtilities.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mina on 3/1/2018.
 */

public class SignUpModelImp implements SignUpContract.SignUpModel {
    ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

    @Override
    public void requestSignUp(final String mobile, final String password,String email, int familyID, final GetCallback.onSignUpFinish listener) {

        String data="{'Mobile':"+mobile+",'Password':'"+password+"','Email':'"+email+"','FamliyID':'"+ familyID +"'}";

        Call<String> call = apiInterface.signUpApi(data);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //  success=true;
                String s = response.body();
                try {
                    JSONObject object = new JSONObject(s);
                    String status=object.getString("Status");
                    switch (status) {
                        case "Mobile already exists":
                            listener.onFailure("رقم الهاتف مسجل بالفعل!");
                            break;
                        case "Success":
                            listener.onSuccess("تم التسجيل بنجاح.", mobile);
                            break;
                        default:
                            listener.onFailure("حدث خطأ");
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //  success=false;
                listener.onFailure(t.getMessage());
            }
        });

    }
}
