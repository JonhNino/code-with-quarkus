package com.transfer.services.impl;


import com.transfer.model.Transferencia;

public interface LoaderCacheService {

    void guardarTransferencia(Transferencia key);

    Transferencia obtenerTransferencia(String codigoUnicoTransaccion);
}
