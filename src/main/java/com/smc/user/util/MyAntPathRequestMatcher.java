package com.smc.user.util;

import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestVariablesExtractor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Data
public class MyAntPathRequestMatcher implements RequestMatcher, RequestVariablesExtractor {

    private AntPathRequestMatcher antPathRequestMatcher;

    private HttpMethod httpMethod;

    private String pattern;

    private String targetMethod;

    private String targetUrl;

    @Override
    public boolean matches(HttpServletRequest request) {
        if (this.httpMethod != null && StringUtils.hasText(targetMethod)
                && this.httpMethod != HttpMethod.valueOf(targetMethod)) {

            return false;
        }

        if (this.pattern.equals("/**")) {
            return true;
        }

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match(targetUrl, pattern);
    }

    @Override
    public Map<String, String> extractUriTemplateVariables(HttpServletRequest request) {
        return antPathRequestMatcher.extractUriTemplateVariables(request);
    }
}
