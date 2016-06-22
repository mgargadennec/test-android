package fr.billetel.bolotusandroid.api.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maël Gargadennnec on 17/06/2016.
 */
public class ApiRootRequest implements ApiGetRequest {

  @Override
  public String getUrl() {
    return "https://www.fnacspectacles.mgargadennec.cardiweb.com/lotus-bo/api/";
  }

  @Override
  public Map<String, Object> getParameters() {
    return new HashMap<>();
  }
}
