package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ConvertToPdfDTO implements Serializable {

    private static final long serialVersionUID = 7716787549208618625L;

    private String fileContent;

}
