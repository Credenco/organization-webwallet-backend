package com.credenco.webwallet.backend.rest.webmanage;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.WalletUser;
import com.credenco.webwallet.backend.rest.webmanage.dto.UserDto;
import com.credenco.webwallet.backend.rest.webmanage.form.UserForm;
import com.credenco.webwallet.backend.service.WalletUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final WalletUserService walletUserService;

    @GetMapping
    public UserDto getUser(LocalPrincipal principal) {
        final WalletUser walletUser = walletUserService.getWalletUser(principal.getWallet(), principal.getName());
        return UserDto.from(principal, walletUser);
    }

    @PostMapping
    public UserDto saveUser(LocalPrincipal principal, @RequestBody UserForm userForm){
        final WalletUser walletUser = walletUserService.saveWalletUser(principal.getWallet(), principal.getName(), userForm.getLocale());
        return UserDto.from(principal, walletUser);
    }

    @PostMapping("/preferences")
    public UserDto saveUserPreferences(LocalPrincipal principal, @RequestBody UserForm userForm) {
        final WalletUser walletUser = walletUserService.saveWalletUserPreferences(principal.getWallet(), principal.getName(), userForm.getUserPreferences());
        return UserDto.from(principal, walletUser);
    }

}
