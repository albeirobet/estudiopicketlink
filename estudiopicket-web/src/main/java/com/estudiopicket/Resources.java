package com.estudiopicket;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.picketlink.annotations.PicketLink;

/**
 * This class uses CDI to alias Java EE resources, such as the {@link FacesContext}, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private FacesContext facesContext;
 * </pre>
 */
@Stateful
public class Resources {

    @PersistenceContext(unitName = "picketlink-default")
    private EntityManager em;

    /*
     * Since we are using JPAIdentityStore to store identity-related data, we must provide it with an EntityManager via a
     * producer method or field annotated with the @PicketLink qualifier.
     */
    @Produces
    @PicketLink
    public EntityManager getPicketLinkEntityManager() {
        return em;
    }

    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
