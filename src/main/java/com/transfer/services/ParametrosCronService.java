package com.transfer.services;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ParametrosCronService {

    private static final Logger LOG = Logger.getLogger(ParametrosCronService.class);

    @Inject
    ParametroService parametroService;

    @Scheduled(cron = "30 * * * * ?")
    void ejecutarTareaProgramada() {
        LOG.info("Ejecutando tarea programada cada 2 minutos con 30 segundos");
        procesarParametros();
    }

    private void procesarParametros() {
        LOG.info("Iniciando procesamiento de parámetros desde el cron job...");
        try {
            parametroService.processParametros();
            LOG.info("Procesamiento de parámetros completado exitosamente desde el cron job.");
        } catch (Exception e) {
            LOG.error("Error durante el procesamiento de parámetros en el cron job", e);
        }
    }
}