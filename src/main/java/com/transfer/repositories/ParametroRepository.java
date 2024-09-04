package com.transfer.repositories;

import com.transfer.entities.Parametro;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParametroRepository implements PanacheMongoRepository<Parametro> {
    public Parametro findByTipoOperacion(String tipoOperacion) {
        return find("tipoOperacion", tipoOperacion).firstResult();
    }
}



