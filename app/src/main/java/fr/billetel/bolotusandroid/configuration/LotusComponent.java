package fr.billetel.bolotusandroid.configuration;

import javax.inject.Singleton;

import dagger.Component;
import fr.billetel.bolotusandroid.modules.home.MainActivity;

/**
 * Created by Maël Gargadennnec on 21/06/2016.
 */
@Singleton
@Component(modules = {HATEOASModule.class, HttpModule.class})
public interface LotusComponent {
}
