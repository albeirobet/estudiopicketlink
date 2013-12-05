package com.estudiopicket.http;

import org.picketlink.annotations.PicketLink;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.model.basic.Realm;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * <p>We use this class to hold the current realm for a specific user.</p>
 */
@SessionScoped
@Named
public class RealmSelector implements Serializable {

    @Inject
    private PartitionManager partitionManager;

    private Realm realm;

    @Produces
    @PicketLink
    public Realm select() {
        return this.realm;
    }

    public Resources.REALM getRealm() {
        if (this.realm == null) {
            return null;
        }

        return Resources.REALM.valueOf(this.realm.getName());
    }

    public void setRealm(Resources.REALM realm) {
        this.realm = this.partitionManager.getPartition(Realm.class, realm.name());
    }
}
