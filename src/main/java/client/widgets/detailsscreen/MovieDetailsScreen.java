package client.widgets.detailsscreen;

import client.widgets.MainScreen.SaveMovieListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import shared.MovieDto;

import static client.SpConstants.DISPLAY_NONE;

/**
 * Created by John Silver on 08.17.2015 17:35
 */
public class MovieDetailsScreen extends Composite {

    @UiField
    HTMLPanel detailsScreen;
    @UiField
    InputElement titleField;
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
    DivElement glassLayer;
    private SaveMovieListener saveListener;
    private MovieDto movie;

    public MovieDetailsScreen() {
        initWidget(ourUiBinder.createAndBindUi(this));


        DOM.sinkEvents(titleField, Event.ONKEYPRESS);
        Event.setEventListener(titleField, new EventListener() {
            public void onBrowserEvent(Event event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    movie.setName(titleField.getValue());
                    saveListener.save(movie);
                    directorField.focus();
                }
            }
        });

        DOM.sinkEvents(directorField, Event.ONKEYPRESS);
        Event.setEventListener(directorField, new EventListener() {
            public void onBrowserEvent(Event event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    movie.setDirector(directorField.getValue());
                    saveListener.save(movie);
                    titleField.focus();
                }
            }
        });
    }

    public void show(MovieDto movie) {
        this.movie = movie;
        detailsScreen.removeStyleName(DISPLAY_NONE);
        titleField.setValue(movie.getName());
        directorField.setValue(movie.getDirector());
//        writerField.setInnerHTML(movie.getWriter().replaceAll(",", "<br>"));
//        actorsField.setInnerHTML(movie.getActors().replaceAll(",", "<br>"));
//        awardsField.setInnerHTML(movie.getAwards().replaceAll(",", "<br>"));
//        imdbRatingField.setInnerText(movie.getImdbRating());
    }

    public void hide() {
        detailsScreen.addStyleName(DISPLAY_NONE);
        clean();
    }

    public void clean() {
        titleField.setInnerText("");
    }

    public void setSaveListener(SaveMovieListener saveListener) {
        this.saveListener = saveListener;
    }


    interface MovieDetailsScreenUiBinder extends UiBinder<HTMLPanel, MovieDetailsScreen> {
    }

    private static MovieDetailsScreenUiBinder ourUiBinder = GWT.create(MovieDetailsScreenUiBinder.class);

}