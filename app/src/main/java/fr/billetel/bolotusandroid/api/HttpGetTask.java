package fr.billetel.bolotusandroid.api;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.squareup.okhttp.OkHttpClient;

import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import fr.billetel.bolotusandroid.R;
import fr.billetel.bolotusandroid.modules.administration.user.User;
import fr.billetel.bolotusandroid.api.mapping.BoJacksonHalModule;
import fr.billetel.bolotusandroid.api.request.ApiGetRequest;

/**
 * Created by Maël Gargadennnec on 17/06/2016.
 */
public class HttpGetTask<Progress, Result> extends AsyncTask<ApiGetRequest, Progress, Result> {
  private RestTemplate restTemplate;
  private Activity activity;
  private HttpGetTaskDelegate<Result> delegate;
  private Class<Result> resultClass;

  public HttpGetTask(RestTemplate restTemplate, Activity activity, HttpGetTaskDelegate delegate, Class<Result> resultClass) {
    this.restTemplate = restTemplate;
    this.activity = activity;
    this.delegate = delegate;
    this.resultClass = resultClass;
  }

  @Override
  protected Result doInBackground(ApiGetRequest... params) {
    return doExecuteRequest(params);
  }

  private Result doExecuteRequest(ApiGetRequest... params) {
    try {
      if (params[0].getUrl() != null) {
        return doRequest(restTemplate, params[0].getUrl(), resultClass);
      }
    } catch (Exception e) {
      Log.e("HttpGetTask", e.getMessage(), e);
      delegate.onHttpTaskError();
    }

    return null;
  }

  private Result doRequest(RestTemplate restTemplate, String url, Class<Result> resultType) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer 73db6457-1c9c-4ed2-be10-742ada31b9ea");
    ResponseEntity<Result> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers), resultType);
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

  public interface HttpGetTaskDelegate<Result> {
    void onHttpTaskSuccess(Result result);

    void onHttpTaskError();

    void onHttpTaskCancelled();
  }
}
