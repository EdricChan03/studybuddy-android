package com.edricchan.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MyIntroActivity extends IntroActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addSlide(new SimpleSlide.Builder()
				.title("Add and remove tasks")
				.description("With the same features as a typical task and more!")
				.image(R.drawable.ic_onboarding_1)
				.background(R.color.colorTeal).build());
		addSlide(new SimpleSlide.Builder()
				.title("Sync with all your devices")
				.description("Sync all your tasks with all your devices instantly.")
				.image(R.drawable.ic_onboarding_2)
				.background(R.color.colorOrange).build());
		addSlide(new SimpleSlide.Builder()
				.title("As simple as 1, 2, 3")
				.description("To add a task, simply tap the plus button and fill in the form, then click save.")
				.image(R.drawable.ic_onboarding_3)
				.background(R.color.colorLime).build());
		addSlide(new SimpleSlide.Builder()
				.title("Sign in to continue")
				.description("To continue, sign up/ sign in with your preferred provider.")
				.buttonCtaLabel("Sign in")
				.buttonCtaClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(MyIntroActivity.this, LoginActivity.class);
						startActivity(intent);
					}
				})
				.background(R.color.colorAccent)
				.build());

		autoplay(2500, INFINITE);
	}
}
