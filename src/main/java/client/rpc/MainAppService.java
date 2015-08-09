package client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;
import shared.MapDto;

/**
 * Created by John Silver on 05.14.2015 14:33
 */
@RemoteServiceRelativePath("MainAppService")
public interface MainAppService extends RemoteService {
    MapDto getMap();

    /**
     * Utility/Convenience class.
     * Use MainAppService.App.getInstance() to access static instance of MainAppServiceAsync
     */
    public static class App {
        private static final MainAppServiceAsync ourInstance = (MainAppServiceAsync) GWT.create(MainAppService.class);

        public static MainAppServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
