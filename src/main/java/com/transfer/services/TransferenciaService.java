package com.transfer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transfer.model.Transferencia;
import com.transfer.services.impl.LoaderCacheService;
import io.quarkus.infinispan.client.Remote;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.infinispan.client.hotrod.RemoteCache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static io.quarkus.arc.ComponentsProvider.LOG;

@ApplicationScoped
@RegisterForReflection(serialization = true, targets = {ArrayList.class})
public class TransferenciaService implements LoaderCacheService {

    @Inject
    @Remote("transferencias")
    RemoteCache<String, String> cache; // Cache ahora almacena Strings

    @Inject
    ObjectMapper objectMapper; // Inyección de Jackson ObjectMapper

    private final List<String> validCanales = Arrays.asList("TuBancoPersonas", "TuBancoEmpresas", "TuBancoApp", "TuBancoAppEmpresas");
    private final List<String> validMonedas = Arrays.asList("DOP", "USD", "EUR");
    private final List<String> validTiposCuenta = Arrays.asList("SV", "DD", "GL");

    @ConfigProperty(name = "cache.transferencias.ttl")
    Long ttl;

    @ConfigProperty(name = "cache.transferencias.max-idle")
    Long maxIdle;

    @Override
    public void guardarTransferencia(Transferencia transferencia) {
        try {
            validarTransferencia(transferencia);

            String transferenciaJson = objectMapper.writeValueAsString(transferencia);

            String key = transferencia.codigoUnicoTransaccion();
            cache.put(key, transferenciaJson, ttl, TimeUnit.MINUTES, maxIdle, TimeUnit.MINUTES);
        } catch (JsonProcessingException ex) {
            LOG.errorf("Error al convertir la transferencia a JSON: %s", ex);
            throw new RuntimeException("Error al convertir la transferencia a JSON", ex);
        } catch (HotRodClientException ex) {
            LOG.errorf("Error guardando la transferencia en Data Grid: %s", ex);
            throw new RuntimeException("Error al guardar la transferencia", ex);
        }
    }

    private void validarCampoObligatorio(String valor, String mensajeError) {
        if (valor == null || valor.isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private void validarTransferencia(Transferencia transferencia) {
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

    private void validarOperacionLBTR(Transferencia transferencia) {
        validarCampoObligatorio(transferencia.cuentaContable(), "La Cuenta Contable es obligatoria para operaciones LBTR.");
        validarCampoObligatorio(transferencia.descripcion1(), "Descripción 1 es obligatoria para operaciones LBTR.");
        validarCampoObligatorio(transferencia.codigoUnicoTransaccion(), "Código único de transacción es obligatorio y debe tener 11 posiciones.");
    }

    private void validarCobroImpuesto(Transferencia transferencia) {
        // Validación específica para Cobro de Impuesto 0.15%
        validarCampoObligatorio(transferencia.cuentaContable(), "La Cuenta Contable es obligatoria para Cobro de Impuesto 0.15%.");
        validarCampoObligatorio(transferencia.descripcion1(), "Descripción 1 es obligatoria para Cobro de Impuesto 0.15%.");
        validarCampoObligatorio(transferencia.codigoUnicoTransaccion(), "Código único de transacción es obligatorio.");

        if (transferencia.descripcion3() == null || !transferencia.descripcion3().contains(transferencia.canal())) {
            throw new IllegalArgumentException("Descripción 3 debe contener el nombre del canal para Cobro de Impuesto.");
        }
    }

    private void validarCobroComision(Transferencia transferencia) {
        // Validación específica para Cobro de la Comisión
        validarCampoObligatorio(transferencia.cuentaContable(), "La Cuenta Contable es obligatoria para Cobro de la Comisión.");
        validarCampoObligatorio(transferencia.descripcion1(), "Descripción 1 es obligatoria para Cobro de la Comisión.");
        validarCampoObligatorio(transferencia.codigoUnicoTransaccion(), "Código único de transacción es obligatorio.");
    }

    private void validarLBTRPuestoBolsa(Transferencia transferencia) {
        // Validación específica para LBTR Puesto de Bolsa
        validarCampoObligatorio(transferencia.cuentaContable(), "La Cuenta Contable es obligatoria para LBTR Puesto de Bolsa.");
        validarCampoObligatorio(transferencia.codigoUnicoTransaccion(), "Código único de transacción es obligatorio.");
    }


    public Transferencia obtenerTransferencia(String codigoUnicoTransaccion) {
        try {

            String transferenciaJson = cache.get(codigoUnicoTransaccion);
            if (transferenciaJson != null) {

                return objectMapper.readValue(transferenciaJson, Transferencia.class);
            }
            return null;
        } catch (JsonProcessingException ex) {
            LOG.errorf("Error al convertir JSON a Transferencia: %s", ex);
            throw new RuntimeException("Error al convertir JSON a Transferencia", ex);
        } catch (HotRodClientException ex) {
            LOG.errorf("Error al recuperar la transferencia desde Data Grid: %s", ex);
            throw new RuntimeException("Error al recuperar la transferencia", ex);
        }
    }

}
