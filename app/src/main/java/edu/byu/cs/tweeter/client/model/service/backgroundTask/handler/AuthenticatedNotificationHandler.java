package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticatedNotificationHandler extends AuthenticatedTaskHandler<AuthenticatedObserver> {

    public AuthenticatedNotificationHandler(AuthenticatedObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, AuthenticatedObserver observer, User registeredUser, AuthToken authToken) {
        observer.handleSuccess(registeredUser, authToken);
    }
}
