package kr.co.galaxy.gateway.security;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {

    private Object credentials;
    private Object principal;
    private String name;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public UserAuthentication(
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }
    public UserAuthentication(){
        super(null);

    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
