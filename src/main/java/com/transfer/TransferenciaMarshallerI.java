package com.transfer;

import com.transfer.model.Transferencia;
import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

public interface TransferenciaMarshallerI {
    Transferencia readFrom(MessageMarshaller.ProtoStreamReader reader) throws IOException;

    void writeTo(MessageMarshaller.ProtoStreamWriter writer, Transferencia transferencia) throws IOException, IOException;
}
