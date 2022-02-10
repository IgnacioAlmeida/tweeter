package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public class FollowersPagedPresenter extends PagedPresenter<User> {
    private static final int PAGE_SIZE = 10;

    private FollowService followService;
    private UserService userService;
    private PagedPresenter pagedPresenter;
    private User lastFollower;
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

    public FollowersPagedPresenter(View view) {
        super(view);
        pagedPresenter = new PagedPresenter(view);
        followService = new FollowService();
        userService = new UserService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new GetFollowersObserver());
        }
    }

    public class GetFollowersObserver implements PagedObserver<User> {

        @Override
        public void handleSuccess(List list, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastFollower = (list.size() > 0) ? (User) list.get(list.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.handleFeedSuccess(list);
        }

        @Override
        public void handleFailure(String message) {
            String failurePrefix =  "Failed to get followers: ";
            pagedPresenter.handleFailure(failurePrefix,message,isLoading);
        }

        @Override
        public void handleException(Exception exception) {
            String exceptionPrefix = "Failed to get followers because of exception: ";
            pagedPresenter.handleException(exceptionPrefix,exception,isLoading);
        }
    }

    public void loadUser(String userAlias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new FollowersPagedPresenter.GetUserObserver());

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

