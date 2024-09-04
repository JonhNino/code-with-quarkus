/*import com.transfer.model.Transferencia;
import com.transfer.resources.TransferenciaController;
import com.transfer.services.TransferenciaService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TransferenciaControllerTest {

    @Inject
    TransferenciaService transferenciaService;

    @Inject
    TransferenciaController transferenciaController;

    private Transferencia transferencia;

    @BeforeEach
    void setup() {
        transferencia = new Transferencia(
                "tipoOperacion",
                "codigoTransaccion",
                "cuentaContable",
                "descripcion1",
                "descripcion2",
                "descripcion3",
                "canal",
                "moneda",
                "tipoCuentaOrigen",
                "tipoCuentaDestino",
                "codigoUnicoTransaccion"
        );
    }

    @Test
    void testGuardarTransferencia() {
        Response response = transferenciaController.guardarTransferencia(transferencia);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Transferencia guardada exitosamente", response.getEntity());
    }

    @Test
    void testGuardarTransferenciaInvalid() {
        transferencia = new Transferencia(
                "tipoOperacion",
                "codigoTransaccion",
                "cuentaContable",
                "descripcion1",
                "descripcion2",
                "descripcion3",
                "canal",
                "moneda",
                "tipoCuentaOrigen",
                "tipoCuentaDestino",
                null // Invalid data
        );

        Response response = transferenciaController.guardarTransferencia(transferencia);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Datos inválidos", response.getEntity());
    }

    @Test
     void testObtenerTransferencia() {
        Response response = transferenciaController.obtenerTransferencia("codigoUnicoTransaccion");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    void testObtenerTransferenciaNoEncontrada() {
        Response response = transferenciaController.obtenerTransferencia("codigoInvalido");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Transferencia no encontrada", response.getEntity());
    }
}

 */