package com.isco.upc.app.resource;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.isco.upc.app.filter.JWTRequestFilter;
import com.isco.upc.app.service.FileService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

import io.swagger.annotations.Api;
import org.springframework.data.mongodb.gridfs.GridFsResource;

@Path("/files")
@Api(value = "/files")
public class FileResource {

	 private static final Logger LOGGER = LoggerFactory.getLogger(FileResource.class);
	 
	 
	@Autowired
	FileService fileService;
	

	   
	   
	
	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadImageFile(@FormDataParam("uploadFile") InputStream fileInputStream,
			@FormDataParam("uploadFile") FormDataContentDisposition fileFormDataContentDisposition, @FormDataParam("uploadFile") final FormDataBodyPart body) {
		    

		LOGGER.info("Upload file ...");
		// local variables
		String fileName = null;
		String uploadFilePath = null;

		String mimeType = body.getMediaType().toString();
		fileName = fileFormDataContentDisposition.getFileName();
		DBObject metaData = new BasicDBObject();
		metaData.put("brand", "Audi");
		metaData.put("model", "Audi A3");
		metaData.put("description",
				"Audi german automobile manufacturer that designs, engineers, and distributes automobiles");


		uploadFilePath = fileService.store(fileInputStream, fileName, mimeType, metaData);

		return Response.ok(uploadFilePath).build();
	}
	
	
	@GET
	@Path("download/{imageId}")
 @Produces(MediaType.APPLICATION_OCTET_STREAM)
	//  @Produces({"image/png", "image/jpg", "image/gif"})
	public Response downloadImageFile(@PathParam("imageId") String imageId) throws IOException {
	 
	   // set file (and path) to be download
	
	   GridFsResource file = fileService.getById(imageId);
	   if (file == null){
		   return Response.status(404).
		              entity("FILE NOT FOUND with id:" + imageId).
		              type("text/plain").
		              build();
	   }
	 
	   ResponseBuilder responseBuilder = Response.ok((Object) file.getInputStream());
	   responseBuilder.header("Content-Disposition", "attachment; filename=\""+ file.getFilename() +"\"");
	   return responseBuilder.build();
	}
}
