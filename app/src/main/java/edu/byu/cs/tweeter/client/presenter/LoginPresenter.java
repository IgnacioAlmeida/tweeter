package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticatedObserver;
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

    public void validateLogin(EditText alias, EditText password) {
        if (alias.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }


    public void handleLogin(String id, String password) {
        loginService.getLogin(id, password, new GetLoginObserver());
    }

    public class GetLoginObserver implements AuthenticatedObserver {

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
