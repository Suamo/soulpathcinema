package client.widgets.detailsscreen;

import client.widgets.MainScreen.SaveTokenListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import shared.TokenDto;

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
    DivElement glassLayer;

    @UiField
    MovieDetails movieDetails;
    @UiField
    PersonDetails personDetails;

    @UiField
    SpanElement movieButton;
    @UiField
    SpanElement personButton;

    private TokenDto token;

    public TokenDetailsScreen() {
        initWidget(ourUiBinder.createAndBindUi(this));

        DOM.sinkEvents(movieButton, Event.ONCLICK);
        Event.setEventListener(movieButton, new EventListener() {
            public void onBrowserEvent(Event event) {
                if(!movieButton.getClassName().contains(BLUE_BUTTON)) {
                    personButton.replaceClassName(BLUE_BUTTON, GRAY_BUTTON);
                    movieButton.replaceClassName(GRAY_BUTTON, BLUE_BUTTON);
                    movieDetails.show(token);
                    personDetails.hide();
                }
            }
        });

        DOM.sinkEvents(personButton, Event.ONCLICK);
        Event.setEventListener(personButton, new EventListener() {
            public void onBrowserEvent(Event event) {
                if(!personButton.getClassName().contains(BLUE_BUTTON)) {
                    movieButton.replaceClassName(BLUE_BUTTON, GRAY_BUTTON);
                    personButton.replaceClassName(GRAY_BUTTON, BLUE_BUTTON);
                    personDetails.show(token);
                    movieDetails.hide();
                }
            }
        });
    }

    public void show(TokenDto token) {
        this.token = token;
        detailsScreen.removeStyleName(DISPLAY_NONE);
        if (token.getPerson() != null) {
            personDetails.removeStyleName(DISPLAY_NONE);
        } else {
            movieDetails.removeStyleName(DISPLAY_NONE);
        }
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