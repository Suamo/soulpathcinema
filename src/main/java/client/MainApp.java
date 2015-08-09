package client;

import client.widgets.MainScreen;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;

/**
 * Created by John Silver on 05.14.2015 14:33
 */
public class MainApp implements EntryPoint {
    public void onModuleLoad() {
        RootPanel.get().add(new MainScreen());
    }
}
