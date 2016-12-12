package giwi.org.giwitter.helpers;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
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
    @Post("/user/login")
    String login(@Body String string);

    @Get(("/private/message"))
    String getMesages();

    @Get(("/private/user/list"))
    String getUsers();

    @Put("/user/register")
    String register(@Body String string);
    @Post("/private/message")
    String sendMessage(@Body String s);

    void setHeader(String name, String value);

    String getHeader(String name);


}