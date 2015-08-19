package com.kz.redminesweeper.rest;

import com.kz.redminesweeper.account.Account;

import org.androidannotations.annotations.EBean;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@EBean(scope = EBean.Scope.Singleton)
public class RedmineAuthInterceptor implements ClientHttpRequestInterceptor {

    private Account account;

    public void setAccount(Account account) {
        this.account = account;
    }

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.setAuthorization(new HttpBasicAuthentication(account.getLoginId(), account.getPassword()));
        return execution.execute(request, body);
    }


}
