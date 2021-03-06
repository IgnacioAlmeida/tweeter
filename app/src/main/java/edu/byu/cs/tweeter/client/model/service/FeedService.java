package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.ExecuteExecutor;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedService extends ExecuteExecutor {

    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, PagedObserver<Status> getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new PagedNotificationHandler(getFeedObserver));
        execute(getFeedTask);
    }

    public void postStatus(AuthToken currUserAuthToken, Status newStatus, SimpleNotificationObserver getPostStatusObserver) {
        PostStatusTask statusTask = new PostStatusTask(currUserAuthToken,
                newStatus, new SimpleNotificationHandler(getPostStatusObserver));
        execute(statusTask);
    }

}
