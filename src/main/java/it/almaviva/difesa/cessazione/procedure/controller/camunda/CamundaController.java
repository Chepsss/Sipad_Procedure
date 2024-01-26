package it.almaviva.difesa.cessazione.procedure.controller.camunda;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.CustomUserDetail;
import it.almaviva.difesa.cessazione.procedure.service.msproc.CamundaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constant.CAMUNDA_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class CamundaController {

    private final CamundaService camundaService;

    @PostMapping("/removedCamundaInstances")
    @Secured({Constant.ADMIN_ROLE_ID})
    public void removeCamundaInstance(@RequestBody List<String> instances) {
        var userLogged = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.notNull(userLogged, "User is not authenticated");
        Assert.isTrue(userLogged.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equalsIgnoreCase(Constant.ADMIN_ROLE_ID)),
                "User is not Admin");
        if (!instances.isEmpty()) {
            instances.parallelStream().forEach(camundaService::deleteProcessInstance);
        }
    }

}
