package br.inpe.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import br.inpe.log.Log;
import br.inpe.model.ImagesCollection;
import br.inpe.repository.ImageRepository;


@Service("imageService")
public class ImageServiceImpl implements ImageService{
	
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private Log log;
	
	public void setLog(Log log){
		this.log = log;
	}
	
	public void setImageRepository(ImageRepository imageRepository){
		this.imageRepository = imageRepository;
	}

	@Override
	public void saveImage(ImagesCollection image) {
		
		this.imageRepository.insert(image);
		try {
			//nao deleto, simplesmente zero o arquivo
			this.log.deleteLog();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	

}
