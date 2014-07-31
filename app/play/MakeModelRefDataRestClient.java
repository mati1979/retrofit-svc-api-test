package play;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Http;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by mati on 26/07/2014.
 */
public class MakeModelRefDataRestClient {

    private final String baseUrl;

    private final Gson gson;

    public MakeModelRefDataRestClient(final String baseUrl, final Gson gson) {
        this.baseUrl = baseUrl;
        this.gson = gson;
    }

    public static class Makes {

        public List<Make> makes;

    }

    public static class Models {

        public List<Model> models;

    }

    public static class Make {

        @SerializedName("n")
        public String name;

        @SerializedName("i")
        public Integer id;

    }

    public static class Model {

        @SerializedName("n")
        public String name;

        @SerializedName("i")
        public Integer id;

        @SerializedName("p")
        public Integer parent;

    }

    public F.Promise<List<Make>> makes(final String category) {
        final Type type = new TypeToken<List<Make>>() {}.getType();

        return WS.url(String.format("%s/r/makes/%s", baseUrl, category)).get()
                .map(response -> toDomainObject(type, response));
    }

    public F.Promise<List<Model>> models(final Integer makeId) {
        final Type type = new TypeToken<List<Model>>() {}.getType();

        return WS.url(String.format("%s/r/models/%d", baseUrl, makeId)).get()
                .map(response -> toDomainObject(type, response));
    }

    private <T> T toDomainObject(final Type type, final WSResponse response) {
        if (response.getStatus() == Http.Status.OK) {
            return gson.fromJson(response.getBody(), type);
        }

        throw new RuntimeException("Http call failed, status:" + response.getStatus());
    }

}
