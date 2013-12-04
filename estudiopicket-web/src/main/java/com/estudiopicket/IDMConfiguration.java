package com.estudiopicket;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.jpa.model.sample.simple.AccountTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.AttributeTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.AttributedTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.GroupTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.IdentityTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PartitionTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PasswordCredentialTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RelationshipIdentityTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RelationshipTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RoleTypeEntity;
import org.picketlink.idm.model.Relationship;
import org.picketlink.internal.EEJPAContextInitializer;

/**
 * This bean produces the configuration for PicketLink IDM
 * 
 * 
 * @author Shane Bryzak
 *
 */
@ApplicationScoped
public class IDMConfiguration {

    @Inject
    private EEJPAContextInitializer contextInitializer;

    private IdentityConfiguration identityConfig = null;

    @Produces IdentityConfiguration createConfig() {
        if (identityConfig == null) {
            initConfig();
        }
        return identityConfig;
    }

    /**
     * This method uses the IdentityConfigurationBuilder to create an IdentityConfiguration, which 
     * defines how PicketLink stores identity-related data.  In this particular example, a 
     * JPAIdentityStore is configured to allow the identity data to be stored in a relational database
     * using JPA.
     */
    private void initConfig() {
        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();

        builder
            .named("default")
                .stores()
                    .jpa()
                        .mappedEntity(
                                AccountTypeEntity.class,
                                RoleTypeEntity.class,
                                GroupTypeEntity.class,
                                IdentityTypeEntity.class,
                                RelationshipTypeEntity.class,
                                RelationshipIdentityTypeEntity.class,
                                PartitionTypeEntity.class,
                                PasswordCredentialTypeEntity.class,
                                AttributeTypeEntity.class)
                        .supportGlobalRelationship(Relationship.class)
                        .addContextInitializer(this.contextInitializer)
                        // Specify that this identity store configuration supports all features
                        .supportAllFeatures();

        identityConfig = builder.build();
    }
}

