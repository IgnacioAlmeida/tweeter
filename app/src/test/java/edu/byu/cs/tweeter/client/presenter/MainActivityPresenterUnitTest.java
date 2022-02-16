package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MainActivityPresenterUnitTest {

  private MainActivityPresenter.View mockView;
  private FeedService mockFeedService;

  private MainActivityPresenter mainActivityPresenterSpy;

  @Before
  public void setup() {
    mockView = Mockito.mock(MainActivityPresenter.View.class);
    mockFeedService = Mockito.mock(FeedService.class);

    mainActivityPresenterSpy = Mockito.spy(new MainActivityPresenter(mockView));
    Mockito.when(mainActivityPresenterSpy.getFeedService()).thenReturn(mockFeedService);
  }

  public void setupAndCallPostingStatusAndVerifyCommonResult(Answer<Void> answer){
    Mockito.doAnswer(answer).when(mockFeedService).postStatus(Mockito.any(),Mockito.any(),Mockito.any());
    mainActivityPresenterSpy.postStatus(Mockito.any());
    Mockito.verify(mockView).displayInfoMessage("Posting Status...");
  }

  @Test
  public void testPostStatus_postSuccessful() {
    HandleObserverAnswer handleObserverAnswer = new HandleObserverAnswer() {
      @Override
      void observerHandler() {
        observer.handleSuccess(true);
      }
    };

    setupAndCallPostingStatusAndVerifyCommonResult(handleObserverAnswer.answer);
    Mockito.verify(mockView).handlePostSuccess();
  }

  @Test
  public void testPostStatus_postFailedWithMessage() {
    HandleObserverAnswer handleObserverAnswer = new HandleObserverAnswer() {
      @Override
      void observerHandler() {
        observer.handleFailure("the error message");
      }
    };

    setupAndCallPostingStatusAndVerifyCommonResult(handleObserverAnswer.answer);
    Mockito.verify(mockView).displayErrorMessage("Failed to post status: the error message");
  }

  @Test
  public void testPostStatus_postFailedWithException(){
    HandleObserverAnswer handleObserverAnswer = new HandleObserverAnswer() {
      @Override
      void observerHandler() {
        observer.handleException(new Exception("the exception message"));
      }
    };
    setupAndCallPostingStatusAndVerifyCommonResult(handleObserverAnswer.answer);
    Mockito.verify(mockView).displayErrorMessage("Failed to post status because of exception: the exception message");
  }

  public abstract class HandleObserverAnswer {
    Answer<Void> answer;
    MainActivityPresenter.GetPostStatusObserver observer;
    abstract void observerHandler();
    public HandleObserverAnswer() {
      answer = new Answer<Void>() {
        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
          observer = invocation.getArgument(1, MainActivityPresenter.GetPostStatusObserver.class);
          observerHandler();
          return null;
        }
      };
    }
  }
}
