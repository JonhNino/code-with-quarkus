package com.transfer.repositories;

import com.transfer.entities.Canales;
import com.transfer.entities.CommissionCodes;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommissionCodesRepository implements PanacheMongoRepository<CommissionCodes> {
    public CommissionCodes findByTipoOperacion(String canal) {
        return find("CommissionCodes", canal).firstResult();
    }
}
