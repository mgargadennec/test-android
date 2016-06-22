package fr.billetel.bolotusandroid.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;

import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.security.cert.CertificateException;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import fr.billetel.bolotusandroid.api.mapping.BoJacksonHalModule;

/**
 * Created by Maël Gargadennnec on 21/06/2016.
 */
@Module
public class HttpModule {

  @Provides
  @Singleton
  OkHttpClient provideOkHttpClient(){
    OkHttpClient httpClient = getUnsafeOkHttpClient();
    httpClient.setFollowRedirects(true);
    httpClient.setFollowSslRedirects(true);
    httpClient.setHostnameVerifier(new HostnameVerifier() {
      @Override
      public boolean verify(String hostname, SSLSession session) {
        return true;
      }
    });

    return httpClient;
  }

  @Provides
  @Singleton
  OkHttpClientHttpRequestFactory provideOkHttpClientHttpRequestFactory(OkHttpClient okHttpClient){
    return new OkHttpClientHttpRequestFactory(okHttpClient);
  }

  private OkHttpClient getUnsafeOkHttpClient() {
    try {
      final TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
          @Override
          public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
          }

          @Override
          public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
          }

          @Override
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
          }
        }
      };

      // Install the all-trusting trust manager
      final SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      // Create an ssl socket factory with our all-trusting manager
      final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

      OkHttpClient okHttpClient = new OkHttpClient();
      okHttpClient.setSslSocketFactory(sslSocketFactory);
      okHttpClient.setHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });

      return okHttpClient;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


}
