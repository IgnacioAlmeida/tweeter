package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginService {



    public interface GetLoginObserver {
        void handleSuccess(User user, AuthToken authToken);
        void handleFailure(String message);
        void handleException(Exception ex);
    }

    public void getLogin(String id, String password, GetLoginObserver getLoginObserver) {
        LoginTask loginTask = new LoginTask(id, password, new LoginHandler(getLoginObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }


    public interface GetLogoutObserver extends SimpleNotificationObserver {

    }

    public void logout(AuthToken currUserAuthToken, GetLogoutObserver getLogoutObserver) {
        LogoutTask logoutTask = new LogoutTask(currUserAuthToken, new SimpleNotificationHandler(getLogoutObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }


}
