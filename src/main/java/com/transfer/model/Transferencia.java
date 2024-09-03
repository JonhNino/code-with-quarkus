package com.transfer.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tipoOperacion",
        "codigoTransaccion",
        "cuentaContable",
        "descripcion1",
        "descripcion2",
        "descripcion3",
        "canal",
        "moneda",
        "tipoCuentaOrigen",
        "tipoCuentaDestino",
        "codigoUnicoTransaccion"
})
@RegisterForReflection
public class Transferencia implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tipoOperacion;
    private String codigoTransaccion;
    private String cuentaContable;
    private String descripcion1;
    private String descripcion2;
    private String descripcion3;
    private String canal;
    private String moneda;
    private String tipoCuentaOrigen;
    private String tipoCuentaDestino;
    private String codigoUnicoTransaccion;
}
