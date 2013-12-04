package com.estudiopicket;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;

/**
 * We control the authentication process from this action bean, so that in the event of a failed authentication we can add an
 * appropriate FacesMessage to the response.
 * 
 * @author Shane Bryzak
 * 
 */
@Stateless
@Named
public class LoginController implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7531745790200729528L;

	@Inject
    private Identity identity;

    @Inject
    private FacesContext facesContext;

    public void login() {
        AuthenticationResult result = identity.login();
        if (AuthenticationResult.FAILED.equals(result)) {
            facesContext.addMessage(
                    null,
                    new FacesMessage("Authentication was unsuccessful.  Please check your username and password "
                            + "before trying again."));
        }
    }
}
