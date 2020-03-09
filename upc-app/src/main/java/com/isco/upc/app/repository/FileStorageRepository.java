package com.isco.upc.app.repository;

import java.io.InputStream;
import java.util.List;

import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.data.mongodb.gridfs.GridFsResource;

public interface FileStorageRepository {

	 public String store(InputStream inputStream, String fileName, String contentType, DBObject metaData);
			 
	/*public GridFSDBFile retrive(String fileName);

	public GridFSDBFile getById(String id);

	public GridFSDBFile getByFilename(String filename);

	public List findAll();*/
	
	public void delete(String id);


	public GridFSFile retrive(String fileName);

	public GridFsResource getById(String id);

	public GridFSFile getByFilename(String filename);

	public GridFSFindIterable findAll();
}
