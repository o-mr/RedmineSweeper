package com.kz.redminesweeper.rest;

import com.kz.redminesweeper.bean.CurrentUser;
import com.kz.redminesweeper.bean.Issue;
import com.kz.redminesweeper.bean.Issues;
import com.kz.redminesweeper.bean.Project;
import com.kz.redminesweeper.bean.Projects;
import com.kz.redminesweeper.bean.Statuses;
import com.kz.redminesweeper.bean.Trackers;
import com.kz.redminesweeper.bean.User;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.web.client.RestTemplate;


@Rest(converters={RedmineJsonConverter.class}, interceptors = {RedmineAuthInterceptor.class})
public interface RedmineRestService extends RestClientErrorHandling {

    RestTemplate getRestTemplate();

    void setRootUrl(String rootUrl);

    String getRootUrl();

    @Get("/users/current.json")
    CurrentUser getMyUserInfo();

    @Get("/issue_statuses.json")
    Statuses getStatuses();

    @Get("/trackers.json")
    Trackers getTrackers();

    @Get("/projects.json")
    Projects getProjects();

    @Get("/projects/{projectId}.json")
    Project getProjectById(int projectId);

    @Get("/issues.json?offset={offset}&limit={limit}")
    Issues getIssues(int offset, int limit);

    @Get("/projects/{projectId}/issues.json?offset={offset}&limit={limit}")
    Issues getIssuesByProjectId(int projectId, int offset, int limit);

    @Get("/issues.json?assigned_to_id=me&offset={offset}&limit={limit}")
    Issues getMyIssues(int offset, int limit);

    @Get("/projects/{projectId}/issues.json?assigned_to_id=me&offset={offset}&limit={limit}")
    Issues getMyIssuesByProjectId(int projectId, int offset, int limit);

    @Get("/projects/{projectId}/issues.json?assigned_to_id=me&status_id={status_id}&offset={offset}&limit={limit}")
    Issues getMyIssuesByProjectIdAndStatusId(int projectId, String status_id, int offset, int limit);

    @Get("/projects/{projectId}/issues.json?assigned_to_id=me&status_id=*&tracker_id={tracker_id}&offset={offset}&limit={limit}")
    Issues getMyIssuesByProjectIdAndTrackerId(int projectId, String tracker_id, int offset, int limit);

    @Get("/projects/{projectId}/issues.json?watcher_id={watcher_id}&offset={offset}&limit={limit}")
    Issues getIssuesByProjectIdAndWatcherId(int projectId, String watcher_id, int offset, int limit);

    @Get("/issues/{issueId}.json")
    Issue getIssueById(int issueId);

}
