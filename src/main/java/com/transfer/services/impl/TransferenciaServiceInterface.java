package com.transfer.services.impl;


import com.transfer.model.Transferencia;

public interface TransferenciaServiceInterface  {

    void guardarTransferencia(Transferencia key);

    Transferencia obtenerTransferencia(String codigoUnicoTransaccion);
}
