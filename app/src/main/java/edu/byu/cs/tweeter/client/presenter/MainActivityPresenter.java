package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;
import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivityPresenter {




    public interface View {
        void displayErrorMessage(String message);
        void handleSuccess(boolean follow);
        void handleSuccessFollowees(int count);
        void handleSuccessFollowers(int count);

    }
    private View view;
    private FollowService followService;

    public MainActivityPresenter(View view) {
        this.view = view;
        followService = new FollowService();
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public List<String> parseURLs(String post) throws MalformedURLException {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public void unfollow(User selectedUser) {
        followService.unfollow(Cache.getInstance().getCurrUserAuthToken(),selectedUser, new GetUnfollowObserver());
    }

    public class GetUnfollowObserver implements FollowService.GetUnfollowObserver {

        @Override
        public void handleSuccess(boolean follow) {
            view.handleSuccess(false);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to unfollow: " + message);

        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to unfollow because of exception: " + exception.getMessage());

        }
    }

    public void follow(User selectedUser) {
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowObserver());
    }

    public class GetFollowObserver implements FollowService.GetFollowObserver {

        @Override
        public void handleSuccess(boolean follow) {
            view.handleSuccess(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to unfollow: " + message);

        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to unfollow because of exception: " + exception.getMessage());

        }
    }


    public void getFollowingCounter(User selectedUser, Executor executor) {
        followService.getFollowingCounter(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowingCounterObserver(), executor);
    }

    public class GetFollowingCounterObserver implements FollowService.GetFollowingCounterObserver {

        @Override
        public void handleSuccess(int count) {
            view.handleSuccessFollowees(count);

        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get following count: " + message);

        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get following count because of exception: " + exception.getMessage());

        }
    }


    public void getFollowersCounter(User selectedUser, Executor executor) {
        followService.getFollowersCounter(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowersCounterObserver(), executor);
    }

    public class GetFollowersCounterObserver implements FollowService.GetFollowersCounterObserver {

        @Override
        public void handleSuccess(int count) {
            view.handleSuccessFollowers(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get followers count: " + message);

        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get followers count because of exception: "+ exception.getMessage());

        }
    }
}
