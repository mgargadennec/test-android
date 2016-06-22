package fr.billetel.bolotusandroid.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import fr.billetel.bolotusandroid.api.request.ApiGetRequest;

/**
 * Created by Maël Gargadennnec on 17/06/2016.
 */
public class HttpAuthenticateTask<Progress, Result> extends AsyncTask<ApiGetRequest, Progress, Result> {
  private RestTemplate restTemplate;
  private Activity activity;
  private HttpAuthenticateTaskDelegate<Result> delegate;
  private Class<Result> resultClass;

  public HttpAuthenticateTask(RestTemplate restTemplate, Activity activity, HttpAuthenticateTaskDelegate delegate, Class<Result> resultClass) {
    this.restTemplate=restTemplate;
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
      if(params[0].getUrl()!=null) {
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

  public interface HttpAuthenticateTaskDelegate<Result> {
    void onHttpTaskSuccess(Result result);

    void onHttpTaskError();

    void onHttpTaskCancelled();
  }

  public
}
