package com.smc.user.service.impl;

import com.smc.user.service.IRbacService;
import com.smc.user.util.MyAntPathRequestMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RbacServiceImpl implements IRbacService {
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        boolean hasPermission = false;

        Collection<? extends GrantedAuthority> resources = authentication.getAuthorities();

        if (CollectionUtils.isEmpty(resources)) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : resources) {
            if (grantedAuthority != null && grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")) {
                return false;
            }
        }

        List<RequestMatcher> requestMatchers = resources.stream().map(resource -> {
            String urlAndMethod = resource.getAuthority();
            String url = urlAndMethod.split(":")[0];
            String method = urlAndMethod.split(":")[1];

            AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(url, method);
            MyAntPathRequestMatcher myAntPathRequestMatcher = new MyAntPathRequestMatcher();
            myAntPathRequestMatcher.setAntPathRequestMatcher(antPathRequestMatcher);
            myAntPathRequestMatcher.setHttpMethod(HttpMethod.valueOf(request.getHeader("sourceRequestMethod")));
            myAntPathRequestMatcher.setPattern(request.getHeader("sourceRequest"));
            myAntPathRequestMatcher.setTargetMethod(method);
            myAntPathRequestMatcher.setTargetUrl(url);

            return myAntPathRequestMatcher;
        }).collect(Collectors.toList());

        for (RequestMatcher requestMatcher: requestMatchers) {
            if (requestMatcher.matches(request)) {
                hasPermission = true;
                break;
            }
        }

        return hasPermission;
    }
}
