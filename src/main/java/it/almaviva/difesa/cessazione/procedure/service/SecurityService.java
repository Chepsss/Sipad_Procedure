package it.almaviva.difesa.cessazione.procedure.service;

import it.almaviva.difesa.cessazione.procedure.converter.CustomUserDetailConverter;
import it.almaviva.difesa.cessazione.procedure.model.common.CustomUserDetailDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SecurityService {

    private final CustomUserDetailConverter userDetailConverter;

    public SecurityService(CustomUserDetailConverter userDetailConverter) {
        this.userDetailConverter = userDetailConverter;
    }

    public CustomUserDetailDTO getUserDetails() {
        return userDetailConverter.convert(SecurityContextHolder.getContext().getAuthentication());
    }

    public String getUserToken() {
        CustomUserDetailDTO customerUserDetail = getUserDetails();
        return customerUserDetail.getToken();
    }

    public Long getEmployeeIdOfUserLogged() {
        CustomUserDetailDTO customerUserDetail = getUserDetails();
        return customerUserDetail.getEmployeeId();
    }

    public Set<String> getUserRoles() {
        CustomUserDetailDTO user = getUserDetails();
        return user.getAuthorities();
    }

}
