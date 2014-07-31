package controllers;

import play.libs.F;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

import java.util.Optional;

/**
 * Created by mati on 26/07/2014.
 */
public final class RxPlay {

    public static <R> F.Promise<R> toPromise(final Observable<R> obs) {
        final RxPlayObserver rxPlayObserver = new RxPlayObserver();
        final Subscription subscription = obs.subscribe(rxPlayObserver);

        return rxPlayObserver.toFPromise().map(val -> {
            subscription.unsubscribe();
            return val;
        });
    }

    public static <E> Observable<E> toObservable(final F.Promise<E> promise) {
        return Observable.create((Observable.OnSubscribe<E>) subscriber -> {
            promise.onRedeem(data -> {
                subscriber.onNext(data);
                subscriber.onCompleted();
            });

            promise.onFailure(t -> subscriber.onError(t));
        });
    }

    private static class RxPlayObserver<T> implements Observer<T> {

        private final F.RedeemablePromise<T> rPromise = F.RedeemablePromise.empty();

        private Optional<T> data = Optional.empty();

        public F.Promise<T> toFPromise() {
            return rPromise;
        }

        @Override
        public void onNext(final T data) {
            this.data = Optional.ofNullable(data);
        }

        @Override
        public void onCompleted() {
            data.ifPresent(d -> rPromise.success(d));
        }

        @Override
        public void onError(final Throwable t) {
            rPromise.failure(t);
        }

    }

}
