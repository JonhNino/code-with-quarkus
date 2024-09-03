package com.transfer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.config.ConfigurationBuilder;
import org.infinispan.protostream.domain.MarshallerProvider;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.ProtoSchemaBuilder;
import java.io.IOException;

@ApplicationScoped
public class InfinispanConfig {

    @Inject
    RemoteCacheManager cacheManager;

    @Inject
    TransferenciaMarshaller transferenciaMarshaller;

    @PostConstruct
    public void setup() {
        SerializationContext ctx = cacheManager.getSerializationContext();

        // Registrar el archivo Protobuf
        try {
            ctx.registerProtoFiles(FileDescriptorSource.fromResources("transferencia.proto"));
        } catch (IOException e) {
            throw new RuntimeException("Error registrando el archivo Protobuf", e);
        }

        // Registrar el marshaller
        ctx.registerMarshaller(transferenciaMarshaller);
    }
}
