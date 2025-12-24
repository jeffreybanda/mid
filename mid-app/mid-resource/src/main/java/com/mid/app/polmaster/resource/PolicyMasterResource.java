package com.mid.app.polmaster.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mid.app.common.model.HttpCode;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PolicyMasterResource {

	@POST
	public Response add() {

		return Response.status(HttpCode.CREATED.getCode()).build();

	}

}
