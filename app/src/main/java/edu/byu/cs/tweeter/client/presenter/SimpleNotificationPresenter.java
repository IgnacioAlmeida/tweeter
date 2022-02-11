package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;

public class SimpleNotificationPresenter implements SimpleNotificationObserver {
    String error;

    public SimpleNotificationPresenter(String error) {
        this.error = error;
    }

    @Override
    public void handleFailure(String message) {

    }

    @Override
    public void handleException(Exception ex) {

    }

    @Override
    public void handleSuccess(Boolean status) {

    }
}
