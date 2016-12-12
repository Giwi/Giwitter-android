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
    /**
     * Login string.
     *
     * @param string the string
     * @return the string
     */
    @Post("/user/login")
    String login(@Body String string);

    /**
     * Gets mesages.
     *
     * @return the mesages
     */
    @Get(("/private/message"))
    String getMesages();

    /**
     * Gets users.
     *
     * @return the users
     */
    @Get(("/private/user/list"))
    String getUsers();

    /**
     * Register string.
     *
     * @param string the string
     * @return the string
     */
    @Put("/user/register")
    String register(@Body String string);

    /**
     * Send message string.
     *
     * @param s the s
     * @return the string
     */
    @Post("/private/message")
    String sendMessage(@Body String s);

    /**
     * Sets header.
     *
     * @param name  the name
     * @param value the value
     */
    void setHeader(String name, String value);

    /**
     * Gets header.
     *
     * @param name the name
     * @return the header
     */
    String getHeader(String name);


}