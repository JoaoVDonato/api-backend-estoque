package com.br.bank.api_bank.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumber {

    private String phoneNumber;

    // Esse regex representa os caracteres "(", ")" e "-"
    private static final String PHONE_CHARACTERS = "[()\\-]";

    private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");


    public static PhoneNumber of(String value) {

        if (value == null) {
            log.error("O número de telefone não pode ser nulo");
            throw new IllegalArgumentException("O número de telefone não pode ser nulo");
        } else if (value.isBlank()) {
            log.error("O número de telefone não pode ficar em branco");
            throw new IllegalArgumentException("O número de telefone não pode ficar em branco");
        }

        String phoneNumbers = value.replaceAll(PHONE_CHARACTERS, "").trim();

        if (!isNumeric(phoneNumbers) || phoneNumbers.length() != 11) {
            log.error("O número de telefone ({}) deve conter apenas 11 caracteres numéricos.", phoneNumbers);
            throw new IllegalArgumentException("O número de telefone (" + phoneNumbers + ") deve conter apenas 11 caracteres numéricos.");
        }
        return new PhoneNumber(formatPhoneNumber(phoneNumbers));

    }

    public static boolean isNumeric(String strNum) {
        return pattern.matcher(strNum).matches();
    }

    public static String formatPhoneNumber(String phoneNumber) {
        return "(" + phoneNumber.substring(0, 2) + ")"
                + phoneNumber.substring(2, 7) + "-"
                + phoneNumber.substring(7);
    }

    public String getNumero() {
        return phoneNumber.replaceAll(PHONE_CHARACTERS, "");
    }
}
