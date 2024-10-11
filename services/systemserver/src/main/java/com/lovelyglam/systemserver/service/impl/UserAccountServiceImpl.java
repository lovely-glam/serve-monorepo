package com.lovelyglam.systemserver.service.impl;

import com.lovelyglam.database.model.dto.request.UserAccountRequest;
import com.lovelyglam.database.model.dto.response.ProfileResponse;
import com.lovelyglam.database.model.entity.SystemAccount;
import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.repository.SystemAccountRepository;
import com.lovelyglam.database.repository.UserAccountRepository;
import com.lovelyglam.systemserver.service.UserAccountService;

import java.math.BigDecimal;
import com.lovelyglam.systemserver.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final SystemAccountRepository systemAccountRepository;
    private final AuthUtils authUtils;

    @Override
    public ProfileResponse disableAccount(BigDecimal id) {
        var systemAccount = authUtils.getUserAccountFromAuthentication();
        if (systemAccount == null) {
            throw new AuthFailedException("Required system account to do this funcions");
        }
        if (!systemAccountRepository.findById(systemAccount.getId()).equals(systemAccount)) {
            throw new AuthFailedException("Required system account");
        }
        if (systemAccount == null) {
            throw new AuthFailedException("Required");
        }
        var userAccountDb = userAccountRepository.findById(id);
        if (userAccountDb == null) {
            throw new NotFoundException("User account not found");

        }
        try {
            var item = userAccountRepository.save(userAccountDb.get());

            return ProfileResponse.builder()
                    .fullName(item.getFullname())
                    .email(item.getEmail())
                    .avatarUrl(item.getAvatarUrl())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed change account password with reason: %s", ex.getMessage()));
        }

    }
}
