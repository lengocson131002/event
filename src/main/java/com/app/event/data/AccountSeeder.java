package com.app.event.data;

import com.app.event.entity.Account;
import com.app.event.enums.Role;
import com.app.event.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(value = 2)
public class AccountSeeder implements CommandLineRunner {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            Account admin = new Account()
                    .setUsername("admin")
                    .setPassword(passwordEncoder.encode("Aqswde123@"))
                    .setName("Administrator")
                    .setRole(Role.ADMIN)
                    .setEmail("admin@gmail.com");

            accountRepository.save(admin);
        }
    }
}
