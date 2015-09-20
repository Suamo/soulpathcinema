package client.widgets.detailsscreen;

import client.widgets.MainScreen.SaveTokenListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import shared.MovieDto;
import shared.TokenDto;

import static client.SpConstants.DISPLAY_NONE;

/**
 * Created by John Silver on 20.20.2015 20:36
 */
public class MovieDetails extends Composite {
    @UiField
    InputElement titleField;
    @UiField
    InputElement imdbId;
    @UiField
    InputElement directorField;
    @UiField
    DivElement writerField;
    @UiField
    DivElement actorsField;
    @UiField
    DivElement awardsField;
    @UiField
    DivElement imdbRatingField;

    @UiField
    DivElement refreshButton;

    private TokenDto token;
    private SaveTokenListener saveListener;

    public MovieDetails() {
        initWidget(ourUiBinder.createAndBindUi(this));

        DOM.sinkEvents(writerField, Event.ONCLICK);
        Event.setEventListener(writerField, new EventListener() {
            public void onBrowserEvent(Event event) {
                save();
            }
        });
    }

    public void show(TokenDto token) {
        this.token = token;
        removeStyleName(DISPLAY_NONE);
        if (token.getMovie() == null) {
            token.setPerson(null);
            token.setMovie(new MovieDto());
        }
        titleField.setValue(token.getMovie().getName());
        directorField.setValue(token.getMovie().getDirector());
    }

    private void save() {
        token.getMovie().setName(titleField.getValue());
        token.getMovie().setImdbId(imdbId.getValue());
        saveListener.save(token);
    }

    public void hide() {
        addStyleName(DISPLAY_NONE);
        titleField.setInnerText("");
    }

    public void setSaveListener(SaveTokenListener saveListener) {
        this.saveListener = saveListener;
    }

    interface MovieDetailsUiBinder extends UiBinder<HTMLPanel, MovieDetails> {
    }

    private static MovieDetailsUiBinder ourUiBinder = GWT.create(MovieDetailsUiBinder.class);
}