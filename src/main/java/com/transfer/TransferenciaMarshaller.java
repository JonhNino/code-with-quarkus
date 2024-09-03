package com.transfer;

import com.transfer.model.Transferencia;
import jakarta.enterprise.context.ApplicationScoped;
import org.infinispan.protostream.BaseMarshaller;
import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

@ApplicationScoped
public class TransferenciaMarshaller implements BaseMarshaller<Transferencia>, TransferenciaMarshallerI {

    @Override
    public Transferencia readFrom(MessageMarshaller.ProtoStreamReader reader) throws IOException {
        return Transferencia.builder()
                .tipoOperacion(reader.readString("tipoOperacion"))
                .codigoTransaccion(reader.readString("codigoTransaccion"))
                .cuentaContable(reader.readString("cuentaContable"))
                .descripcion1(reader.readString("descripcion1"))
                .descripcion2(reader.readString("descripcion2"))
                .descripcion3(reader.readString("descripcion3"))
                .canal(reader.readString("canal"))
                .moneda(reader.readString("moneda"))
                .tipoCuentaOrigen(reader.readString("tipoCuentaOrigen"))
                .tipoCuentaDestino(reader.readString("tipoCuentaDestino"))
                .codigoUnicoTransaccion(reader.readString("codigoUnicoTransaccion"))
                .build();
    }

    @Override
    public void writeTo(MessageMarshaller.ProtoStreamWriter writer, Transferencia transferencia) throws IOException, IOException {
        writer.writeString("tipoOperacion", transferencia.getTipoOperacion());
        writer.writeString("codigoTransaccion", transferencia.getCodigoTransaccion());
        writer.writeString("cuentaContable", transferencia.getCuentaContable());
        writer.writeString("descripcion1", transferencia.getDescripcion1());
        writer.writeString("descripcion2", transferencia.getDescripcion2());
        writer.writeString("descripcion3", transferencia.getDescripcion3());
        writer.writeString("canal", transferencia.getCanal());
        writer.writeString("moneda", transferencia.getMoneda());
        writer.writeString("tipoCuentaOrigen", transferencia.getTipoCuentaOrigen());
        writer.writeString("tipoCuentaDestino", transferencia.getTipoCuentaDestino());
        writer.writeString("codigoUnicoTransaccion", transferencia.getCodigoUnicoTransaccion());
    }

    @Override
    public Class<? extends Transferencia> getJavaClass() {
        return Transferencia.class;
    }

    @Override
    public String getTypeName() {
        return "package-name.Transferencia";
    }
}
