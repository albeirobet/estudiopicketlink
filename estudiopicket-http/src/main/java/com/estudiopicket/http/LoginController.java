package com.estudiopicket.http;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.Identity;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.IdentityManager;

/**
 * We control the authentication process from this bean, so that in the event of a failed authentication we can add an
 * appropriate FacesMessage to the response.
 *
 * @author Pedro Igor
 */
@Named
@RequestScoped
public class LoginController {

    @Inject
    private Identity identity;

    @Inject
    private DefaultLoginCredentials loginCredentials;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private FacesContext facesContext;

    public String login() {
        this.identity.login();

        String result = null;

        if (this.identity.isLoggedIn()) {
            result = "/companies/index.xhtml";
        } else {
            this.facesContext.addMessage(null, new FacesMessage(
                    "Authentication was unsuccessful.  Please check your username and password " + "before trying again."));
        }

        return result;
    }

    public String logout() {
        this.identity.logout();
        return "/home.xhtml";
    }

}