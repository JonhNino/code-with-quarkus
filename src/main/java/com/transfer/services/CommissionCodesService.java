package com.transfer.services;

import com.transfer.entities.Canales;
import com.transfer.entities.CommissionCodes;
import com.transfer.repositories.CanalesRepository;
import com.transfer.repositories.CommissionCodesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class CommissionCodesService {

    private static final Logger LOG = Logger.getLogger(CommissionCodesService.class);

    @Inject
    CommissionCodesRepository commissionCodesRepository;

    public void processCommissionCodes() {
        LOG.info("Iniciando procesamiento de todos los CommissionCodes");
        List<CommissionCodes> canales = commissionCodesRepository.listAll();
        LOG.info("Número total de registros encontrados: " + canales.size());



        LOG.info("Procesamiento de CommissionCodes completado");
    }


}