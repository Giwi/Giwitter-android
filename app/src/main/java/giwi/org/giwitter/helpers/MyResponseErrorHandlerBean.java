package giwi.org.giwitter.helpers;

import android.content.Context;
import android.widget.Toast;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

@EBean
class MyResponseErrorHandlerBean implements ResponseErrorHandler {
    @RootContext
    Context context;

    /**
     * Indicates whether the given response has any errors.
     * Implementations will typically inspect the {@link ClientHttpResponse#getStatusCode() HttpStatus}
     * of the response.
     *
     * @param response the response to inspect
     * @return {@code true} if the response has an error; {@code false} otherwise
     * @throws IOException in case of I/O errors
     */
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getRawStatusCode() >= 400;
    }

    /**
     * Handles the error in the given response.
     * This method is only called when {@link #hasError(ClientHttpResponse)} has returned {@code true}.
     *
     * @param response the response with the error
     * @throws IOException in case of I/O errors
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new RestClientException(response.getStatusText());
    }
}
