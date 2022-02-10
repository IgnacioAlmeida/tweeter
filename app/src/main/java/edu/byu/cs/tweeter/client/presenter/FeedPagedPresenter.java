package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public class FeedPagedPresenter extends PagedPresenter<User> {
    private PagedPresenter pagedPresenter;
    private UserService userService;
    private FeedService feedService;
    private static final int PAGE_SIZE = 10;

    private Status lastStatus;

    private boolean hasMorePages;
    private boolean isLoading = false;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }


    public FeedPagedPresenter(View view) {
        super(view);
        this.pagedPresenter = new PagedPresenter(view);
        userService = new UserService();
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
            isLoading = false;
            view.setLoadingStatus(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.handleFeedSuccess(statuses);
        }

        @Override
        public void handleFailure(String message) {
            String failurePrefix = "Failed to get feed: ";
            pagedPresenter.handleFailure(failurePrefix, message, isLoading);
        }

        @Override
        public void handleException(Exception exception) {
            String exceptionPrefix = "Failed to get feed because of exception: ";
            pagedPresenter.handleException(exceptionPrefix,exception,isLoading);
        }
    }

    public void loadUser(String userAlias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new FeedPagedPresenter.GetUserObserver());
    }


    public class GetUserObserver implements UserObserver {
        @Override
        public void handleSuccess(User user) {
            view.handleSuccess(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get user's profile: " + message);

        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
}
