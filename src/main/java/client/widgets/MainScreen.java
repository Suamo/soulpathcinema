package client.widgets;

import client.SpConstants;
import client.SpUtils;
import client.rpc.MainAppService;
import client.widgets.detailsscreen.MovieDetailsScreen;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import shared.MapDto;
import shared.Movie;
import shared.TokenDto;

import java.util.HashMap;

import static client.SpConstants.DISPLAY_NONE;
import static com.google.gwt.dom.client.Style.Unit.PX;

/**
 * Created by John Silver on 05.14.2015 14:33
 */
public class MainScreen extends Composite {

    public static final String DEFAULT_TOKEN_IMG = "/img/unknown_token.svg";

    @UiField
    DivElement loadingScreen;
    @UiField
    DivElement svgLayer;
    @UiField
    DivElement imagesLayer;
    @UiField
    DivElement mandala;
    @UiField
    MovieDetailsScreen movieDetails;
    @UiField
    DivElement popupContent;

    private HashMap<String, TokenDto> knownMovies;

    public MainScreen() {
        initWidget(ourUiBinder.createAndBindUi(this));

        updatePoster(popupContent, null);
        MainAppService.App.getInstance().getMap(new AsyncCallback<MapDto>() {
            public void onSuccess(final MapDto mapDto) {
                knownMovies = mapDto.getKnownMovies();
                svgLayer.setInnerHTML(mapDto.getMap());

                DOM.sinkEvents(imagesLayer, Event.ONMOUSEOVER | Event.ONCLICK);
                Event.setEventListener(imagesLayer, new EventListener() {
                    public void onBrowserEvent(Event event) {
                        if (Event.ONMOUSEOVER == event.getTypeInt()) {
                            Element element = getEventTarget(event);
                            if (element == null) {
                                return;
                            }
                            String id = element.getId();
                            if (id != null) {
                                showTokenDetails(id);
                            }
                        } else if (Event.ONCLICK == event.getTypeInt()) {
                            Element element = getEventTarget(event);
                            if (element == null) {
                                return;
                            }
                            String id = element.getId();
                            if (id != null) {
                                movieDetails.show(knownMovies.get(id).getMovie());
                            }
                        }
                    }
                });

                DOM.sinkEvents(mandala, Event.ONCLICK);
                Event.setEventListener(mandala, new EventListener() {
                    public void onBrowserEvent(Event event) {
                        movieDetails.hide();
                    }
                });

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    public void execute() {
                        for (TokenDto token : knownMovies.values()) {
                            imagesLayer.appendChild(newTokenElement(token));
                        }
                        Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
                            public boolean execute() {
                                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                                    public void execute() {
                                        SpUtils.listenToNativeEvent(SpUtils.ANIMATION_EVENT, loadingScreen, new EventListener() {
                                            public void onBrowserEvent(Event event) {
                                                loadingScreen.addClassName(DISPLAY_NONE);
                                            }
                                        });
                                        loadingScreen.addClassName("hide");
                                    }
                                });
                                return false;
                            }
                        }, 1000);
                    }
                });
            }

            public void onFailure(Throwable caught) {
                System.out.println(caught.getMessage());
            }
        });
    }

    private Element getEventTarget(Event event) {
        EventTarget target = event.getEventTarget();
        Element element = Element.as(target);
        if (element.getClassName().contains("img-circle")) {
            return element;
        }
        return null;
    }

    private Element newTokenElement(TokenDto tokenDto) {
        Element element = DOM.getElementById(tokenDto.getDomId());
        int offsetLeft = element.getAbsoluteLeft();
        int offsetTop = element.getAbsoluteTop();

        Element token = DOM.createDiv();
        token.setId(tokenDto.getDomId());
        token.addClassName("token");
        token.addClassName(tokenDto.getDomId());
        token.addClassName("img-circle circle-size" + tokenDto.getSize());
        token.getStyle().setLeft(offsetLeft, PX);
        token.getStyle().setTop(offsetTop, PX);
        updatePoster(token, tokenDto.getMovie());
        return token;
    }

    private void showTokenDetails(String elementId) {
        updatePoster(popupContent, knownMovies.get(elementId).getMovie());
    }

    private void updatePoster(Element content, Movie movie) {
        if (movie == null || movie.getPoster() == null) {
            content.getStyle().setBackgroundImage("url('" + DEFAULT_TOKEN_IMG + "')");
        } else {
            String poster = movie.getPoster();
            content.getStyle().setBackgroundImage("url('" + poster + "')");
        }
    }


    interface MainScreenUiBinder extends UiBinder<HTMLPanel, MainScreen> {
    }

    private static MainScreenUiBinder ourUiBinder = GWT.create(MainScreenUiBinder.class);
}