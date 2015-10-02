package client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import shared.MapDto;
import shared.entity.Token;

/**
 * Created by John Silver on 05.14.2015 14:33
 */
public interface MainAppServiceAsync {
    void getMap(AsyncCallback<MapDto> async);

    void saveToken(Token dto, AsyncCallback<Void> async);
}
