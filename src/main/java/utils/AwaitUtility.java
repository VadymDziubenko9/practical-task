package utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.Matcher;

import java.time.Duration;
import java.util.concurrent.Callable;

@Slf4j
@UtilityClass
public class AwaitUtility {

    private static final String DEFAULT_WAIT_MESSAGE = "Awaiting condition to meet the specified criteria...";
    private static final Duration DEFAULT_POLL_INTERVAL = Duration.ofMillis(500);

    /**
     * Waits for a condition to be met with detailed logging.
     *
     * @param <T>          the type of the result provided by the supplier
     * @param duration     the maximum duration to wait
     * @param pollInterval the interval between polls
     * @param supplier     the condition supplier
     * @param matcher      the matcher to validate the condition
     * @param description  a description for the wait operation
     * @return the result if the condition is met within the duration
     */
    private static <T> T awaitWithLogs(Duration duration, Duration pollInterval,
                                       Callable<T> supplier, Matcher<? super T> matcher, String description) {
        log.info("Starting await: {}", description);
        try {
            return Awaitility.await(description)
                    .pollInSameThread()
                    .atMost(duration)
                    .pollInterval(pollInterval)
                    .until(supplier, matcher);
        } catch (ConditionTimeoutException ex) {
            log.error("Condition timed out after {}: {}", duration, description, ex);
            throw ex;
        }
    }

    /**
     * Waits safely for a condition to be met, with logging on failure.
     *
     * @param <T>          the type of the result provided by the supplier
     * @param duration     the maximum duration to wait
     * @param pollInterval the interval between polls
     * @param supplier     the condition supplier
     * @param matcher      the matcher to validate the condition
     * @return true if the condition is met, false otherwise
     */
    public static <T> boolean waitForCondition(Duration duration, Duration pollInterval,
                                               Callable<T> supplier, Matcher<? super T> matcher) {
        try {
            awaitWithLogs(duration, pollInterval, supplier, matcher, DEFAULT_WAIT_MESSAGE);
            return true;
        } catch (ConditionTimeoutException | AssertionError ex) {
            log.warn("Condition not met within the specified duration: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * A simpler version of waitForCondition with default poll interval.
     *
     * @param <T>      the type of the result provided by the supplier
     * @param duration the maximum duration to wait
     * @param supplier the condition supplier
     * @param matcher  the matcher to validate the condition
     * @return true if the condition is met, false otherwise
     */
    public static <T> boolean waitForCondition(Duration duration, Callable<T> supplier, Matcher<? super T> matcher) {
        return waitForCondition(duration, DEFAULT_POLL_INTERVAL, supplier, matcher);
    }
}
