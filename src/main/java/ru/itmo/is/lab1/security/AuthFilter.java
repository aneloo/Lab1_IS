package ru.itmo.is.lab1.security;

import jakarta.inject.Inject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class AuthFilter extends HttpFilter {

    @Inject
    private UserSession userSession;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (isPublic(path) || userSession.isLoggedIn()) {
            chain.doFilter(req, res);
            return;
        }
        if (path.startsWith("/jakarta.faces.push/")) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String loginUrl = req.getContextPath() + "/login.xhtml";
        if ("partial/ajax".equals(req.getHeader("Faces-Request"))) {
            res.setContentType("text/xml");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<partial-response><redirect url=\"%s\"></redirect></partial-response>", loginUrl);
            return;
        }
        res.sendRedirect(loginUrl);
    }

    private static boolean isPublic(String path) {
        return path.equals("/login.xhtml")
                || path.equals("/register.xhtml")
                || path.startsWith("/jakarta.faces.resource/")
                || path.startsWith("/resources/");
    }
}
