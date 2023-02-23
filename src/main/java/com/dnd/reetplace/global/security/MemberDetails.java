package com.dnd.reetplace.global.security;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.type.LoginType;
import com.dnd.reetplace.app.type.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public class MemberDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(RoleType.NORMAL.getKey()));
    }

    @Override
    public String getPassword() {
        return member.getUid();
    }

    @Override
    public String getUsername() {
        return member.getId().toString();
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


    public Long getId() {
        return member.getId();
    }

    public String getUid() {
        return member.getUid();
    }

    public LoginType getLoginType() {
        return member.getLoginType();
    }
}
