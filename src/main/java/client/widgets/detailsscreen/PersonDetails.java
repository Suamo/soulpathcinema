package client.widgets.detailsscreen;

import client.widgets.MainScreen.SaveTokenListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import shared.PersonDto;
import shared.TokenDto;

import static client.SpConstants.DISPLAY_NONE;

/**
 * Created by John Silver on 20.20.2015 20:39
 */
public class PersonDetails extends Composite {
    @UiField
    InputElement nameField;

    private SaveTokenListener saveListener;
    private TokenDto token;

    public PersonDetails() {
        initWidget(ourUiBinder.createAndBindUi(this));

        DOM.sinkEvents(nameField, Event.ONKEYPRESS);
        Event.setEventListener(nameField, new EventListener() {
            public void onBrowserEvent(Event event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    save();
                }
            }
        });
    }

    public void show(TokenDto token) {
        this.token = token;
        removeStyleName(DISPLAY_NONE);
        if (token.getMovie() != null) {
            token.setMovie(null);
            token.setPerson(new PersonDto());
        }
        nameField.setValue(token.getPerson().getName());
    }

    private void save() {
        token.getPerson().setName(nameField.getValue());
        saveListener.save(token);
    }

    public void hide() {
        addStyleName(DISPLAY_NONE);
        nameField.setValue("");
    }

    public void setSaveListener(SaveTokenListener saveListener) {
        this.saveListener = saveListener;
    }

    interface PersonDetailsUiBinder extends UiBinder<HTMLPanel, PersonDetails> {
    }

    private static PersonDetailsUiBinder ourUiBinder = GWT.create(PersonDetailsUiBinder.class);
}