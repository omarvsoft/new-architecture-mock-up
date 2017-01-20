package client.api.cibp.evertec.com.android_client;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;


import com.evertec.cibp.api.sdk.authorization.LoginFacade;
import com.evertec.cibp.api.sdk.common.model.domain.UserNameValidationResponse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final Button button = (Button) findViewById(R.id.btnSign);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                signClien(v);
            }
        });

        System.setProperty("ci-bp.services.ahutorization-api.url", "http://10.0.2.2:8081/authentication/v1/");


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void signClien(View view) {

        LoginFacade login = new LoginFacade();

        TextView editClient = (TextView) findViewById(R.id.editClient);
        TextView editSecret = (TextView) findViewById(R.id.editSecret);

        TextView tvResponse    = (TextView) findViewById(R.id.tvResponse);
        TextView tvUserProfile = (TextView) findViewById(R.id.tvUserProfile);

        Boolean result =
                login.signClient(editClient.getText().toString(),
                        editSecret.getText().toString(),
                        "http://10.0.2.2:9090/authserver");

        if (!result) {
            System.out.println("Invalid credentials");

            tvResponse.setText("");
            tvUserProfile.setText("");

            AlertDialog alertInvalidCredentials = new AlertDialog.Builder(this)
                    .setTitle("Invalid client credentials")
                    .setMessage("Please, validate client id and client secret")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        } else {

            UserNameValidationResponse userNameValid = login.validateUser("omarvsoft");

            if (userNameValid.isSuccessful()) {

                tvResponse.setText(userNameValid.toString());
                tvUserProfile.setText(userNameValid.getCustomerProfile().toString());

            } else {
                System.out.println(":=(");
                System.out.println(userNameValid.getErrorMessage());
            }
        }
    }
}
