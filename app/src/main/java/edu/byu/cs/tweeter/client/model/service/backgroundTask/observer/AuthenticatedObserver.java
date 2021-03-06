package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticatedObserver extends ServiceObserver{
    void handleSuccess(User user, AuthToken authToken);
}
