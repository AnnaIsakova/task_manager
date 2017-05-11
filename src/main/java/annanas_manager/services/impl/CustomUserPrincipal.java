package annanas_manager.services.impl;

import annanas_manager.entities.CustomUser;
import annanas_manager.entities.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


public class CustomUserPrincipal implements UserDetails {

    private CustomUser customUser;

    public CustomUserPrincipal(CustomUser user) {
        this.customUser = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> grntdAuths = new HashSet<GrantedAuthority>();

        List<UserRole> roles = new ArrayList<>();

        if (customUser != null) {
            roles.add(customUser.getRole());
        }

        for (UserRole role : roles) {
            grntdAuths.add(new SimpleGrantedAuthority(role.name()));
        }
        return grntdAuths;
    }

    @Override
    public String getPassword() {
        return customUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.customUser.getEmail();
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
        return true;
    }

    public String getFullName() {
        String name = this.customUser.getFirstName() + " " + this.customUser.getLastName();
        return name;
    }

    public long getId(){
        return this.customUser.getId();
    }
}
