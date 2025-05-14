package com.financely.tracker.config;

import  com.financely.tracker.service.JwtService;
import com.financely.tracker.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.
                csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(registry->{
                registry.requestMatchers("/home","/register/**","/authenticate").permitAll();
                registry.requestMatchers("/admin/**").hasRole("ADMIN");
                registry.requestMatchers("/user/**").hasRole("USER");
            }).formLogin(httpSecurityFormLoginConfigurer -> {
                httpSecurityFormLoginConfigurer.loginPage("/login")
                        .successHandler(new AuthenticationSuccessHandler())
                    .permitAll();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
               .build();
    }
//In memory user
//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails normalUser = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("1234"))
//                .roles("USER")
//                .build();
//        UserDetails adminUser = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("456"))
//                .roles("ADMIN","USER")
//                .build();
//        return new InMemoryUserDetailsManager(normalUser,adminUser);
//    }
    @Bean
    public UserDetailsService userDetailsService(){
        return myUserDetailService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
