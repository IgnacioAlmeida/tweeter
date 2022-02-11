package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class PagedPresenterStatus extends Presenter<Status> {

    private FeedService feedService;

    public PagedPresenterStatus(View view){
        super(view);
        feedService = new FeedService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            feedService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetFeedObserver());

        }
    }

    public class GetFeedObserver implements PagedObserver<Status> {

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            presenterHandleSuccess(statuses, hasMorePages);
        }

        @Override
        public void handleFailure(String message) {
            String failurePrefix =  "Failed to get feed: ";
            view.displayErrorMessage(failurePrefix + message);
        }

        @Override
        public void handleException(Exception exception) {
            String exceptionPrefix = "Failed to get feed because of exception: ";
            view.displayErrorMessage(exceptionPrefix + exception.getMessage());
        }
    }

}
