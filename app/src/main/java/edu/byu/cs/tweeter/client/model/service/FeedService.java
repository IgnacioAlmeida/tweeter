package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.presenter.FeedPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedService {

    public interface GetFeedObserver {
        void handleSuccess(List<Status> statuses, boolean hasMorePages);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, GetFeedObserver getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetFeedHandler(getFeedObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFeedTask);
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends Handler {
        GetFeedObserver observer;

        public GetFeedHandler(GetFeedObserver observer) {
            this.observer = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
                observer.handleSuccess(statuses, hasMorePages);
            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }
}