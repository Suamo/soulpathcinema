package client.widgets;

import client.SpUtils;
import client.rpc.MainAppService;
import client.widgets.detailsscreen.TokenDetailsScreen;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.*;
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
import shared.UserDto;
import shared.entity.Movie;
import shared.entity.Person;
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
    public static final String DEFAULT_FILTER_STATE = "Solved";
    public static final String LOGGED = "logged";
    public static final String ERROR = "error";

    @UiField
    DivElement loadingScreen;
    @UiField
    DivElement svgLayer;
    @UiField
    DivElement imagesLayer;
    @UiField
    DivElement mandala;
    @UiField
    TokenDetailsScreen tokenDetails;
    @UiField
    DivElement mainPoster;

    @UiField
    DivElement successSavingInfoBox;
    @UiField
    DivElement wrongSavingInfoBox;
    @UiField
    DivElement loginInfoBox;
    @UiField
    SpanElement filterIndicator;
    @UiField
    DivElement singInInitButton;
    @UiField
    DivElement singIn;
    @UiField
    DivElement logIn;
    @UiField
    InputElement emailField;
    @UiField
    InputElement passwordField;

    private HashMap<String, Token> knownMovies;
    private boolean initPosterDisplayed = true;
    private FilterType filterType = FilterType.NONE;
    private String filterValue = "";
    private UserDto user;

    public MainScreen() {
        initWidget(ourUiBinder.createAndBindUi(this));

        updatePoster(true, mainPoster, null);

        tokenDetails.setSaveListener(new SaveTokenListener() {
            public void save(Token dto) {
                MainAppService.App.getInstance().saveToken(dto, new AsyncCallback<Token>() {
                    public void onFailure(Throwable caught) {
                        showTemporaryPopup(wrongSavingInfoBox);
                    }

                    public void onSuccess(Token token) {
                        showTemporaryPopup(successSavingInfoBox);
                        knownMovies.put(token.getDomId(), token);
                        tokenDetails.updateModel(token);
                    }
                });
            }
        });

        tokenDetails.setChangeFilterListener(new ChangeFilterListener() {
            public void filter(FilterType type, String value) {
                filterTokens(type, value);
            }
        });

        reloadModel(null);

        DOM.sinkEvents(singInInitButton, Event.ONCLICK);
        Event.setEventListener(singInInitButton, new EventListener() {
            public void onBrowserEvent(Event event) {
                //todo do not react if logged in
                loginInfoBox.addClassName("show");
            }
        });

        DOM.sinkEvents(singIn, Event.ONCLICK);
        Event.setEventListener(singIn, new EventListener() {
            public void onBrowserEvent(Event event) {
                if (!isCredentialsValid()) {
                    return;
                }
                MainAppService.App.getInstance().singIn(emailField.getValue(), passwordField.getValue(), new AsyncCallback<UserDto>() {
                    public void onFailure(Throwable caught) {
                        passwordField.addClassName(ERROR);
                        emailField.addClassName(ERROR);
                    }

                    public void onSuccess(UserDto user) {
                        if (user == null) {
                            passwordField.addClassName(ERROR);
                            emailField.addClassName(ERROR);
                            return;
                        } else {
                            passwordField.removeClassName(ERROR);
                            emailField.removeClassName(ERROR);
                        }
                        reloadModel(user);
                        loginInfoBox.removeClassName("show");
                        //todo clear email and password fields
                    }
                });
            }
        });

        DOM.sinkEvents(logIn, Event.ONCLICK);
        Event.setEventListener(logIn, new EventListener() {
            public void onBrowserEvent(Event event) {
                if (!isCredentialsValid()) {
                    return;
                }
                MainAppService.App.getInstance().logIn(emailField.getValue(), passwordField.getValue(), new AsyncCallback<UserDto>() {
                    public void onFailure(Throwable caught) {
                        passwordField.addClassName(ERROR);
                        emailField.addClassName(ERROR);
                    }

                    public void onSuccess(UserDto user) {
                        if (user == null) {
                            passwordField.addClassName(ERROR);
                            emailField.addClassName(ERROR);
                            return;
                        } else {
                            passwordField.removeClassName(ERROR);
                            emailField.removeClassName(ERROR);
                        }
                        reloadModel(user);
                        loginInfoBox.removeClassName("show");
                        //todo clear email and password fields
                    }
                });
            }
        });
    }

    private boolean isCredentialsValid() {
        if (!isEmailValid(emailField.getValue())) {
            emailField.addClassName(ERROR);
            return false;
        }
        emailField.removeClassName(ERROR);
        if (passwordField.getValue().isEmpty()) {
            passwordField.addClassName(ERROR);
            return false;
        }
        passwordField.removeClassName(ERROR);
        return true;
    }

    public native boolean isEmailValid(String email) /*-{
        return /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i.test(email);
    }-*/;

    private void reloadModel(UserDto user) {
        MainAppService.App.getInstance().getMap(user, new AsyncCallback<MapDto>() {
            public void onSuccess(final MapDto mapDto) {
                MainScreen.this.user = mapDto.getUser();
                if (mapDto.getUser() != null) {
                    singInInitButton.addClassName(LOGGED);
                    singInInitButton.setInnerText(LOGGED);
                } else {
                    singInInitButton.removeClassName(LOGGED);
                    singInInitButton.setInnerText("Sing in");
                }
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
                                tokenDetails.show(knownMovies.get(id));
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
                        tokenDetails.hide();
                    }
                });

                DOM.sinkEvents(filterIndicator, Event.ONCLICK);
                Event.setEventListener(filterIndicator, new EventListener() {
                    public void onBrowserEvent(Event event) {
                        filterTokens(FilterType.AVAILABILITY, DEFAULT_FILTER_STATE);
                    }
                });

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    public void execute() {
                        updateModel();
                    }
                });
            }

            public void onFailure(Throwable caught) {
                System.out.println(caught.getMessage());
            }
        });
    }

    private void filterTokens(FilterType type, String value) {
        filterType = type;
        filterValue = value;
        filterIndicator.setInnerText(value);
        tokenDetails.hide();
        updateModel();
    }

    private void updateModel() {
        imagesLayer.setInnerHTML(null);
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

    private void showTemporaryPopup(final DivElement infoBox) {
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
        if (filterType == FilterType.NONE || isSolved(tokenDto) || isFiltredCountry(tokenDto) || isFiltredDirector(tokenDto)) {
            token.addClassName("solved");
        }
        token.getStyle().setLeft(offsetLeft, PX);
        token.getStyle().setTop(offsetTop, PX);

        updatePoster(false, token, tokenDto);
        return token;
    }

    private boolean isFiltredDirector(Token tokenDto) {
        Movie movie = tokenDto.getMovie();
        Person person = tokenDto.getPerson();
        return (filterType == FilterType.DIRECTOR && movie != null &&
                movie.getDirector() != null &&
                movie.getDirector().contains(filterValue)) ||
                (person != null && person.getName() != null && person.getName().equals(filterValue));
    }

    private boolean isFiltredCountry(Token tokenDto) {
        return filterType == FilterType.COUNTRY && tokenDto.getMovie() != null &&
                tokenDto.getMovie().getCountry() != null &&
                tokenDto.getMovie().getCountry().equals(filterValue);
    }

    private boolean isSolved(Token tokenDto) {
        return filterType == FilterType.AVAILABILITY &&
                (tokenDto.getMovie() != null || tokenDto.getPerson() != null);
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

    public interface SaveTokenListener {
        void save(Token dto);
    }

    public interface ChangeFilterListener {
        void filter(FilterType type, String value);
    }

    public enum FilterType {
        NONE, AVAILABILITY, DIRECTOR, ACTOR, WRITER, COUNTRY
    }

    interface MainScreenUiBinder extends UiBinder<HTMLPanel, MainScreen> {
    }

    private static MainScreenUiBinder ourUiBinder = GWT.create(MainScreenUiBinder.class);
}