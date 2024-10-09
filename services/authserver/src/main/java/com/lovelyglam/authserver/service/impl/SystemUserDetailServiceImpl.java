package com.lovelyglam.authserver.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import com.lovelyglam.database.model.entity.SystemAccount;
import com.lovelyglam.database.repository.SystemAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemUserDetailServiceImpl implements UserDetailsService {
    private final SystemAccountRepository systemAccountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var result = systemAccountRepository.findSystemAccountByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found account with this username"));
        return new User(result.getUsername(), result.getHashPassword(), rolesToAuthority(result));
    }
    private Collection<GrantedAuthority> rolesToAuthority(SystemAccount user) {
        var roleList = new ArrayList<GrantedAuthority>();
        roleList.add(new SimpleGrantedAuthority("ROLE_SYSTEM"));
        return roleList;
    }
}
