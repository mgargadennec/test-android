package fr.billetel.bolotusandroid.configuration;

import javax.inject.Singleton;

import dagger.Component;
import fr.billetel.bolotusandroid.modules.home.MainActivity;
import fr.billetel.bolotusandroid.splash.SplashScreenActivity;

/**
 * Created by Maël Gargadennnec on 21/06/2016.
 */
@Singleton
@Component(modules = {HATEOASModule.class, HttpModule.class})
public interface LotusComponent {

  void inject(MainActivity activity);

  void inject(SplashScreenActivity activity);

}
