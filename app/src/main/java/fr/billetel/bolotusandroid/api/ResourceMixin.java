package fr.billetel.bolotusandroid.api;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ResourceMixin<T> extends Resource<T> {

  public ResourceMixin(T content, Link[] links) {
    super(content, links);
  }

  @JsonIgnore(value = false)
  @Override
  public abstract Link getId();

}
