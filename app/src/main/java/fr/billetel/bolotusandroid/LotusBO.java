package fr.billetel.bolotusandroid;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import fr.billetel.bolotusandroid.configuration.DaggerLotusComponent;
import fr.billetel.bolotusandroid.configuration.HATEOASModule;
import fr.billetel.bolotusandroid.configuration.HttpModule;
import fr.billetel.bolotusandroid.configuration.LotusComponent;
import fr.billetel.bolotusandroid.font.LotusFontModule;

/**
 * Created by Maël Gargadennnec on 16/06/2016.
 */
public class LotusBO extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Iconify
      .with(new FontAwesomeModule())
      .with(new LotusFontModule());

    DaggerLotusComponent.builder().build();
  }
}
