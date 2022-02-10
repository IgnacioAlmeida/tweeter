package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUserNotificationHandler extends GetUserHandler<UserObserver> {

    public GetUserNotificationHandler(UserObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, UserObserver observer, User user) {
        observer.handleSuccess(user);
    }

}
