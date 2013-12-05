package com.estudiopicket.http;

import java.io.IOException;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.picketlink.Identity;
import org.picketlink.idm.model.Partition;

/**
 * <p>Simple {@Filter} to protected companies resources from unauthorized access.</p>
 */
@WebFilter(urlPatterns = RealmProtectionFilter.REALM_BASE_URI + "/*")
public class RealmProtectionFilter implements Filter {

    public static final String REALM_BASE_URI = "/companies";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Inject
    private Instance<Identity> identityInstance;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean isAuthorized = getIdentity().isLoggedIn();

        if (getIdentity().isLoggedIn() && isProtectedResource(httpRequest)) {
            isAuthorized = isUserBaseRealmURI(httpRequest);
        }

        if (isAuthorized) {
            chain.doFilter(httpRequest, httpResponse);
        } else {
            forwardAccessDeniedPage(httpRequest, httpResponse);
        }
    }

    private boolean isUserBaseRealmURI(HttpServletRequest httpRequest) {
        Identity identity = getIdentity();
        Partition partition = identity.getAccount().getPartition(); // this is safe

        String allowedRealmBaseURI = getRealmBaseURI(httpRequest) + "/" + partition.getName();

        // let's check if the user trying to access a resource from his realm. if not deny.
        return httpRequest.getRequestURI().startsWith(allowedRealmBaseURI);
    }

    private void forwardAccessDeniedPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        httpRequest.getServletContext().getRequestDispatcher("/accessDenied.jsf").forward(httpRequest, httpResponse);
    }

    private String getRealmBaseURI(HttpServletRequest httpRequest) {
        return httpRequest.getContextPath() + REALM_BASE_URI;
    }

    private boolean isProtectedResource(HttpServletRequest request) {
        return !request.getRequestURI().equals(getRealmBaseURI(request) + "/index.jsf");
    }

    private Identity getIdentity() {
        return this.identityInstance.get();
    }

    @Override
    public void destroy() {
    }
}