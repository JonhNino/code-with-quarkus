package com.transfer.resources;

import com.transfer.model.Transferencia;
import com.transfer.services.TransferenciaService;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;

@Path("/transferencias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransferenciaController {

    @Inject
    TransferenciaService transferenciaService;

    @POST
    public Response guardarTransferencia(Transferencia transferencia) {
        try {
            transferenciaService.guardarTransferencia(transferencia);
            return Response.ok("Transferencia guardada exitosamente").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /*@GET
    @Path("/{codigoUnicoTransaccion}")
    public Response obtenerTransferencia(@PathParam("codigoUnicoTransaccion") String codigoUnicoTransaccion) {
        Transferencia transferencia = transferenciaService.obtenerTransferencia(codigoUnicoTransaccion);
        if (transferencia != null) {
            return Response.ok(transferencia).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Transferencia no encontrada").build();
        }
    }

     */
}
