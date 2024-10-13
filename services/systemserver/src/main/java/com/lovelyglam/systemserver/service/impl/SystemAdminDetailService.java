package com.lovelyglam.systemserver.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.entity.SystemAccount;
import com.lovelyglam.database.repository.SystemAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemAdminDetailService implements UserDetailsService {
    private final SystemAccountRepository systemAccountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemAccount account = systemAccountRepository.findSystemAccountByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found this system account"));
        return new User(
            account.getUsername(), 
            account.getHashPassword(), 
            account.isActive(),
            true,
            true,
            true,
            rolesToAuthority(account)
        );
    }
    private Collection<GrantedAuthority> rolesToAuthority(SystemAccount user) {
        var roleList = new ArrayList<GrantedAuthority>();
        roleList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return roleList;
    }
}
