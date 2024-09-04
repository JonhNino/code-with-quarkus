package com.transfer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transfer.model.Transferencia;
import com.transfer.services.impl.TransferenciaServiceInterface;
import io.quarkus.infinispan.client.Remote;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.infinispan.client.hotrod.RemoteCache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static io.quarkus.arc.ComponentsProvider.LOG;

@ApplicationScoped
@RegisterForReflection(serialization = true, targets = {ArrayList.class})
public class TransferenciaService implements TransferenciaServiceInterface {

    @Inject
    @Remote("TRANSFER_PARAMETERS")
    RemoteCache<String, String> cache;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    TransferenciaValidationService validationService;

    @ConfigProperty(name = "cache.transferencias.ttl")
    Long ttl;

    @ConfigProperty(name = "cache.transferencias.max-idle")
    Long maxIdle;

    @Override
    public void guardarTransferencia(Transferencia transferencia) {
        try {
            validationService.validarTransferencia(transferencia);

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

    @Override
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