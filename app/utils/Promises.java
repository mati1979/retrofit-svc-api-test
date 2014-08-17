package utils;

import play.libs.F;

/**
 * Created by mati on 17/08/2014.
 */
public class Promises {

    public static <T1, T2, R> F.Promise<R> zip(final F.Promise<? extends T1> a, F.Promise<? extends T2> b, final F.Function2<? super T1, ? super T2, ? extends R> zipFunction) {
        return a.flatMap(aa -> b.map(bb -> zipFunction.apply(aa, bb)));
    }

    public static <T1, T2, T3, R> F.Promise<R> zip(final F.Promise<? extends T1> a,
                                                   final F.Promise<? extends T2> b,
                                                   final F.Promise<? extends T3> c,
                                                   final F.Function3<? super T1, ? super T2, ? super T3, ? extends R> zipFunction) {
        return a.flatMap(aa -> b.flatMap(bb -> c.map(cc -> zipFunction.apply(aa, bb, cc))));
    }

}
