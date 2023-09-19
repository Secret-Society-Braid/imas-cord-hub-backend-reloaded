/*
 * Copyright 2008-present MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/*
 * Copyright 2015 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.braid.society.secret.imascordhubbackend.internal.database;

import com.mongodb.MongoInterruptedException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.lang.Nullable;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class SubscriberHelpers {

  /**
   * A Subscriber that stores the publishers results and provides a latch so can block on completion.
   *
   * @param <T> The publishers result type
   */
  public abstract static class ObservableSubscriber<T> implements Subscriber<T> {
    private final List<T> received;
    private final List<RuntimeException> errors;
    private final CountDownLatch latch;
    private volatile Subscription subscription;
    private volatile boolean completed;

    /**
     * Construct an instance
     */
    public ObservableSubscriber() {
      this.received = new ArrayList<>();
      this.errors = new ArrayList<>();
      this.latch = new CountDownLatch(1);
    }

    @Override
    public void onSubscribe(final Subscription s) {
      subscription = s;
    }

    @Override
    public void onNext(final T t) {
      received.add(t);
    }

    @Override
    public void onError(final Throwable t) {
      if (t instanceof RuntimeException) {
        errors.add((RuntimeException) t);
      } else {
        errors.add(new RuntimeException("Unexpected exception", t));
      }
      onComplete();
    }

    @Override
    public void onComplete() {
      completed = true;
      latch.countDown();
    }

    /**
     * Gets the subscription
     *
     * @return the subscription
     */
    public Subscription getSubscription() {
      return subscription;
    }

    /**
     * Get received elements
     *
     * @return the list of received elements
     */
    public List<T> getReceived() {
      return received;
    }

    /**
     * Get error from subscription
     *
     * @return the error, which may be null
     */
    public RuntimeException getError() {
      if (errors.size() > 0) {
        return errors.get(0);
      }
      return null;
    }

    /**
     * Get received elements.
     *
     * @return the list of receive elements
     */
    public List<T> get() {
      return await().getReceived();
    }

    /**
     * Get received elements.
     *
     * @param timeout how long to wait
     * @param unit the time unit
     * @return the list of receive elements
     */
    public List<T> get(final long timeout, final TimeUnit unit) {
      return await(timeout, unit).getReceived();
    }


    /**
     * Get the first received element.
     *
     * @return the first received element
     */
    public T first() {
      List<T> received = await().getReceived();
      return received.size() > 0 ? received.get(0) : null;
    }

    /**
     * Await completion or error
     *
     * @return this
     */
    public ObservableSubscriber<T> await() {
      return await(60, TimeUnit.SECONDS);
    }

    /**
     * Await completion or error
     *
     * @param timeout how long to wait
     * @param unit the time unit
     * @return this
     */
    public ObservableSubscriber<T> await(final long timeout, final TimeUnit unit) {
      subscription.request(Integer.MAX_VALUE);
      try {
        if (!latch.await(timeout, unit)) {
          throw new MongoTimeoutException("Publisher onComplete timed out");
        }
      } catch (InterruptedException e) {
        throw InterruptionUtil.interruptAndCreateMongoInterruptedException("Interrupted waiting for observeration", e);
      }
      if (!errors.isEmpty()) {
        throw errors.get(0);
      }
      return this;
    }

    // BEGIN COPY-PASTE FROM `com.mongodb.internal.connection.InterruptionUtil`
    /*
     * Copyright 2008-present MongoDB, Inc.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *   http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     *
     */
    private static class InterruptionUtil {
      /**
       * {@linkplain Thread#interrupt() Interrupts} the {@linkplain Thread#currentThread() current thread}
       * before creating {@linkplain MongoInterruptedException}.
       * We do this because the interrupt status is cleared before throwing {@link InterruptedException},
       * we are not propagating {@link InterruptedException}, which means we must reinstate the interrupt status.
       * This matches the behavior documented by {@link MongoInterruptedException}.
       */
      public static MongoInterruptedException interruptAndCreateMongoInterruptedException(
        @Nullable final String msg, @Nullable final InterruptedException cause) {
        Thread.currentThread().interrupt();
        return new MongoInterruptedException(msg, cause);
      }

      /**
       * If {@code e} is {@link InterruptedException}, then {@link #interruptAndCreateMongoInterruptedException(String, InterruptedException)}
       * is used.
       *
       * @return {@link Optional#empty()} iff {@code e} does not communicate an interrupt.
       */
      public static Optional<MongoInterruptedException> translateInterruptedException(
        @Nullable final Throwable e, @Nullable final String message) {
        if (e instanceof InterruptedException) {
          return Optional.of(interruptAndCreateMongoInterruptedException(message, (InterruptedException) e));
        } else if (
          // `InterruptedIOException` is weirdly documented, and almost seems to be a relic abandoned by the Java SE APIs:
          // - `SocketTimeoutException` is `InterruptedIOException`,
          //   but it is not related to the Java SE interrupt mechanism. As a side note, it does not happen when writing.
          // - Java SE methods, where IO may indeed be interrupted via the Java SE interrupt mechanism,
          //   use different exceptions, like `ClosedByInterruptException` or even `SocketException`.
          (e instanceof InterruptedIOException && !(e instanceof SocketTimeoutException))
            // see `java.nio.channels.InterruptibleChannel`
            // and `java.net.Socket.connect`, `java.net.Socket.getOutputStream`/`getInputStream`
            || e instanceof ClosedByInterruptException
            // see `java.net.Socket.connect`, `java.net.Socket.getOutputStream`/`getInputStream`
            || (e instanceof SocketException && Thread.currentThread().isInterrupted())) {
          // The interrupted status is not cleared before throwing `ClosedByInterruptException`/`SocketException`,
          // so we do not need to reinstate it.
          // `InterruptedIOException` does not specify how it behaves with regard to the interrupted status, so we do nothing.
          return Optional.of(new MongoInterruptedException(message, (Exception) e));
        } else {
          return Optional.empty();
        }
      }

      private InterruptionUtil() {
      }
    }
  }
  // END OF COPY-PASTE FROM `com.mongodb.internal.connection.InterruptionUtil`

  /**
   * A Subscriber that immediately requests Integer.MAX_VALUE onSubscribe
   *
   * @param <T> The publishers result type
   */
  public static class OperationSubscriber<T> extends ObservableSubscriber<T> {

    @Override
    public void onSubscribe(final Subscription s) {
      super.onSubscribe(s);
      s.request(Integer.MAX_VALUE);
    }
  }

  /**
   * A Subscriber that prints a message including the received items on completion
   *
   * @param <T> The publishers result type
   */
  public static class PrintSubscriber<T> extends OperationSubscriber<T> {
    private final String message;

    /**
     * A Subscriber that outputs a message onComplete.
     *
     * @param message the message to output onComplete
     */
    public PrintSubscriber(final String message) {
      this.message = message;
    }

    @Override
    public void onComplete() {
      System.out.printf((message) + "%n", getReceived());
      super.onComplete();
    }
  }

  /**
   * A Subscriber that prints the json version of each document
   */
  public static class PrintDocumentSubscriber extends ConsumerSubscriber<Document> {
    /**
     * Construct a new instance
     */
    public PrintDocumentSubscriber() {
      super(t -> System.out.println(t.toJson()));
    }
  }

  /**
   * A Subscriber that prints the toString version of each element
   * @param <T> the type of the element
   */
  public static class PrintToStringSubscriber<T> extends ConsumerSubscriber<T> {
    /**
     * Construct a new instance
     */
    public PrintToStringSubscriber() {
      super(System.out::println);
    }
  }

  /**
   * A Subscriber that processes a consumer for each element
   * @param <T> the type of the element
   */
  public static class ConsumerSubscriber<T> extends OperationSubscriber<T> {
    private final Consumer<T> consumer;

    /**
     * Construct a new instance
     * @param consumer the consumer
     */
    public ConsumerSubscriber(final Consumer<T> consumer) {
      this.consumer = consumer;
    }


    @Override
    public void onNext(final T document) {
      super.onNext(document);
      consumer.accept(document);
    }
  }

  private SubscriberHelpers() {
  }
}
