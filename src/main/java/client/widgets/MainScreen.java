package client.widgets;

import client.SpUtils;
import client.rpc.MainAppService;
import client.widgets.detailsscreen.TokenDetailsScreen;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import shared.MapDto;
import shared.entity.Token;

import java.util.HashMap;

import static client.SpConstants.DISPLAY_NONE;
import static com.google.gwt.dom.client.Style.Unit.PX;

/**
 * Created by John Silver on 05.14.2015 14:33
 * dimentions:
 * big images: 791*791
 * small images: 57*57
 */
public class MainScreen extends Composite {

    public static final String DEFAULT_MAIN_TOKEN_IMG = "/img/author.jpg";
    public static final String DEFAULT_TOKEN_IMG = "/img/unknown_token.svg";
    public static final String SOUL_PATH_CINEMA_LINK = "https://soulpathcinema.wordpress.com/";
    public static final String POPUP_SHOW = "show";
    public static final String POPUP_HIDE = "hide";

    @UiField
    DivElement loadingScreen;
    @UiField
    DivElement svgLayer;
    @UiField
    DivElement imagesLayer;
    @UiField
    DivElement mandala;
    @UiField
    TokenDetailsScreen movieDetails;
    @UiField
    DivElement mainPoster;

    @UiField
    DivElement successSavingInfoBox;
    @UiField
    DivElement wrongSavingInfoBox;

    private HashMap<String, Token> knownMovies;
    private boolean initPosterDisplayed = true;

    public MainScreen() {
        initWidget(ourUiBinder.createAndBindUi(this));

        updatePoster(true, mainPoster, null);

        movieDetails.setSaveListener(new SaveTokenListener() {
            public void save(Token dto) {
                MainAppService.App.getInstance().saveToken(dto, new AsyncCallback<Token>() {
                    public void onFailure(Throwable caught) {
                        showTemoraryPopup(wrongSavingInfoBox);
                    }

                    public void onSuccess(Token token) {
                        showTemoraryPopup(successSavingInfoBox);
                        knownMovies.put(token.getDomId(), token);
                        movieDetails.updateModel(token);
                    }
                });
            }
        });

        MainAppService.App.getInstance().getMap(new AsyncCallback<MapDto>() {
            public void onSuccess(final MapDto mapDto) {
                knownMovies = mapDto.getTokens();
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
                                initPosterDisplayed = false;
                                updatePoster(true, mainPoster, knownMovies.get(id));
                            }
                        } else if (Event.ONCLICK == event.getTypeInt()) {
                            Element element = getEventTarget(event);
                            if (element == null) {
                                return;
                            }
                            String id = element.getId();
                            if (id != null) {
                                movieDetails.show(knownMovies.get(id));
                            }
                        }
                    }
                });

                DOM.sinkEvents(mandala, Event.ONCLICK);
                Event.setEventListener(mandala, new EventListener() {
                    public void onBrowserEvent(Event event) {
                        if (initPosterDisplayed) {
                            Window.open(SOUL_PATH_CINEMA_LINK, "_blank", "");
                            return;
                        }
                        movieDetails.hide();
                    }
                });

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    public void execute() {
                        for (Token token : knownMovies.values()) {
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

    private void showTemoraryPopup(final DivElement infoBox) {
        infoBox.removeClassName(POPUP_HIDE);
        infoBox.addClassName(POPUP_SHOW);
        Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
            public boolean execute() {
                infoBox.addClassName(POPUP_HIDE);
                return false;
            }
        }, 2000);
    }

    private Element getEventTarget(Event event) {
        EventTarget target = event.getEventTarget();
        Element element = Element.as(target);
        if (element.getClassName().contains("img-circle")) {
            return element;
        }
        return null;
    }

    private Element newTokenElement(Token tokenDto) {
        Element element = DOM.getElementById(tokenDto.getDomId());
        int offsetLeft = element.getAbsoluteLeft();
        int offsetTop = element.getAbsoluteTop();

        Element token = DOM.createDiv();
        token.setId(tokenDto.getDomId());
        token.addClassName("token");
        token.addClassName(tokenDto.getDomId());
        token.addClassName("img-circle circle-size" + tokenDto.getSize());
        if (tokenDto.getMovie() != null || tokenDto.getPerson() != null) {
            token.addClassName("solved");
        }
        token.getStyle().setLeft(offsetLeft, PX);
        token.getStyle().setTop(offsetTop, PX);

        updatePoster(false, token, tokenDto);
        return token;
    }

    private void updatePoster(boolean isMainPoster, Element content, Token tokenDto) {
        content.getStyle().setProperty("background", "");
        if (tokenDto == null || tokenDto.getCategoryName() == null || tokenDto.getCategoryId() == null) {
            content.getStyle().setBackgroundImage("url('" + (isMainPoster ? DEFAULT_MAIN_TOKEN_IMG : DEFAULT_TOKEN_IMG) + "')");
        } else {
            if (isMainPoster) {
                content.getStyle().setBackgroundImage("url('" + requestFullTokenImage(tokenDto.getCategoryName(), tokenDto.getCategoryId()) + "')");
            } else {
                content.getStyle().setBackgroundImage("url('/img/movies/" + tokenDto.getCategoryName() + "/" + tokenDto.getCategoryId() + ".jpg')");
            }
        }
    }

    private String requestFullTokenImage(String category, String id) {
        return "/MainApp/ImageServlet?category=" + category + "&id=" + id;
    }

    public static interface SaveTokenListener {
        void save(Token dto);
    }

    interface MainScreenUiBinder extends UiBinder<HTMLPanel, MainScreen> {
    }

    private static MainScreenUiBinder ourUiBinder = GWT.create(MainScreenUiBinder.class);
}