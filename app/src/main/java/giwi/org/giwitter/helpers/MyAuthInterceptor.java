package giwi.org.giwitter.helpers;

import org.androidannotations.annotations.EBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * The type My auth interceptor.
 */
@EBean(scope = EBean.Scope.Singleton)
public class MyAuthInterceptor implements ClientHttpRequestInterceptor {

    /**
     * Intercept client http response.
     *
     * @param request   the request
     * @param body      the body
     * @param execution the execution
     * @return the client http response
     * @throws IOException the io exception
     */
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        if(Session.token != null && !"".equals(Session.token)) {
            headers.add(Constants.INTENT_TOKEN, Session.token);
        }
        return execution.execute(request, body);
    }
}