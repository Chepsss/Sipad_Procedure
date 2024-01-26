package it.almaviva.difesa.cessazione.procedure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static final List<String> TIPI_PROCEDIMENTO = Arrays.asList("CszLimitiEtà", "CszInfermità", "CszAusiliaria", "CszRiserva");
    public static final List<Short> GERARCHIE = Arrays.asList((short) 520, (short) 140, (short) 230, (short) 350);

    /**
     * Mask the fiscal code for the FE
     */
    public static String maskFiscalCode(String fiscalCode) {
        return fiscalCode.replaceAll("\\b([a-zA-Z]{4})\\w+([a-zA-Z0-9]{4})", "$1*******$2");
    }

    public static <T> Page<T> getPageFromList(List<T> list, Pageable pageable) {
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    public static <T> List<T> sortList(List<T> listToSort, Sort.Direction direction, Comparator<T> comparator) {
        if (direction.isAscending()) {
            return listToSort.parallelStream().sorted(comparator).collect(Collectors.toList());
        } else if (direction.isDescending()) {
            return listToSort.parallelStream().sorted(comparator.reversed()).collect(Collectors.toList());
        } else {
            return listToSort;
        }
    }

    public static String objectInJsonString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper.writeValueAsString(object);
    }

    public static String replaceWrongTags(String fileContent) {
        return fileContent.replaceAll("<br>", "<br/>")
                .replaceAll("<BR>", "<br/>")
                .replaceAll("</br>", "<br/>")
                .replaceAll("</BR>", "<br/>");
    }

    //cr 16
    public static void confrontaStringhe(String... controllo) {
        ArrayList<String> arr = new ArrayList<String>();
        //vericare che la size di controllo sia maggiore di un valore
        if (controllo.length > 1) {
            for (int i = 0; i < controllo.length; i++) {
                if (controllo[i] != null)
                    arr.add(controllo[i]);
            }
        }
        for (int i = 0; i < arr.size(); i++) {
            String variabile = arr.get(i);
            log.info("Ente/Organo Amministrativo " + variabile);
            for (int j = 0; j < arr.size(); j++) {
                ArrayList<String> arr_i = new ArrayList<>();
                if (i != j) {
                    {
                        if (variabile.equals(arr.get(j))) {
                            log.info("Ente/Organo Amministrativo " + variabile + " uguale al valore " + arr.get(j));
                        } else {
                            arr_i.add(arr.get(j));
                            log.info("Ente/Organo Amministrativo " + variabile + " diverso dal valore " + arr.get(j));
                        }
                    }
                }

            }

            // fine cr16


        }
    }
}
