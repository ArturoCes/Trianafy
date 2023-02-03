package com.salesianostriana.dam.trianafy.error.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.dam.trianafy.error.ApiError;
import com.salesianostriana.dam.trianafy.error.ApiSubError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ApiErrorImpl implements ApiError {

    private HttpStatus status;

    private String message;

    private String path;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ApiSubError>subErrorsL;

    @Override
    public int getStatusCode() {
        return status != null ? status.value() : 0;
    }

    @Override
    public List<ApiSubError> getSubErrors() {
        return null;
    }
}
