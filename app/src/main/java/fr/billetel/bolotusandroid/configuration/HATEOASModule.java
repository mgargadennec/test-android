package fr.billetel.bolotusandroid.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.billetel.bolotusandroid.api.HttpGetTask;
import fr.billetel.bolotusandroid.api.mapping.BoJacksonHalModule;
import fr.billetel.bolotusandroid.modules.administration.user.User;

/**
 * Created by Maël Gargadennnec on 21/06/2016.
 */
@Module
public class HATEOASModule {

  @Provides
  @Singleton
  Jackson2HalModule provideJackson2HalModule() {
    return new Jackson2HalModule();
  }


  @Provides
  @Singleton
  BoJacksonHalModule provideBoJacksonHalModule(Class<?>[] supportedClasses) {
    return new BoJacksonHalModule(supportedClasses);
  }

  @Provides
  @Singleton
  ObjectMapper provideObjectMapper(Jackson2HalModule jackson2HalModule, BoJacksonHalModule boJacksonHalModule) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(jackson2HalModule);
    mapper.registerModule(boJacksonHalModule);
    return mapper;
  }

  @Provides
  @Singleton
  MappingJackson2HttpMessageConverter provideMappingJackson2HttpMessageConverter(ObjectMapper mapper) {
    MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
    messageConverter.setObjectMapper(mapper);
    return messageConverter;
  }

  @Provides
  RestTemplate provideRestTemplate(ClientHttpRequestFactory requestFactory, MappingJackson2HttpMessageConverter messageConverter) {
    final RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(messageConverter);
    restTemplate.setRequestFactory(requestFactory);
    return restTemplate;
  }

}
