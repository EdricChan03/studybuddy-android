package com.edricchan.studybuddy;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance("Add and remove tasks", "With the same features as a typical todo list.", R.drawable.ic_onboarding_1, getResources().getColor(R.color.colorTeal)));
        addSlide(AppIntroFragment.newInstance("Sync with all your devices", "Sync all your tasks to your other devices instantly.", R.drawable.ic_onboarding_2, getResources().getColor(R.color.colorLime)));
    }
    @Override
    public void onSkipPressed() {
        finish();
    }
    @Override
    public void onDonePressed() {
        finish();
    }
}
