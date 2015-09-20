package client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import shared.MapDto;
import shared.MovieDto;

/**
 * Created by John Silver on 05.14.2015 14:33
 */
public interface MainAppServiceAsync {
    void getMap(AsyncCallback<MapDto> async);

    void saveMovie(MovieDto dto, AsyncCallback<Void> async);
}
