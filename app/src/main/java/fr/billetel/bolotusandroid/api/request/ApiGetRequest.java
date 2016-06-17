package fr.billetel.bolotusandroid.api.request;

import java.util.Map;

/**
 * Created by Maël Gargadennnec on 17/06/2016.
 */
public interface ApiGetRequest {
  String getUrl();

  Map<String,Object> getParameters();
}
