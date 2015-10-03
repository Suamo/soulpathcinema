package client.widgets.detailsscreen;

import client.widgets.MainScreen;
import client.widgets.MainScreen.SaveTokenListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import shared.entity.Movie;
import shared.entity.Token;

import static client.SpConstants.DISPLAY_NONE;
import static java.util.Arrays.asList;

/**
 * Created by John Silver on 20.20.2015 20:36
 */
public class MovieDetails extends Composite {
    @UiField
    InputElement titleField;
    @UiField
    InputElement imdbId;
    @UiField
    DivElement directorField;
    @UiField
    DivElement writerField;
    @UiField
    DivElement actorsField;
    @UiField
    DivElement awardsField;
    @UiField
    DivElement countryField;
    @UiField
    DivElement imdbRatingField;

    @UiField
    DivElement imdbRedirection;

    private Token token;
    private SaveTokenListener saveListener;
    private MainScreen.ChangeFilterListener changeFilterListener;

    public MovieDetails() {
        initWidget(ourUiBinder.createAndBindUi(this));

        DOM.sinkEvents(titleField, Event.ONKEYPRESS);
        Event.setEventListener(titleField, new EventListener() {
            public void onBrowserEvent(Event event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    save();
                    imdbId.focus();
                }
            }
        });

        DOM.sinkEvents(imdbId, Event.ONKEYPRESS);
        Event.setEventListener(imdbId, new EventListener() {
            public void onBrowserEvent(Event event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    save();
                    titleField.focus();
                }
            }
        });

        DOM.sinkEvents(imdbRedirection, Event.ONCLICK);
        Event.setEventListener(imdbRedirection, new EventListener() {
            public void onBrowserEvent(Event event) {
                Movie movie = token.getMovie();
                if (movie == null || movie.getImdbId() == null || movie.getImdbId().trim().length() == 0) {
                    return;
                }
                Window.open("http://www.imdb.com/title/" + movie.getImdbId(), "_blank", "");
            }
        });

        DOM.sinkEvents(countryField, Event.ONCLICK);
        Event.setEventListener(countryField, new EventListener() {
            public void onBrowserEvent(Event event) {
                if (!countryField.getInnerText().trim().isEmpty()) {
                    changeFilterListener.filter(MainScreen.FilterType.COUNTRY, countryField.getInnerText());
                }
            }
        });
    }

    public void show(Token token) {
        this.token = token;
        removeStyleName(DISPLAY_NONE);
        if (token.getMovie() == null) {
            titleField.setValue("");
        } else {
            titleField.setValue(token.getMovie().getName());
            imdbId.setValue(token.getMovie().getImdbId());

            directorField.setInnerText(token.getMovie().getDirector());

            imdbRatingField.setInnerText(token.getMovie().getImdbRating());
            actorsField.setInnerText(token.getMovie().getActors());
            awardsField.setInnerText(token.getMovie().getAwards());
            writerField.setInnerText(token.getMovie().getWriter());
            countryField.setInnerText(token.getMovie().getCountry());
        }
    }

    private void save() {
        if (token.getMovie() == null) {
            token.setPerson(null);
            token.setMovie(new Movie());
        }
        token.getMovie().setName(titleField.getValue());
        token.getMovie().setImdbId(imdbId.getValue());
        saveListener.save(token);
    }

    public void hide() {
        addStyleName(DISPLAY_NONE);
        for (InputElement field : asList(titleField, imdbId)) {
            field.setValue("");
        }
        for (DivElement field : asList(directorField, writerField, actorsField, awardsField, imdbRatingField)) {
            field.setInnerText("");
        }
    }

    public void setSaveListener(SaveTokenListener saveListener) {
        this.saveListener = saveListener;
    }

    public void setChangeFilterListener(MainScreen.ChangeFilterListener changeFilterListener) {
        this.changeFilterListener = changeFilterListener;
    }

    interface MovieDetailsUiBinder extends UiBinder<HTMLPanel, MovieDetails> {
    }

    private static MovieDetailsUiBinder ourUiBinder = GWT.create(MovieDetailsUiBinder.class);
}