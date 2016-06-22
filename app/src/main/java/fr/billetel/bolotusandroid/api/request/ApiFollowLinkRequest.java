package fr.billetel.bolotusandroid.api.request;

import org.springframework.hateoas.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maël Gargadennnec on 17/06/2016.
 */
public class ApiFollowLinkRequest implements ApiGetRequest {
  private Resource resource;
  private String relation;

  public ApiFollowLinkRequest(Resource resource, String relation) {
    this.resource = resource;
    this.relation = relation;
  }

  @Override
  public String getUrl() {
    return resource.getLink(relation)!=null ? resource.getLink(relation).getHref():null;
  }

  @Override
  public Map<String, Object> getParameters() {
    return new HashMap<>();
  }
}
