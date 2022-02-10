package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.ExecuteExecutor;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticatedNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class LoginService extends ExecuteExecutor {

    public void getLogin(String id, String password, AuthenticatedObserver getLoginObserver) {
        LoginTask loginTask = new LoginTask(id, password, new AuthenticatedNotificationHandler(getLoginObserver));
        execute(loginTask);
    }

    public void logout(AuthToken currUserAuthToken, SimpleNotificationObserver getLogoutObserver) {
        LogoutTask logoutTask = new LogoutTask(currUserAuthToken, new SimpleNotificationHandler(getLogoutObserver));
        execute(logoutTask);
    }
}
