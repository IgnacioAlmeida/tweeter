package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetFeedTask.
 */
public class GetFeedHandler extends PagedNotificationHandler<PagedObserver> {

    public GetFeedHandler(FeedService.GetFeedObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PagedObserver observer, List<PagedObserver> list, boolean hasMorePages) {
        observer.handleSuccess(list, hasMorePages);

    }
}
