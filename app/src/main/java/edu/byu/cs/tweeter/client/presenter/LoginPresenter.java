package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {

    public interface View {
        void displayErrorMessage(String message);
        void handleSuccess(User user, AuthToken authToken);
    }
    private View view;
    private LoginService loginService;
    public LoginPresenter(View view) {
        this.view = view;
        loginService = new LoginService();
    }

    public void handleLogin(String id, String password) {
        loginService.getLogin(id, password, new GetLoginObserver());
    }

    public class GetLoginObserver implements LoginService.GetLoginObserver {

        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            view.handleSuccess(user,authToken);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to login because of exception: " + ex.getMessage());
        }
    }

}
