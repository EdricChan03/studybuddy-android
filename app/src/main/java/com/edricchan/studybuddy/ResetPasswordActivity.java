package com.edricchan.studybuddy;

import android.os.Bundle;
import android.app.Activity;

public class ResetPasswordActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
