package com.codedrinker.github;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codedrinker.exception.CommentHubException;
import com.codedrinker.github.entity.GitHubIssue;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


/**
 * Created by codedrinker on 06/08/2017.
 */
@Component
public class GitHubIssueApi extends AbstractGitHubApi {
    private final Logger logger = LoggerFactory.getLogger(GitHubIssueApi.class);

    public GitHubIssue create(String accessToken, GitHubIssue issue) throws CommentHubException {
        try {
            String url = BASE_URL
                    + "/repos/"
                    + issue.getOwner()
                    + "/"
                    + issue.getRepo()
                    + "/issues?access_token=" + accessToken;
            OkHttpClient client = new OkHttpClient();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", issue.getTitle());
            jsonObject.put("body", issue.getBody());
            logger.info(jsonObject.toString());
            Request request = new Request.Builder()
                    .addHeader("Accept", "application/vnd.github.squirrel-girl-preview, application/vnd.github.html+json")
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), jsonObject.toJSONString()))
                    .build();

            Response execute = client.newCall(request).execute();
            if (execute.isSuccessful()) {
                logger.info(execute.body().toString());
                return JSONObject.parseObject(execute.body().string(), GitHubIssue.class);
            } else {
                logger.error("create issue error, url -> {}, message -> {}, result -> {}",
                        url, jsonObject.toString(), JSON.toJSONString(execute.body()));
                if (execute.code() == 404) {
                    throw new CommentHubException("Authorization Failed.");
                }
                throw new CommentHubException(execute.message());
            }
        } catch (IOException e) {
            logger.error("create issue error -> {}", accessToken, e);
            throw new CommentHubException(e.getMessage());
        }
    }

    public void addLabels2Issue(String accessToken, GitHubIssue issue, List<String> labels) throws CommentHubException {
        try {
            String url = issue.getUrl() + "?access_token=" + accessToken;
            OkHttpClient client = new OkHttpClient();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("labels", labels);
            Request request = new Request.Builder()
                    .addHeader("Accept", "application/vnd.github.squirrel-girl-preview, application/vnd.github.html+json")
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), jsonObject.toJSONString()))
                    .build();

            Response execute = client.newCall(request).execute();
            if (!execute.isSuccessful()) {
                logger.error("add issue labels error, url -> {}, message -> {}, result -> {}",
                        url, jsonObject.toString(), JSON.toJSONString(execute.body()));
                if (execute.code() == 404) {
                    throw new CommentHubException("Authorization Failed.");
                }
                throw new CommentHubException(execute.message());
            }
        } catch (IOException e) {
            logger.error("create issue error -> {}", accessToken, e);
            throw new CommentHubException(e.getMessage());
        }
    }
}
