package com.transfer.repositories;

import com.transfer.entities.Canales;
import com.transfer.entities.Parametro;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CanalesRepository implements PanacheMongoRepository<Canales> {
    public Canales findByTipoOperacion(String canal) {
        return find("canal", canal).firstResult();
    }
}
