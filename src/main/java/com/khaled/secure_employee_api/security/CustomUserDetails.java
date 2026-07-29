package com.khaled.secure_employee_api.security;

import com.khaled.secure_employee_api.user.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final AppUser appUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Add roles
        appUser.getRoles().forEach(role ->
                authorities.add(
                        new SimpleGrantedAuthority(
                                "ROLE_" + role.getName()
                        )
                )
        );

        // Add permissions
        appUser.getRoles().forEach(role ->
                role.getPermissions().forEach(permission ->
                        authorities.add(
                                new SimpleGrantedAuthority(
                                        permission.getName()
                                )
                        )
                )
        );

        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getUsername();
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
        return appUser.isEnabled();
    }
}
