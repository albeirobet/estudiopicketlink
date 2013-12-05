package com.estudiopicket.http;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.idm.model.basic.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class IDMInitializer {

    @Inject
    private PartitionManager partitionManager;

    /**
     * <p>Creates some default users for each realm/company.</p>
     */
    @PostConstruct
    public void createDefaultUsers() {
        createUserForRealm(Resources.REALM.acme.name(), "bugs");
        createUserForRealm(Resources.REALM.umbrella.name(), "jill");
        createUserForRealm(Resources.REALM.wayne.name(), "bruce");
    }

    private void createUserForRealm(String realmName, String loginName) {
        Realm partition = this.partitionManager.getPartition(Realm.class, realmName);

        if (partition == null) {
            partition = new Realm(realmName);
            this.partitionManager.add(partition);
        }

        IdentityManager identityManager = this.partitionManager.createIdentityManager(partition);

        User user = new User(loginName);
        Password password = new Password(user.getLoginName() + "123");

        identityManager.add(user);
        identityManager.updateCredential(user, password);
    }

}