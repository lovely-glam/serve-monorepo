package com.lovelyglam.userserver.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.repository.UserAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findUserAccountByUsername(username).orElseThrow();
        return new User(userAccount.getUsername(),userAccount.getHashPassword(),rolesToAuthority(userAccount));
    }
    private Collection<GrantedAuthority> rolesToAuthority(UserAccount user) {
        var roleList = new ArrayList<GrantedAuthority>();
        roleList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return roleList;
    }
    
}
