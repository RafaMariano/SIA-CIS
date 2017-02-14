package br.inpe.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import br.inpe.database.Query;

@Path("/images")
public class ComputerProcessor {
	
	
	@GET
	@Path("{page: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCountryById(@PathParam("page") int id) throws JsonGenerationException, JsonMappingException, JsonParseException, IOException {
		 return Response.ok(Query.findOne(id)).build();
		//return Query.findOne(id).toString();
	}
	
	@GET
	@Path("/id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImageInf(@PathParam("id") String id) throws JsonGenerationException, JsonMappingException, JsonParseException, IOException {
//		return Query.findOne(id);
	return Response.ok(Query.findOne(id)).build();
	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@DefaultValue("0")
	 public Response echoInputList(@QueryParam("day") final int day, @QueryParam("month") final int month, @QueryParam("year") final int year) {
		System.out.println(day);
		System.out.println(month);
		System.out.println(year);
	        return Response.ok(day+month+year).build();
	    }

}
