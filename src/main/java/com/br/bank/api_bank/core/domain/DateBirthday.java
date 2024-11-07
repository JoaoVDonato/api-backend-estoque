package com.br.bank.api_bank.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateBirthday {

    private LocalDate date;

    public static DateBirthday convertDate(String date){

        try{
            return new DateBirthday(LocalDate.parse(date));
        }catch(DateTimeParseException e){
            throw new DateTimeParseException(
                    "Ocorreu um erro na conversão da data: " + date,
                    e.getParsedString(),
                    e.getErrorIndex());
        }
    }
}
