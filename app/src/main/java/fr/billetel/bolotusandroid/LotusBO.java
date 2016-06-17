package fr.billetel.bolotusandroid;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

/**
 * Created by Maël Gargadennnec on 16/06/2016.
 */
public class LotusBO extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Iconify
      .with(new FontAwesomeModule());

  }
}
