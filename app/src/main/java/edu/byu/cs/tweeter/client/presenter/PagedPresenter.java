package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class PagedPresenter<T> implements ServiceObserver {
    public PagedPresenter(View view) {
        this.view = view;
    }

    public interface View<T> {
        void displayErrorMessage(String message);
        void handleSuccess(User user);
        void setLoadingStatus(boolean value);
        void handleFeedSuccess(List<T> list);
    }
    protected View view;

    @Override
    public void handleFailure(String failurePrefix, String message, boolean isLoading) {
        handler(isLoading, failurePrefix + message);
    }

    @Override
    public void handleException(String exceptionPrefix, Exception ex, boolean isLoading) {
        handler(isLoading,exceptionPrefix + ex.getMessage() );
    }

    private void handler(boolean status, String message) {
        view.setLoadingStatus(status);
        view.displayErrorMessage(message);
    }
}
