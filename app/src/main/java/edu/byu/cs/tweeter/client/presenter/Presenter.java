package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class Presenter<T> {

    protected static final int PAGE_SIZE = 10;
    protected View view;
    private UserService userService;

    protected User user;
    protected boolean hasMorePages = true;
    protected Status lastStatus = null;
    protected User lastFollower;
    protected boolean isLoading = false;

    public Presenter(View view) {
        this.view = view;
        this.userService = new UserService();
    }

    public interface View<T> {
        void displayErrorMessage(String message);
        void setLoadingStatus(boolean value);
        void successfulAdd(List<T> list);
        void handleSuccess(User user);
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    protected void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    protected User getLastFollower() {
        return lastFollower;
    }

    protected void setLastFollower(User lastFollower) {
        this.lastFollower = lastFollower;
    }

    public void handleFailure(String failurePrefix, String message, boolean isLoading) {
        handler(isLoading, failurePrefix + message);
    }

    public void handleException(String exceptionPrefix, Exception ex, boolean isLoading) {
        handler(isLoading,exceptionPrefix + ex.getMessage() );
    }

    private void handler(boolean status, String message) {
        view.setLoadingStatus(status);
        view.displayErrorMessage(message);
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
    public void loadUser(String userAlias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetUserObserver());
    }

}
