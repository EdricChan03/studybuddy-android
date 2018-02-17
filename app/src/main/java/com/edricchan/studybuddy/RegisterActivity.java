package com.edricchan.studybuddy;

import android.os.Bundle;
import android.app.Activity;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
