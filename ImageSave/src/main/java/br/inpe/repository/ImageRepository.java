package br.inpe.repository;

import java.io.InputStream;

import org.springframework.data.repository.Repository;

import com.mongodb.DBObject;

import br.inpe.model.ImagesCollection;

public interface ImageRepository extends Repository<ImagesCollection, Long>{
	
	public ImagesCollection insert(ImagesCollection entity);
	
}
