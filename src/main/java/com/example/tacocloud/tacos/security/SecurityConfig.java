package com.example.tacocloud.tacos.security;

import com.example.tacocloud.tacos.User;  // Добавлен импорт для кастомной сущности User
import com.example.tacocloud.tacos.data.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;

import java.util.List;

@Configuration
@EnableGlobalAuthentication
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailService(PasswordEncoder encoder) {
        List<UserDetails> userList = new ArrayList<>();
        // Используем полное имя для Spring Security User, чтобы избежать конфликта
        userList.add(new org.springframework.security.core.userdetails.User(
                "buzz", encoder.encode("password"),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))));
        userList.add(new org.springframework.security.core.userdetails.User(
                "woody", encoder.encode("password"),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))));
        return new InMemoryUserDetailsManager(userList);
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            User user = userRepo.findByUsername(username);  // Теперь User - это кастомная сущность
            if (user != null) return user;

            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/design", "/orders").hasRole("USER")  // Доступ к дизайну и заказам только для пользователей с ролью USER
                        .requestMatchers("/", "/**").permitAll()  // Остальные страницы доступны всем
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Страница входа
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")  // После выхода перенаправление на главную
                )
                .csrf(csrf -> csrf.disable());  // В Taco Cloud CSRF часто отключают для простоты, но в реальном проекте лучше включить

        return http.build();
    }
}
