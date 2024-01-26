package it.almaviva.difesa.cessazione.procedure.validation;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureOpeningDataDTO;
import it.almaviva.difesa.cessazione.procedure.validation.annotations.CheckDateValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Objects;

public class ProcedureDateValidator implements ConstraintValidator<CheckDateValid, ProcedureOpeningDataDTO> {

    @Override
    public boolean isValid(ProcedureOpeningDataDTO openingDataDTO, ConstraintValidatorContext context) {

        LocalDateTime dataDocRichiesta = openingDataDTO.getDataDocRichiesta();
        if (Objects.nonNull(dataDocRichiesta) && checkDateAfterToday(dataDocRichiesta)) {
            context.buildConstraintViolationWithTemplate(ErrorsConst.DATE_GREATER_ERROR)
                    .addPropertyNode(Constant.DATA_DOCRICHIESTA).addConstraintViolation();
            return false;
        }
        LocalDateTime dataPresDocRich = openingDataDTO.getDataPresDocRich();
        if (Objects.nonNull(dataPresDocRich)) {
            if (checkDateAfterToday(dataPresDocRich)) {
                context.buildConstraintViolationWithTemplate(ErrorsConst.DATE_GREATER_ERROR)
                        .addPropertyNode(Constant.DATA_PRESDOCRICH).addConstraintViolation();
                return false;
            } else if (Objects.nonNull(dataDocRichiesta) && checkDataPresDocRich(dataDocRichiesta, dataPresDocRich)) {
                context.buildConstraintViolationWithTemplate(ErrorsConst.DATE_PRESDOCRICH_ERROR)
                        .addPropertyNode(Constant.DATA_PRESDOCRICH).addConstraintViolation();
                return false;
            }
        }
        LocalDateTime dataProtIstanza = openingDataDTO.getDataProtIstanza();
        if (Objects.nonNull(dataProtIstanza)) {
            if (checkDateAfterToday(dataProtIstanza)) {
                context.buildConstraintViolationWithTemplate(ErrorsConst.DATE_GREATER_ERROR)
                        .addPropertyNode(Constant.DATA_PROTISTANZA).addConstraintViolation();
                return false;
            } else if (Objects.nonNull(dataPresDocRich) && checkDataProtIstanza(dataPresDocRich, dataProtIstanza)) {
                context.buildConstraintViolationWithTemplate(ErrorsConst.DATE_DATAPROTISTANZA_ERROR)
                        .addPropertyNode(Constant.DATA_PROTISTANZA).addConstraintViolation();
                return false;
            }
        }
        LocalDateTime dataProtIstanzaPec = openingDataDTO.getDataProtIstanzaPec();
        if (Objects.nonNull(dataProtIstanzaPec)) {
            if (checkDateAfterToday(dataProtIstanzaPec)) {
                context.buildConstraintViolationWithTemplate(ErrorsConst.DATE_GREATER_ERROR)
                        .addPropertyNode(Constant.DATA_PROTISTANZA_PEC).addConstraintViolation();
                return false;
            } else if (Objects.nonNull(dataProtIstanza) && checkDataProtIstanzaPec(dataProtIstanza, dataProtIstanzaPec)) {
                context.buildConstraintViolationWithTemplate(ErrorsConst.DATE_DATAPROTISTANZA_PEC_ERROR)
                        .addPropertyNode(Constant.DATA_PROTISTANZA_PEC).addConstraintViolation();
                return false;
            }
        }
        LocalDateTime dataDecorrenza = openingDataDTO.getDataDecorrenza();
        if (Objects.nonNull(dataDecorrenza) && checkDateBeforeToday(dataDecorrenza)) {
            context.buildConstraintViolationWithTemplate(ErrorsConst.DATE_LOWER_ERROR)
                    .addPropertyNode(Constant.DATA_DECORRENZA).addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean checkDateAfterToday(LocalDateTime localDateTime) {
        return !ChronoLocalDate.from(localDateTime).equals(ChronoLocalDate.from(LocalDateTime.now()))
                && !ChronoLocalDate.from(localDateTime).isBefore(ChronoLocalDate.from(LocalDateTime.now()));
    }

    private boolean checkDateBeforeToday(LocalDateTime localDateTime) {
        return !ChronoLocalDate.from(localDateTime).equals(ChronoLocalDate.from(LocalDateTime.now()))
                && !ChronoLocalDate.from(localDateTime).isAfter(ChronoLocalDate.from(LocalDateTime.now()));
    }

    private boolean checkDataPresDocRich(LocalDateTime dataDocRichiesta, LocalDateTime dataPresDocRich) {
        return !ChronoLocalDate.from(dataPresDocRich).equals(ChronoLocalDate.from(dataDocRichiesta))
                && !ChronoLocalDate.from(dataPresDocRich).isAfter(ChronoLocalDate.from(dataDocRichiesta));
    }

    private boolean checkDataProtIstanza(LocalDateTime dataPresDocRich, LocalDateTime dataProtIstanza) {
        return !ChronoLocalDate.from(dataProtIstanza).equals(ChronoLocalDate.from(dataPresDocRich))
                && !ChronoLocalDate.from(dataProtIstanza).isAfter(ChronoLocalDate.from(dataPresDocRich));
    }
    private boolean checkDataProtIstanzaPec(LocalDateTime dataProtIstanza, LocalDateTime dataProtIstanzaPec) {
        return !ChronoLocalDate.from(dataProtIstanzaPec).equals(ChronoLocalDate.from(dataProtIstanza))
                && !ChronoLocalDate.from(dataProtIstanzaPec).isAfter(ChronoLocalDate.from(dataProtIstanza));
    }

}
