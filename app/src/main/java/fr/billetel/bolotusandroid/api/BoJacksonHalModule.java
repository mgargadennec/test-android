package fr.billetel.bolotusandroid.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.hateoas.core.Relation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BoJacksonHalModule extends SimpleModule {

  public BoJacksonHalModule(Class<?>[] resourceClasses) {
    setMixInAnnotation(Resource.class, ResourceMixin.class);
    addDeserializer(EmbeddedWrapper.class, new HalEmbeddedWrapperDeserializer(resourceClasses));
  }

  public static class HalEmbeddedWrapperDeserializer extends StdDeserializer<EmbeddedWrapper> {

    private Map<String, Class<?>> relationClasses;

    protected HalEmbeddedWrapperDeserializer(Class<?>[] resourceClasses) {
      super(EmbeddedWrapper.class);
      initEmbeddedResources(resourceClasses);
    }

    private void initEmbeddedResources(Class<?>[] resourceClasses) {
      relationClasses = new HashMap<String, Class<?>>();


      for (Class<?> resourceClass : resourceClasses) {
        Relation relation = resourceClass.getAnnotation(Relation.class);
        if (relation!=null) {
          relationClasses.put(relation.value(), resourceClass);
        }
      }
    }

    private static final long serialVersionUID = 4755806754621032622L;

    /*
     * (non-Javadoc)
     *
     * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser,
     * com.fasterxml.jackson.databind.DeserializationContext)
     */
    @Override
    public EmbeddedWrapper deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
        JsonProcessingException {
      JsonDeserializer<Object> deser = null;
      String currentParent = jp.getParsingContext().getParent().getCurrentName();
      if (relationClasses.get(currentParent) != null) {
          deser = ctxt.findRootValueDeserializer(SimpleType.construct(relationClasses.get(currentParent)));
          return new EmbeddedWrappers(false).wrap(deser.deserialize(jp, ctxt));
      }
      return null;
    }

  }

}
