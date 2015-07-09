/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one or more
 *  *  contributor license agreements.  The ASF licenses this file to You
 *  * under the Apache License, Version 2.0 (the "License"); you may not
 *  * use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.  For additional information regarding
 *  * copyright in this work, please see the NOTICE file in the top level
 *  * directory of this distribution.
 *
 */
package org.apache.usergrid.persistence.core.rx;

/**
 * Classy class class.
 */

import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Returns an Iterator that iterates over all items emitted by a specified Observable.
 * <p>
 * <img width="640" src="https://github.com/ReactiveX/RxJava/wiki/images/rx-operators/B.toIterator.png" alt="">
 * <p>
 *
 * @see <a href="https://github.com/ReactiveX/RxJava/issues/50">Issue #50</a>
 */
public final class ObservableToBlockingIteratorFactory {
    private ObservableToBlockingIteratorFactory() {
        throw new IllegalStateException("No instances!");
    }

    /**
     * Returns an iterator that iterates all values of the observable.
     *
     * @param <T>
     *            the type of source.
     * @return the iterator that could be used to iterate over the elements of the observable.
     */
    public static <T> Iterator<T> toIterator(Observable<? extends T> source) {
        final BlockingQueue<Notification<? extends T>> notifications = new ArrayBlockingQueue<>(1);

        // using subscribe instead of unsafeSubscribe since this is a BlockingObservable "final subscribe"
        final Subscription subscription = source.materialize().subscribe(new Subscriber<Notification<? extends T>>() {
            @Override
            public void onCompleted() {
                // ignore
            }

            @Override
            public void onError(Throwable e) {
                try{
                    notifications.put(Notification.<T>createOnError(e));
                }catch (Exception t){

                }
            }

            @Override
            public void onNext(Notification<? extends T> args) {
                try{
                    notifications.put(args);
                }catch (Exception t){

                }
            }
        });

        return new Iterator<T>() {
            private Notification<? extends T> buf;

            @Override
            public boolean hasNext() {
                if (buf == null) {
                    buf = take();
                }
                if (buf.isOnError()) {
                    throw Exceptions.propagate(buf.getThrowable());
                }
                return !buf.isOnCompleted();
            }

            @Override
            public T next() {
                if (hasNext()) {
                    T result = buf.getValue();
                    buf = null;
                    return result;
                }
                throw new NoSuchElementException();
            }

            private Notification<? extends T> take() {
                try {
                    return notifications.take();
                } catch (InterruptedException e) {
                    subscription.unsubscribe();
                    throw Exceptions.propagate(e);
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Read-only iterator");
            }
        };
    }

}