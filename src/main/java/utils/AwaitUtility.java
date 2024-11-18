package utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.hamcrest.Matcher;

import java.time.Duration;
import java.util.concurrent.Callable;


@Slf4j
@UtilityClass
public class AwaitUtility {
    private static final String POLL_MESSAGE = "Waiting for condition to be ready";

    private static <T> T awaitBase(Duration duration, Duration pollInterval,
                                   final Callable<T> supplier, final Matcher<? super T> matcher, String alias) {
        return Awaitility.await(alias)
                .pollInSameThread()
                .atMost(duration)
                .pollInterval(pollInterval)
                .until(supplier, matcher);
    }


    public static <T> boolean awaitSafe(Duration duration, Duration pollInterval, final Callable<T> supplier, final Matcher<? super T> matcher) {
        try {
            awaitBase(duration, pollInterval, supplier, matcher, POLL_MESSAGE);
            return true;
        } catch (Exception | AssertionError ex) {
            log.debug("Condition failed!");
            return false;
        }
    }
}
