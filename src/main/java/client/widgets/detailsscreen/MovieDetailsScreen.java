package client.widgets.detailsscreen;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.codehaus.jackson.annotate.JsonProperty;
import shared.Movie;

import static client.SpConstants.DISPLAY_NONE;

/**
 * Created by John Silver on 08.17.2015 17:35
 */
public class MovieDetailsScreen extends Composite {

    @UiField
    HTMLPanel detailsScreen;
    @UiField
    SpanElement titleField;
    @UiField
    DivElement directorField;
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

    public MovieDetailsScreen() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void show(Movie movie) {
        detailsScreen.removeStyleName(DISPLAY_NONE);
        titleField.setInnerText(movie.getTitle());
        directorField.setInnerHTML(movie.getDirector().replaceAll(",", "<br>"));
        writerField.setInnerHTML(movie.getWriter().replaceAll(",", "<br>"));
        actorsField.setInnerHTML(movie.getActors().replaceAll(",", "<br>"));
        awardsField.setInnerHTML(movie.getAwards().replaceAll(",", "<br>"));
        imdbRatingField.setInnerText(movie.getImdbRating());
    }

    public void hide() {
        detailsScreen.addStyleName(DISPLAY_NONE);
        clean();
    }

    public void clean() {
        titleField.setInnerText("");
    }


    interface MovieDetailsScreenUiBinder extends UiBinder<HTMLPanel, MovieDetailsScreen> {
    }

    private static MovieDetailsScreenUiBinder ourUiBinder = GWT.create(MovieDetailsScreenUiBinder.class);

}