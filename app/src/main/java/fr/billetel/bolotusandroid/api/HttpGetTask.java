package fr.billetel.bolotusandroid.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;

import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import fr.billetel.bolotusandroid.api.request.ApiGetRequest;

/**
 * Created by Maël Gargadennnec on 17/06/2016.
 */
public class HttpGetTask<Progress, Result> extends AsyncTask<ApiGetRequest, Progress, Result> {
  private Activity activity;
  private HttpTaskDelegate<Result> delegate;
  private Class<Result> resultClass;

  public HttpGetTask(Activity activity, HttpTaskDelegate delegate, Class<Result> resultClass) {
    this.activity = activity;
    this.delegate = delegate;
    this.resultClass = resultClass;
  }

  @Override
  protected Result doInBackground(ApiGetRequest... params) {
    try {
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      mapper.registerModule(new Jackson2HalModule());
      mapper.registerModule(new BoJacksonHalModule(new Class<?>[]{}));

      MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
      messageConverter.setObjectMapper(mapper);

      final OkHttpClient httpClient = getUnsafeOkHttpClient();
      httpClient.setFollowRedirects(true);
      httpClient.setFollowSslRedirects(true);
      httpClient.setHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });

      final List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//      interceptors.add(new ClientHttpRequestInterceptor() {
//        @Override
//        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//          request.getHeaders().add("Authorization", "Bearer 73db6457-1c9c-4ed2-be10-742ada31b9ea");
//          return execution.execute(request, body);
//        }
//      });

      final RestTemplate restTemplate = new RestTemplate();
      restTemplate.getMessageConverters().add(messageConverter);
      restTemplate.setRequestFactory(new OkHttpClientHttpRequestFactory(httpClient));
      restTemplate.setInterceptors(interceptors);
      restTemplate.setErrorHandler(new ResponseErrorHandler() {
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
          HttpStatus.Series series = response.getStatusCode().series();
          return (HttpStatus.Series.CLIENT_ERROR.equals(series)
            || HttpStatus.Series.SERVER_ERROR.equals(series));
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
          Log.i(HttpGetTask.class.toString(), "Response error: {" + response.getStatusCode() + "} {" + response.getStatusText() + "}");
          throw new RuntimeException("Response error: {" + response.getStatusCode() + "} {" + response.getStatusText() + "}");
        }
      });

      return doRequest(restTemplate, params[0].getUrl(), resultClass);
    } catch (Exception e) {
      Log.e("HttpGetTask", e.getMessage(), e);
      delegate.onHttpTaskError();
    }

    return null;
  }

  private Result doRequest(RestTemplate restTemplate, String url, Class<Result> resultType) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization","Bearer 73db6457-1c9c-4ed2-be10-742ada31b9ea");
    ResponseEntity<Result> response = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<Object>(headers),resultType);
    return response.getBody();
  }


  @Override
  protected void onPostExecute(Result result) {
    if (result == null) {
      delegate.onHttpTaskError();
    } else {
      delegate.onHttpTaskSuccess(result);
    }
  }

  @Override
  protected void onCancelled() {
    delegate.onHttpTaskCancelled();
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

  public interface HttpTaskDelegate<Result> {
    void onHttpTaskSuccess(Result result);

    void onHttpTaskError();

    void onHttpTaskCancelled();
  }
}
