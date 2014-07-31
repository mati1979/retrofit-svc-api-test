package retrofit;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

import java.util.List;

/**
 * Created by mati on 26/07/2014.
 */
public interface SvcApi {

    public static class Makes {

        public List<Make> makes;

        @Override
        public String toString() {
            return makes.toString();
        }

    }

    public static class Models {

        public List<Model> models = Lists.newArrayList();

        @Override
        public String toString() {
            return models.toString();
        }

    }

    public static class Make {

        @SerializedName("n")
        public String name;

        @SerializedName("i")
        public Integer id;

        @Override
        public String toString() {
            return "Make{" +
                    "name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }

    }

    public static class Model {

        @SerializedName("n")
        public String name;

        @SerializedName("i")
        public Integer id;

        @SerializedName("p")
        public Integer parent;

        @Override
        public String toString() {
            return "Model{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    ", parent=" + parent +
                    '}';
        }

    }

    @GET("/r/makes/{category}")
    Observable<Makes> makes(@Path("category") String category);

    @GET("/r/models/{makeId}")
    Observable<Models> models(@Path("makeId") Integer makeId);

}
