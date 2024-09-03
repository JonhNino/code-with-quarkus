package com.transfer.model;

import java.io.Serializable;


public record Transferencia(
        String tipoOperacion,
        String codigoTransaccion,
        String cuentaContable,
        String descripcion1,
        String descripcion2,
        String descripcion3,
        String canal,
        String moneda,
        String tipoCuentaOrigen,
        String tipoCuentaDestino,
        String codigoUnicoTransaccion
) implements Serializable {

    public Transferencia {
    }
}
