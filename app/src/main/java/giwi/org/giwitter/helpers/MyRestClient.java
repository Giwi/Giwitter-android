package giwi.org.giwitter.helpers;

import android.content.Context;
import android.widget.Toast;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Header;
import org.androidannotations.rest.spring.annotations.Headers;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Put;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.StringHttpMessageConverter;

/**
 * The interface My rest client.
 */
@Rest(rootUrl = "http://vps288382.ovh.net/api/1",
        converters = {StringHttpMessageConverter.class},
        responseErrorHandler = MyResponseErrorHandlerBean.class,
        interceptors = MyAuthInterceptor.class)
public interface MyRestClient {
    /**
     * Login string.
     *
     * @param string the string
     * @return the string
     */
    @Post("/user/login")
    @Headers({
            @Header(name = "Content-type", value = "application/json")
    })
    String login(@Body String string);


    @Get(("/private/message"))
    @Headers({
            @Header(name = "Content-type", value = "application/json")
    })
    String getMesages();


    /**
     * Register string.
     *
     * @param string the string
     * @return the string
     */
    @Put("/user/register")
    @Headers({
            @Header(name = "Content-type", value = "application/json")
    })
    String register(@Body String string);

    void setHeader(String name, String value);

    String getHeader(String name);


}