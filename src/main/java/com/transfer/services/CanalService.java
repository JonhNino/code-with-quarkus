package com.transfer.services;

import com.transfer.entities.Canales;
import com.transfer.repositories.CanalesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class CanalService {

    private static final Logger LOG = Logger.getLogger(CanalService.class);

    @Inject
    CanalesRepository canalesRepository;

    public void processCanales() {
        LOG.info("Iniciando procesamiento de todos los canales");
        List<Canales> canales = canalesRepository.listAll();
        LOG.info("Número total de registros encontrados: " + canales.size());



        LOG.info("Procesamiento de canales completado");
    }


}