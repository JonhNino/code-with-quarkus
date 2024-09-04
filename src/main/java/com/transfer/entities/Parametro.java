package com.transfer.entities;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.time.Instant;

@MongoEntity(collection="parameters")
public class Parametro extends PanacheMongoEntity {
    public String tipoOperacion;
    public String codigoTransaccion;
    public String cuentaContable;
    public String descripcion1;
    public String descripcion2;
    public String descripcion3;
    public String canal;
    public String moneda;
    public String tipoCuentaOrigen;
    public String tipoCuentaDestino;
    public ParametrosAdicionales parametrosAdicionales;
    public Instant fechaCreacion;
    public Instant fechaActualizacion;

    public static class ParametrosAdicionales {
        public String SUCURSAL_ID;
        public String CAJERO_ID;
        public String OVERRIDE_CODE;
    }
}