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

    @Inject
    CanalService canalService;

    @Inject
    CommissionCodesService commissionCodesService;

    @Scheduled(cron = "30 * * * * ?")
    void ejecutarTareaProgramada() {
        LOG.info("Ejecutando tarea programada cada 2 minutos con 30 segundos");
        procesarParametros();
        procesarCanales();
        procesarCommissionCodes();
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

    private void procesarCanales() {
        LOG.info("Iniciando procesamiento de canales desde el cron job...");
        try {
            canalService.processCanales();
            LOG.info("Procesamiento de canales completado exitosamente desde el cron job.");
        } catch (Exception e) {
            LOG.error("Error durante el procesamiento de canales en el cron job", e);
        }
    }

    private void procesarCommissionCodes() {
        LOG.info("Iniciando procesamiento de CommissionCodes desde el cron job...");
        try {
            commissionCodesService.processCommissionCodes();
            LOG.info("Procesamiento de CommissionCodes completado exitosamente desde el cron job.");
        } catch (Exception e) {
            LOG.error("Error durante el procesamiento de canales en el cron job", e);
        }
    }
}