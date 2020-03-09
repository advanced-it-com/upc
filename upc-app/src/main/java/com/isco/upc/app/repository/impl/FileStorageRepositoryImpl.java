package com.isco.upc.app.repository.impl;

import com.isco.upc.app.repository.FileStorageRepository;

import java.io.InputStream;
import java.util.List;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

@Repository
public class FileStorageRepositoryImpl implements FileStorageRepository{


	 @Autowired
	 GridFsTemplate gridFsTemplate;

	 @Override
	 public String store(InputStream inputStream, String fileName,
	   String contentType, DBObject metaData) {
	  return this.gridFsTemplate
	    .store(inputStream, fileName, contentType, metaData).get()
	    .toString();
	 }

	@Override
	public GridFsResource getById(String id) {
		GridFSFile gridFsFile = this.gridFsTemplate.findOne(new Query(Criteria.where("_id").is(
				id)));
		return this.gridFsTemplate.getResource(gridFsFile);
	}

	@Override
	public GridFSFile getByFilename(String fileName) {
		return gridFsTemplate.findOne(new Query(Criteria.where("filename").is(
				fileName)));
	}

	@Override
	public GridFSFile retrive(String fileName) {
		return gridFsTemplate.findOne(
				new Query(Criteria.where("filename").is(fileName)));
	}

	@Override
	public GridFSFindIterable findAll() {
		return gridFsTemplate.find(null);
	}

	@Override
	public void delete(String id) {
		  this.gridFsTemplate.delete(new Query(Criteria.where("_id").is(
				    id)));
				 
		
	}
}
