package com.kz.redminesweeper.rest;

import android.util.Log;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.account.Account;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import java.util.ArrayList;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class RedmineRestHelper {

    @RestService
    RedmineRestService redmine;

    @Bean
    RedmineAuthInterceptor authInterceptor;

    public void setUpRedmineRestService(Account account) {
        redmine.setRootUrl(account.getRootUrl());
        authInterceptor.setAccount(account);
        RestTemplate template = redmine.getRestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(authInterceptor);
        template.setInterceptors(interceptors);
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setReadTimeout(5000);
        simpleClientHttpRequestFactory.setConnectTimeout(3000);
        template.setRequestFactory(simpleClientHttpRequestFactory);
    }

    @Background
    public void executeRest(final RestExecutor executor) {
        redmine.setRestErrorHandler(new RestErrorHandler() {
            @Override
            public void onRestClientExceptionThrown(NestedRuntimeException e) {
                Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), e);
                Throwable ecase = e.getCause();
                if (ecase instanceof ConnectException) {
                    executor.onFailed(RestError.NETWORK, R.string.label_msg_error_ntework, e);
                } else if (e instanceof HttpClientErrorException) {
                    HttpClientErrorException exception = (HttpClientErrorException) e;
                    if (exception.getStatusCode() == HttpStatus.REQUEST_TIMEOUT) {
                        executor.onFailed(RestError.TIMEOUT, R.string.label_msg_error_rest_timeout, e);
                    } else if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                        executor.onFailed(RestError.NOT_FOUND, R.string.label_msg_error_page_not_found, ecase);
                    } else if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                        executor.onFailed(RestError.UNAUTHORIZED, R.string.label_msg_error_auth_failed, ecase);
                    } else {
                        executor.onFailed(RestError.UNKNOWN, R.string.label_msg_error_rest_failed, e);
                    }
                } else if (ecase instanceof SocketTimeoutException) {
                    executor.onFailed(RestError.TIMEOUT, R.string.label_msg_error_rest_timeout, e);
                } else {
                    executor.onFailed(RestError.UNKNOWN, R.string.label_msg_error_rest_failed, e);
                }
                throw e;
            }
        });
        try {
            Object result = executor.execute(redmine);
            if (result != null) {
                executor.onSuccessful(result);
            } else {
                executor.onFailed(RestError.UNKNOWN, R.string.label_msg_error_rest_failed, null);
            }
        } catch (Exception e) {}
    }

    public interface RestExecutor<R> {

        R execute(RedmineRestService redmine);

        void onSuccessful(R result);

        void onFailed(RestError error, int msgId, Throwable e);

    }

    public enum RestError {
        NETWORK, TIMEOUT, NOT_FOUND, UNAUTHORIZED, UNKNOWN
    }
}
