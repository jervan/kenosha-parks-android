package edu.uwp.appfactory.eventsmanagement.model.Realm;

import io.realm.RealmObject;

/**
 * Created by hanh on 3/25/17.
 */

public class RealmInt extends RealmObject {
    private int val;

    public RealmInt() {
    }

    public RealmInt(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
