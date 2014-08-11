package controllers;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netflix.ribbon.ClientOptions;
import com.netflix.ribbon.Ribbon;
import io.netty.buffer.ByteBuf;
import play.MakeModelRefDataRestClient;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import retrofit.RestAdapter;
import retrofit.SvcApi;
import rx.Observable;

import java.nio.charset.Charset;
import java.util.List;

public class Application extends Controller {

    private final static Gson gson = new GsonBuilder().create();

    public static F.Promise<Result> index() {
        final SvcApi api = new RestAdapter.Builder()
                .setEndpoint("http://m.mobile.de/svcERROR")
                .build()
                .create(SvcApi.class);

        final Observable<SvcApi.Makes> makesObservable = api.makes("Car").onErrorReturn(t -> new SvcApi.Makes());

        final F.Promise<SvcApi.Makes> makesP = RxPlay.toPromise(makesObservable);

        return makesP.map(makes -> ok(Json.toJson(makes)));
    }

    public static F.Promise<Result> index2() {
        final SvcApi api = new RestAdapter.Builder()
                .setEndpoint("http://m.mobile.de/svc")
                .build()
                .create(SvcApi.class);

        final Observable<SvcApi.Models> modelsObservable = api.models(1900);
        final F.Promise<SvcApi.Models> modelsP = RxPlay.toPromise(modelsObservable);

        return modelsP.map(models -> ok(Json.toJson(models)));
    }

    public static F.Promise<Result> index3() {
        final MakeModelRefDataRestClient makeModelRefDataRestClient = new MakeModelRefDataRestClient("http://m.mobile.de/svc", gson);

        final F.Promise<List<MakeModelRefDataRestClient.Make>> makesP = makeModelRefDataRestClient.makes("Car");

        return makesP.map(models -> ok(Json.toJson(models)));
    }

    public static F.Promise<Result> index4() {
        final MakeModelRefDataRestClient makeModelRefDataRestClient = new MakeModelRefDataRestClient("http://m.mobile.de/svc", gson);

        final F.Promise<List<MakeModelRefDataRestClient.Model>> modelsP = makeModelRefDataRestClient.models(1900);

        return modelsP.map(models -> ok(Json.toJson(models)));
    }

    public static F.Promise<Result> index5() {
        final ribbon.SvcApi svcAPI = svcApi();

        final Observable<SvcApi.Makes> makes = makes(svcAPI, "Car");
        final Observable<SvcApi.Models> audiModels = models(svcAPI, 1900);
        final Observable<SvcApi.Models> alfaRomeoModels = models(svcAPI, 900);
        final Observable<SvcApi.Models> abathModels = models(svcAPI, 140);

        return RxPlay.toPromise(Observable.zip(makes, audiModels, alfaRomeoModels, abathModels, (makes1, models1, models2, models3)
                -> ok(Json.toJson(ImmutableList.of(makes1, models1, models2)))));
    }

    private static Observable<SvcApi.Makes> makes(final ribbon.SvcApi svcApi, final String category) {
        final Observable<ByteBuf> bytesObs = svcApi.makes(category).observe();
        final Observable<SvcApi.Makes> makesObs = bytesObs.map(byteBuf -> byteBuf.toString(Charset.defaultCharset()))
                .map(response -> gson.fromJson(response, SvcApi.Makes.class))
                .onErrorReturn(t -> new SvcApi.Makes());

        return makesObs;
    }

    private static Observable<SvcApi.Models> models(final ribbon.SvcApi svcApi, final int makeId) {
        final Observable<ByteBuf> bytesObs = svcApi.models(makeId).observe();
        final Observable<SvcApi.Models> modelsObs = bytesObs.map(byteBuf -> byteBuf.toString(Charset.defaultCharset()))
                .map(response -> gson.fromJson(response, SvcApi.Models.class))
                .onErrorReturn(t -> new SvcApi.Models());

        return modelsObs;
    }

    private static ribbon.SvcApi svcApi() {
        final ClientOptions clientOptions = ClientOptions.create().withConfigurationBasedServerList("http://m.mobile.de");

        return Ribbon.from(ribbon.SvcApi.class, Ribbon.createHttpResourceGroup("SvcApi", clientOptions));
    }

}
