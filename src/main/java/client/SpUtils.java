package client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.EventListener;

/**
 * Created by John Silver on 08.20.2015 20:09
 */
public class SpUtils {
    public static final String TRANSITION_EVENT = "webkitTransitionEnd";
    public static final String ANIMATION_EVENT = "webkitAnimationEnd";

    public static native void listenToNativeEvent(String eventName, Element element, EventListener onEventListener) /*-{
        var listener = function() {
            onEventListener.@com.google.gwt.user.client.EventListener::onBrowserEvent(*)(null);
            element.removeEventListener(eventName, listener, false);
        };
        element.addEventListener(eventName, listener, false);
    }-*/;
}
