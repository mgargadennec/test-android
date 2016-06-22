package fr.billetel.bolotusandroid.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import fr.billetel.bolotusandroid.modules.home.MainActivity;
import fr.billetel.bolotusandroid.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {

  private final int SPLASH_DISPLAY_LENGTH = 3000;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.activity_splash_screen);

    new Handler().postDelayed(new Runnable(){
      @Override
      public void run() {
        Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
      }
    }, SPLASH_DISPLAY_LENGTH);
  }
}
