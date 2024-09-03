package com.transfer.resources;

import com.transfer.model.Transferencia;
import com.transfer.services.TransferenciaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/transferencias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Transferencia", description = "Operaciones relacionadas con transferencias")
public class TransferenciaController {

    @Inject
    TransferenciaService transferenciaService;

    @POST
    @Operation(summary = "Guardar una nueva transferencia",
            description = "Guarda una nueva transferencia en el sistema")
    @APIResponse(responseCode = "200",
            description = "Transferencia guardada exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @APIResponse(responseCode = "400",
            description = "Datos de transferencia inválidos",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    public Response guardarTransferencia(
            @RequestBody(description = "Datos de la transferencia a guardar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Transferencia.class)))
            Transferencia transferencia) {
        try {
            transferenciaService.guardarTransferencia(transferencia);
            return Response.ok("Transferencia guardada exitosamente").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{codigoUnicoTransaccion}")
    @Operation(summary = "Obtener una transferencia por su código único",
            description = "Recupera una transferencia basada en su código único de transacción")
    @APIResponse(responseCode = "200",
            description = "Transferencia encontrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Transferencia.class)))
    @APIResponse(responseCode = "404",
            description = "Transferencia no encontrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    public Response obtenerTransferencia(
            @Parameter(description = "Código único de la transacción",
                    required = true)
            @PathParam("codigoUnicoTransaccion") String codigoUnicoTransaccion) {
        Transferencia transferencia = transferenciaService.obtenerTransferencia(codigoUnicoTransaccion);
        if (transferencia != null) {
            return Response.ok(transferencia).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Transferencia no encontrada").build();
        }
    }
}