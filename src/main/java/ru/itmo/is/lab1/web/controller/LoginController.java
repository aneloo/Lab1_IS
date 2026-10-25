package ru.itmo.is.lab1.web.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import ru.itmo.is.lab1.dto.UserDto;
import ru.itmo.is.lab1.security.UserSession;
import ru.itmo.is.lab1.service.AuthService;

@Named("loginController")
@RequestScoped
public class LoginController {

    @Inject
    private AuthService authService;

    @Inject
    private UserSession userSession;

    private String username;
    private String password;
    private String passwordRepeat;

    public String login() {
        UserDto user = authService.authenticate(username, password);
        startSession(user);
        return "index?faces-redirect=true";
    }

    public String register() {
        UserDto user = authService.register(username, password, passwordRepeat);
        startSession(user);
        return "index?faces-redirect=true";
    }

    public String logout() {
        userSession.logout();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }

    private void startSession(UserDto user) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        ((HttpServletRequest) context.getRequest()).changeSessionId();
        userSession.login(user.username());
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPasswordRepeat() { return passwordRepeat; }
    public void setPasswordRepeat(String passwordRepeat) { this.passwordRepeat = passwordRepeat; }
}
