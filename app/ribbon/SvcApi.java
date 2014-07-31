package ribbon;

import com.netflix.ribbon.RibbonRequest;
import com.netflix.ribbon.proxy.annotation.Http;
import com.netflix.ribbon.proxy.annotation.TemplateName;
import com.netflix.ribbon.proxy.annotation.Var;
import io.netty.buffer.ByteBuf;

public interface SvcApi {

    @TemplateName("makes")
    @Http(method = Http.HttpMethod.GET, uri = "/svc/r/makes/{category}")
    RibbonRequest<ByteBuf> makes(@Var("category") String category);

    @TemplateName("models")
    @Http(method = Http.HttpMethod.GET, uri = "/svc/r/models/{makeId}")
    RibbonRequest<ByteBuf> models(@Var("makeId") Integer makeId);

}
