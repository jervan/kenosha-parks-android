package edu.uwp.appfactory.eventsmanagement.model.Realm;

import io.realm.RealmObject;

/**
 * Created by hanh on 3/25/17.
 */

public class RealmString extends RealmObject {
    private String string;

    public RealmString() {
    }

    public RealmString(String string) {
        this.string = string;
    }

    @Override
    public String toString(){
        return string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
