package com.armemius.labwork.resolver;

import com.armemius.labwork.annotations.CurrentUser;
import com.armemius.labwork.data.domain.AppUser;
import com.armemius.labwork.data.repository.AppUserRepository;
import com.armemius.labwork.exceptions.auth.UserNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserJwtContextArgumentResolver implements HandlerMethodArgumentResolver {

    private final AppUserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && (parameter.getParameterType().isAssignableFrom(
                org.springframework.security.core.userdetails.UserDetails.class)
                || parameter.getParameterType().isAssignableFrom(
                AppUser.class));
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (parameter.getParameterType().isInstance(principal)) {
            return principal;
        }
        if (parameter.getParameterType().equals(AppUser.class) && principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<AppUser> user = userRepository.findFirstByUsername(username);
            if (user.isPresent()) {
                return user.get();
            }
        }

        throw new UserNotFoundException("Cannot get user from JWT token");
    }

}
