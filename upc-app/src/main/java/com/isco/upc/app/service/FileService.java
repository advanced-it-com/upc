package com.isco.upc.app.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

import com.isco.upc.app.repository.FileStorageRepository;
import com.isco.upc.app.repository.PersonRepository;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class FileService {

	@Autowired
	private FileStorageRepository fileStorageRepository;
	

	 public String store(InputStream inputStream, String fileName,
	   String contentType, DBObject metaData) {
	  return fileStorageRepository
	    .store(inputStream, fileName, contentType, metaData);
	 }

	
	 public GridFsResource getById(String id) {
	  return fileStorageRepository.getById(id);
	 }
	
	
}
