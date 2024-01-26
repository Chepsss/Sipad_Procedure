package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiassegnazioneRequest implements Serializable {

    private static final long serialVersionUID = -8799280883405540999L;

    private Set<Long> procedureIds;
    private Boolean selectedAll;
    @NotNull
    private Long newEmployeeId;
    @NotNull
    private Long oldEmployeeId;
    @NotNull
    private String roleCode;

}
