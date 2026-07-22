package ru.ryzhukvlad.controller.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ryzhukvlad.entity.User;
import ru.ryzhukvlad.entity.UserRole;
import ru.ryzhukvlad.service.UserService;

@Controller
public class PublicAuthorizationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Autowired
    public PublicAuthorizationController(UserService userService,
                                         PasswordEncoder passwordEncoder,
                                         UserDetailsService userDetailsService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("isAuthentificationFailed", true);
        }
        return "public/authorization/login-page";
    }

    @GetMapping("/registration")
    public String getRegistrationPage() {
        return "public/authorization/registration-page";
    }

    @PostMapping("/registration")
    public String createUserAccount(@RequestParam String name,
                                    @RequestParam String email,
                                    @RequestParam String password,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        String encodedPassword = passwordEncoder.encode(password);
        userService.save(new User(name, email, encodedPassword, UserRole.USER));
        forceAutoLogin(email, request, response);
        return "redirect:/account";
    }

    private void forceAutoLogin(String email,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, request, response);
    }
}
