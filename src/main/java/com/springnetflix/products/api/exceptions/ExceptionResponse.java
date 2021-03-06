package com.springnetflix.products.api.exceptions;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse implements Serializable {

    private Date timestamp;
    private String message;
    private String description;

}
