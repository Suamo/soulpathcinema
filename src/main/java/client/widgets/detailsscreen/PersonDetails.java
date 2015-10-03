package client.widgets.detailsscreen;

import client.widgets.MainScreen;
import client.widgets.MainScreen.SaveTokenListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import shared.entity.Person;
import shared.entity.Token;

import static client.SpConstants.DISPLAY_NONE;

/**
 * Created by John Silver on 20.20.2015 20:39
 */
public class PersonDetails extends Composite {
    @UiField
    InputElement nameField;
    @UiField
    SpanElement directorFilter;
    @UiField
    InputElement isDirectorCheckbox;

    private SaveTokenListener saveListener;
    private Token token;
    private MainScreen.ChangeFilterListener changeFilterListener;

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

        DOM.sinkEvents(directorFilter, Event.ONCLICK);
        Event.setEventListener(directorFilter, new EventListener() {
            public void onBrowserEvent(Event event) {
                changeFilterListener.filter(MainScreen.FilterType.DIRECTOR, nameField.getValue());
            }
        });

        DOM.sinkEvents(isDirectorCheckbox, Event.ONCLICK);
        Event.setEventListener(isDirectorCheckbox, new EventListener() {
            public void onBrowserEvent(Event event) {
                save();
            }
        });
    }

    public void show(Token token) {
        this.token = token;
        removeStyleName(DISPLAY_NONE);
        if (token.getPerson() == null) {
            nameField.setValue("");
        } else {
            nameField.setValue(token.getPerson().getName());
            isDirectorCheckbox.setChecked(token.getPerson().isDirector());
        }
    }

    private void save() {
        if (token.getPerson() == null) {
            token.setMovie(null);
            token.setPerson(new Person());
        }
        token.getPerson().setName(nameField.getValue());
        token.getPerson().setIsDirector(isDirectorCheckbox.isChecked());
        saveListener.save(token);
    }

    public void hide() {
        addStyleName(DISPLAY_NONE);
        nameField.setValue("");
    }

    public void setSaveListener(SaveTokenListener saveListener) {
        this.saveListener = saveListener;
    }

    public void setChangeFilterListener(MainScreen.ChangeFilterListener changeFilterListener) {
        this.changeFilterListener = changeFilterListener;
    }

    interface PersonDetailsUiBinder extends UiBinder<HTMLPanel, PersonDetails> {
    }

    private static PersonDetailsUiBinder ourUiBinder = GWT.create(PersonDetailsUiBinder.class);
}