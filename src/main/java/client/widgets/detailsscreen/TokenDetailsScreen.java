package client.widgets.detailsscreen;

import client.widgets.MainScreen.SaveTokenListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import shared.entity.Token;

import static client.SpConstants.DISPLAY_NONE;

/**
 * Created by John Silver on 08.17.2015 17:35
 */
public class TokenDetailsScreen extends Composite {

    public static final String BLUE_BUTTON = "blueButton";
    public static final String GRAY_BUTTON = "grayButton";
    @UiField
    HTMLPanel detailsScreen;

    @UiField
    MovieDetails movieDetails;
    @UiField
    PersonDetails personDetails;

    @UiField
    SpanElement movieButton;
    @UiField
    SpanElement personButton;

    private Token token;

    public TokenDetailsScreen() {
        initWidget(ourUiBinder.createAndBindUi(this));

        DOM.sinkEvents(movieButton, Event.ONCLICK);
        Event.setEventListener(movieButton, new EventListener() {
            public void onBrowserEvent(Event event) {
                if(!movieButton.getClassName().contains(BLUE_BUTTON)) {
                    showMovieDetails(token);
                }
            }
        });

        DOM.sinkEvents(personButton, Event.ONCLICK);
        Event.setEventListener(personButton, new EventListener() {
            public void onBrowserEvent(Event event) {
                if(!personButton.getClassName().contains(BLUE_BUTTON)) {
                    showPersonDetails(token);
                }
            }
        });
    }

    public void show(Token token) {
        removeStyleName(DISPLAY_NONE);
        updateModel(token);
    }

    public void updateModel(Token token) {
        this.token = token;
        if (token.getPerson() != null) {
            showPersonDetails(token);
        } else {
            showMovieDetails(token);
        }
    }

    private void showMovieDetails(Token token) {
        movieDetails.show(token);
        personButton.replaceClassName(BLUE_BUTTON, GRAY_BUTTON);
        movieButton.replaceClassName(GRAY_BUTTON, BLUE_BUTTON);
        personDetails.hide();
    }

    private void showPersonDetails(Token token) {
        personDetails.show(token);
        movieButton.replaceClassName(BLUE_BUTTON, GRAY_BUTTON);
        personButton.replaceClassName(GRAY_BUTTON, BLUE_BUTTON);
        movieDetails.hide();
    }

    public void hide() {
        detailsScreen.addStyleName(DISPLAY_NONE);
        movieDetails.hide();
        personDetails.hide();
    }

    public void setSaveListener(SaveTokenListener saveListener) {
        movieDetails.setSaveListener(saveListener);
        personDetails.setSaveListener(saveListener);
    }

    interface MovieDetailsScreenUiBinder extends UiBinder<HTMLPanel, TokenDetailsScreen> {
    }

    private static MovieDetailsScreenUiBinder ourUiBinder = GWT.create(MovieDetailsScreenUiBinder.class);

}