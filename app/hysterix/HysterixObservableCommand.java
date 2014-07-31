package hysterix;

import com.github.mati1979.play.hysterix.HysterixCommand;
import com.github.mati1979.play.hysterix.HysterixRequestContext;
import controllers.RxPlay;
import rx.Observable;

/**
* Created by mati on 30/07/2014.
*/
public abstract class HysterixObservableCommand<T> extends HysterixCommand<T> {

    protected HysterixObservableCommand(final HysterixRequestContext hysterixRequestContext) {
        super(hysterixRequestContext);
    }

    protected Observable<T> observe() {
        return RxPlay.toObservable(run());
    }

}
