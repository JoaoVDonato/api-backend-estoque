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
public class Cpf {

    private String cpf;

    private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static Cpf of(String value) {

        if (value == null) {
            log.error("O cpf não pode ser nulo");
            throw new IllegalArgumentException("O cpf não pode ser nulo");
        } else if (value.isBlank()) {
            log.error("O cpf não pode ficar em branco");
            throw new IllegalArgumentException("O cpf não pode ficar em branco");
        }

        String cpfNumbers = value.replace("-", "").replace(".", "");

        if (!isNumeric(cpfNumbers) || cpfNumbers.length() != 11) {
            log.error("Cpf deve conter 11 caracteres numericos: {} ", cpfNumbers);
            throw new IllegalArgumentException("Cpf (" + cpfNumbers + ") deve conter apenas 11 caracteres numéricos.");
        }
        return new Cpf(cpfNumbers);

    }

    public static boolean isNumeric(String numero) {
        return pattern.matcher(numero).matches();

    }
}
