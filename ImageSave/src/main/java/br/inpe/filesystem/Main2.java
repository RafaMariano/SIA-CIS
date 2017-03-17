package br.inpe.filesystem;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import br.inpe.log.Verify;
import br.inpe.model.Image;
import br.inpe.model.ImagesCollection;
import br.inpe.service.FileStorageDAO;
import br.inpe.service.ImageServiceImpl;
import nom.tam.fits.FitsException;

public class Main2 {
	
	public static void main(String[] args) throws IOException, ParseException, FitsException {
	//	
						
				ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
				ImageServiceImpl imagesService = (ImageServiceImpl) ctx.getBean("imageService");
				
				Verify verify = (Verify) ctx.getBean("verify");
				ImagesCollection imageColl = verify.verify();
				
				
				if (imageColl != null){
					imagesService.saveImage(imageColl);
				}
				
				Controller controller = (Controller) ctx.getBean("controller");
				 FileStorageDAO fileStorageDao = (FileStorageDAO) ctx
					     .getBean("fileStorageDao");
				
				ArrayList<String> imagesPathList = controller.getImages();

				for (String pathImage: imagesPathList){
				
					try{
						
						Image image = new Image(pathImage);
						String newPath = controller.getNewPath(pathImage);
						image.setKeyValue("FILESYSTEM", newPath);

						 Resource resource = ctx.getResource("file:"+newPath);
						 
						 DBObject metaData = new BasicDBObject();
						   metaData.put("IMAGE",image.getDocument());
	 
						System.out.println(fileStorageDao.store(resource.getInputStream(),image.getDocument().
									 getString("_id"), "FITS", metaData));
						
						
					}
					catch(FitsException  | ParseException | IOException fi){
						
						verify.moveToCorrupted(pathImage, controller.getPathPrincipal());				
					}
					
				}	
			}
}
