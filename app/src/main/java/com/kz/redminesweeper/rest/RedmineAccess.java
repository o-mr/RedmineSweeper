package com.kz.redminesweeper.rest;

import android.util.Log;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.bean.CurrentUser;
import com.kz.redminesweeper.bean.Issue;
import com.kz.redminesweeper.bean.Issues;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Project;
import com.kz.redminesweeper.bean.Projects;
import com.kz.redminesweeper.bean.Queries;
import com.kz.redminesweeper.bean.Query;
import com.kz.redminesweeper.bean.Status;
import com.kz.redminesweeper.bean.Statuses;
import com.kz.redminesweeper.bean.Tracker;
import com.kz.redminesweeper.bean.Trackers;
import com.kz.redminesweeper.bean.Watcher;

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
public class RedmineAccess {

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
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setReadTimeout(6000);
        clientHttpRequestFactory.setConnectTimeout(3000);
        template.setRequestFactory(clientHttpRequestFactory);
    }

    @Background
    public void executeRest(final RestExecutor executor) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        redmine.setRestErrorHandler(new RestErrorHandler() {
            @Override
            public void onRestClientExceptionThrown(NestedRuntimeException e) {
                Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), e);
                Throwable cause = e.getCause();
                if (cause instanceof ConnectException) {
                    executor.onFailed(R.string.label_msg_error_network, e);
                } else if (e instanceof HttpClientErrorException) {
                    HttpClientErrorException exception = (HttpClientErrorException) e;
                    if (exception.getStatusCode() == HttpStatus.REQUEST_TIMEOUT) {
                        executor.onFailed(R.string.label_msg_error_rest_timeout, e);
                    } else if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                        executor.onFailed(R.string.label_msg_error_page_not_found, cause);
                    } else if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                        executor.onFailed(R.string.label_msg_error_auth_failed, cause);
                    } else {
                        executor.onFailed(R.string.label_msg_error_rest_failed, e);
                    }
                } else if (cause instanceof SocketTimeoutException) {
                    executor.onFailed(R.string.label_msg_error_rest_timeout, e);
                } else {
                    executor.onFailed(R.string.label_msg_error_rest_failed, e);
                }
                throw e;
            }
        });
        try {
            Object result = executor.execute(redmine);
            result.getClass(); //NPE
            executor.onSuccessful(result);
        } catch (NestedRuntimeException e) {
        } catch (Exception e) {
            executor.onFailed(R.string.label_msg_error_rest_failed, e);
        }
    }

    @Background
    public void downloadCurrentUser(final Account account, final RestResultListener<Account> restResultReceiver) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        executeRest(new RedmineAccess.RestExecutor<Account>() {
            @Override
            public Account execute(RedmineRestService redmine) {
                CurrentUser user = redmine.getCurrentUser();
                account.setUser(user.getUser());
                return account;
            }

            @Override
            public void onSuccessful(Account result) {
                restResultReceiver.onSuccessful(result);
            }

            @Override
            public void onFailed(int msgId, Throwable e) {
                restResultReceiver.onFailed(msgId, e);
            }
        });
    }


    @Background
    public void downloadStatuses(final RestResultListener<Statuses> restResultReceiver) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        executeRest(new RedmineAccess.RestExecutor<Statuses>() {
            @Override
            public Statuses execute(RedmineRestService redmine) {
                return redmine.getStatuses();
            }

            @Override
            public void onSuccessful(Statuses result) {
                restResultReceiver.onSuccessful(result);
            }

            @Override
            public void onFailed(int msgId, Throwable e) {
                restResultReceiver.onFailed(msgId, e);
            }
        });
    }

    @Background
    public void downloadQueries(final RestResultListener<Queries> restResultReceiver) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        executeRest(new RedmineAccess.RestExecutor<Queries>() {
            @Override
            public Queries execute(RedmineRestService redmine) {
                return redmine.getQueries();
            }

            @Override
            public void onSuccessful(Queries result) {
                result.sort();
                restResultReceiver.onSuccessful(result);
            }

            @Override
            public void onFailed(int msgId, Throwable e) {
                restResultReceiver.onFailed(msgId, e);
            }
        });
    }

    @Background
    public void downloadTrackers(final RestResultListener<Trackers> restResultReceiver) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        executeRest(new RedmineAccess.RestExecutor<Trackers>() {
            @Override
            public Trackers execute(RedmineRestService redmine) {
                return redmine.getTrackers();
            }

            @Override
            public void onSuccessful(Trackers result) {
                restResultReceiver.onSuccessful(result);
            }

            @Override
            public void onFailed(int msgId, Throwable e) {
                restResultReceiver.onFailed(msgId, e);
            }
        });
    }

    @Background
    public void downloadProjects(final RestResultListener<Projects> restResultReceiver) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        executeRest(new RedmineAccess.RestExecutor<Projects>() {
            @Override
            public Projects execute(RedmineRestService redmine) {
                return redmine.getProjects();
            }

            @Override
            public void onSuccessful(Projects result) {
                restResultReceiver.onSuccessful(result);
            }

            @Override
            public void onFailed(int msgId, Throwable e) {
                restResultReceiver.onFailed(msgId, e);
            }
        });
    }

    @Background
    public void downloadIssues(final Project project, final int offset, final int limit, final IssuesFilter filter, final RestResultListener<Issues> restResultReceiver) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        executeRest(new RedmineAccess.RestExecutor<Issues>() {
            @Override
            public Issues execute(RedmineRestService redmine) {
                if (filter instanceof Status) {
                    return redmine.getMyIssuesByProjectIdAndStatusId(project.getId(), filter.getId(), offset, limit);
                } else if (filter instanceof Tracker) {
                    return redmine.getMyIssuesByProjectIdAndTrackerId(project.getId(), filter.getId(), offset, limit);
                } else if (filter instanceof Watcher) {
                    return redmine.getIssuesByProjectIdAndWatcherId(project.getId(), filter.getId(), offset, limit);
                } else if (filter instanceof Query) {
                    Query query = (Query)filter;
                    if (query.getProject_id() != 0 && query.getProject_id() != project.getId()) {
                        throw new InvalidQueryException();
                    }
                    return redmine.getIssuesByProjectIdAndQueryId(project.getId(), filter.getId(), offset, limit);
                } else {
                    return null;
                }
            }

            @Override
            public void onSuccessful(Issues result) {
                restResultReceiver.onSuccessful(result);
            }

            @Override
            public void onFailed(int msgId, Throwable e) {
                restResultReceiver.onFailed(msgId, e);
            }
        });
    }


    public interface RestExecutor<R> {
        R execute(RedmineRestService redmine);
        void onSuccessful(R result);
        void onFailed(int msgId, Throwable e);
    }

    public interface RestResultListener<R> {
        void onSuccessful(R result);
        void onFailed(int msgId, Throwable e);
    }

}
