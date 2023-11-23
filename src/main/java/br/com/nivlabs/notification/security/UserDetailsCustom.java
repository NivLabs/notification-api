package br.com.nivlabs.notification.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsCustom implements UserDetails {

    private static final long serialVersionUID = 6481208838340753291L;

    private final String uuid;
    private final String applicationName;
    private Boolean isExpired;

    /**
     * Cria o usuário do sistema
     * 
     * @param user Usuário do token de acesso
     * @param isExpired chega se está ativo
     */
    public UserDetailsCustom(String uuid, String applicationName, Boolean isExpired) {
        super();
        this.uuid = uuid;
        this.applicationName = applicationName;
        this.isExpired = isExpired;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return uuid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isExpired;
    }

    public String getUuid() {
        return uuid;
    }

    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

}
