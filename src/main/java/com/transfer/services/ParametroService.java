package com.transfer.services;

import com.transfer.entities.Parametro;
import com.transfer.model.Transferencia;
import com.transfer.repositories.ParametroRepository;
import com.transfer.services.impl.TransferenciaServiceInterface;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Arrays;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ParametroService {

    private static final Logger LOG = Logger.getLogger(ParametroService.class);

    @Inject
    ParametroRepository parametroRepository;

    @Inject
    TransferenciaServiceInterface transferenciaService;

    private static final List<String> CANALES_VALIDOS = Arrays.asList(
            "TuBancoPersonas", "TuBancoEmpresas", "TuBancoApp", "TuBancoAppEmpresas"
    );
    private static final List<String> MONEDAS_VALIDAS = Arrays.asList("DOP", "USD", "EUR");
    private static final List<String> TIPOS_CUENTA_ORIGEN = Arrays.asList("SV", "DD");
    private static final String TIPO_CUENTA_DESTINO = "GL";

    public void processParametros() {
        LOG.info("Iniciando procesamiento de todos los parámetros");
        List<Parametro> parametros = parametroRepository.listAll();
        LOG.info("Número total de registros encontrados: " + parametros.size());

        for (Parametro parametro : parametros) {
            try {
                processParametro(parametro);
            } catch (Exception e) {
                LOG.error(" Error procesando parámetro: " + parametro.tipoOperacion, e);
            }
        }
        LOG.info("Procesamiento de parámetros completado");
    }

    private void processParametro(Parametro parametro) {
        LOG.info("Procesando parámetro: " + parametro.tipoOperacion);

        if (!validarParametrosComunes(parametro)) {
            LOG.error("Parámetros inválidos para: " + parametro.tipoOperacion);
            return;
        }

        Transferencia transferencia = new Transferencia(
                parametro.tipoOperacion,
                parametro.codigoTransaccion,
                parametro.cuentaContable,
                parametro.descripcion1,
                parametro.descripcion2,
                parametro.descripcion3,
                parametro.canal,
                parametro.moneda,
                parametro.tipoCuentaOrigen,
                parametro.tipoCuentaDestino,
                generarCodigoUnicoTransaccion()
        );

        switch (parametro.tipoOperacion) {
            case "LBTR propia":
            case "LBTR tercero":
            case "LBTR Regional":
                processLBTR(transferencia);
                break;
            case "Cobro de impuesto 0.15%":
                processCobroImpuesto(transferencia);
                break;
            case "Cobro de la comisión":
                processCobroComision(transferencia);
                break;
            case "LBTR Puesto de Bolsa":
                processLBTRPuestoBolsa(transferencia);
                break;
            default:
                LOG.warn("Tipo de operación no reconocido: " + parametro.tipoOperacion);
                return;
        }

        guardarEnDataGrid(transferencia);
    }

    private boolean validarParametrosComunes(Parametro parametro) {
        return CANALES_VALIDOS.contains(parametro.canal) &&
                MONEDAS_VALIDAS.contains(parametro.moneda) &&
                TIPOS_CUENTA_ORIGEN.contains(parametro.tipoCuentaOrigen) &&
                TIPO_CUENTA_DESTINO.equals(parametro.tipoCuentaDestino);
    }
    private Transferencia processLBTR(Transferencia transferencia) {
        if (transferencia.descripcion1().startsWith("TRAN\\")) {
            String beneficiario = transferencia.descripcion1().substring(5);
            LOG.info("Procesando LBTR para beneficiario: " + beneficiario);
        }
        return transferencia;
    }

    private Transferencia processCobroImpuesto(Transferencia transferencia) {
        String nuevaDescripcion1 = String.format("Cobro Imp DGII 0.15%%_Trans %s %s",
                transferencia.canal(),
                transferencia.moneda());
        return new Transferencia(
                transferencia.tipoOperacion(),
                transferencia.codigoTransaccion(),
                transferencia.cuentaContable(),
                nuevaDescripcion1,
                transferencia.descripcion2(),
                transferencia.descripcion3(),
                transferencia.canal(),
                transferencia.moneda(),
                transferencia.tipoCuentaOrigen(),
                transferencia.tipoCuentaDestino(),
                transferencia.codigoUnicoTransaccion()
        );
    }

    private Transferencia processCobroComision(Transferencia transferencia) {
        String nuevaDescripcion1 = String.format("COMISION LBTR %s %s",
                transferencia.canal(),
                transferencia.moneda());
        String nuevaDescripcion2 = generarCodigoUnicoTransaccion();
        return new Transferencia(
                transferencia.tipoOperacion(),
                transferencia.codigoTransaccion(),
                transferencia.cuentaContable(),
                nuevaDescripcion1,
                nuevaDescripcion2,
                transferencia.descripcion3(),
                transferencia.canal(),
                transferencia.moneda(),
                transferencia.tipoCuentaOrigen(),
                transferencia.tipoCuentaDestino(),
                transferencia.codigoUnicoTransaccion()
        );
    }

    private Transferencia processLBTRPuestoBolsa(Transferencia transferencia) {
        return new Transferencia(
                transferencia.tipoOperacion(),
                transferencia.codigoTransaccion(),
                transferencia.cuentaContable(),
                "PUESTOBOLSA",
                transferencia.descripcion2(),
                transferencia.descripcion3(),
                transferencia.canal(),
                transferencia.moneda(),
                transferencia.tipoCuentaOrigen(),
                transferencia.tipoCuentaDestino(),
                transferencia.codigoUnicoTransaccion()
        );
    }

    private void guardarEnDataGrid(Transferencia transferencia) {
        try {
            transferenciaService.guardarTransferencia(transferencia);
            LOG.info("Transferencia guardada en Data Grid: "+ transferencia.codigoUnicoTransaccion());
        } catch (Exception e) {
            LOG.error("Error al guardar la transferencia en Data Grid", e);
        }
    }

    private String generarCodigoUnicoTransaccion() {
        return String.format("%011d", System.nanoTime() % 100000000000L);
    }
}