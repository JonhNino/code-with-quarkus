package com.transfer.services;


import com.transfer.model.Transferencia;
import com.transfer.services.impl.TransferenciaValidationServiceInterface;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class TransferenciaValidationService implements TransferenciaValidationServiceInterface {

    private final List<String> validCanales = Arrays.asList("TuBancoPersonas", "TuBancoEmpresas", "TuBancoApp", "TuBancoAppEmpresas");
    private final List<String> validMonedas = Arrays.asList("DOP", "USD", "EUR");
    private final List<String> validTiposCuenta = Arrays.asList("SV", "DD", "GL");

    @Override
    public void validarTransferencia(Transferencia transferencia) {
        validarCampoObligatorio(transferencia.tipoOperacion(), "Tipo de Operación es obligatorio.");
        validarCampoObligatorio(transferencia.codigoTransaccion(), "Código de Transacción es obligatorio.");

        if (!validCanales.contains(transferencia.canal())) {
            throw new IllegalArgumentException("Canal inválido. Debe ser uno de: " + String.join(", ", validCanales));
        }

        if (!validMonedas.contains(transferencia.moneda())) {
            throw new IllegalArgumentException("Moneda inválida. Debe ser una de: " + String.join(", ", validMonedas));
        }

        if (!validTiposCuenta.contains(transferencia.tipoCuentaOrigen()) || !validTiposCuenta.contains(transferencia.tipoCuentaDestino())) {
            throw new IllegalArgumentException("Tipo de Cuenta inválido. Debe ser uno de: " + String.join(", ", validTiposCuenta));
        }

        switch (transferencia.tipoOperacion()) {
            case "LBTR propia":
            case "LBTR tercero":
            case "LBTR Regional":
                validarOperacionLBTR(transferencia);
                break;
            case "Cobro de impuesto 0.15%":
                validarCobroImpuesto(transferencia);
                break;
            case "Cobro de la comisión":
                validarCobroComision(transferencia);
                break;
            case "LBTR Puesto de Bolsa":
                validarLBTRPuestoBolsa(transferencia);
                break;
            default:
                throw new IllegalArgumentException("Tipo de Operación no soportado.");
        }
    }

    private void validarCampoObligatorio(String valor, String mensajeError) {
        if (valor == null || valor.isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private void validarOperacionLBTR(Transferencia transferencia) {
        validarCampoObligatorio(transferencia.cuentaContable(), "La Cuenta Contable es obligatoria para operaciones LBTR.");
        validarCampoObligatorio(transferencia.descripcion1(), "Descripción 1 es obligatoria para operaciones LBTR.");
        validarCampoObligatorio(transferencia.codigoUnicoTransaccion(), "Código único de transacción es obligatorio y debe tener 11 posiciones.");
    }

    private void validarCobroImpuesto(Transferencia transferencia) {
        validarCampoObligatorio(transferencia.cuentaContable(), "La Cuenta Contable es obligatoria para Cobro de Impuesto 0.15%.");
        validarCampoObligatorio(transferencia.descripcion1(), "Descripción 1 es obligatoria para Cobro de Impuesto 0.15%.");
        validarCampoObligatorio(transferencia.codigoUnicoTransaccion(), "Código único de transacción es obligatorio.");

        if (transferencia.descripcion3() == null || !transferencia.descripcion3().contains(transferencia.canal())) {
            throw new IllegalArgumentException("Descripción 3 debe contener el nombre del canal para Cobro de Impuesto.");
        }
    }

    private void validarCobroComision(Transferencia transferencia) {
        validarCampoObligatorio(transferencia.cuentaContable(), "La Cuenta Contable es obligatoria para Cobro de la Comisión.");
        validarCampoObligatorio(transferencia.descripcion1(), "Descripción 1 es obligatoria para Cobro de la Comisión.");
        validarCampoObligatorio(transferencia.codigoUnicoTransaccion(), "Código único de transacción es obligatorio.");
    }

    private void validarLBTRPuestoBolsa(Transferencia transferencia) {
        validarCampoObligatorio(transferencia.cuentaContable(), "La Cuenta Contable es obligatoria para LBTR Puesto de Bolsa.");
        validarCampoObligatorio(transferencia.codigoUnicoTransaccion(), "Código único de transacción es obligatorio.");
    }
}