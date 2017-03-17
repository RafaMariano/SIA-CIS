package br.inpe.filesystem;


import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.inpe.log.Verify;
import br.inpe.model.Image;
import br.inpe.model.ImagesCollection;
import br.inpe.service.ImageServiceImpl;
import nom.tam.fits.FitsException;

public class Main {
	
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
			
			ArrayList<String> imagesPathList = controller.getImages();

			for (String pathImage: imagesPathList){
			
				try{
					
					Image image = new Image(pathImage);
					String newPath = controller.getNewPath(pathImage);
					image.setKeyValue("FILESYSTEM", newPath);
					ImagesCollection ima = new ImagesCollection();
					ima.setDocument(image.getDocument());
				
					imagesService.saveImage(ima);
				}
				catch(FitsException  | ParseException | IOException fi){
					
					verify.moveToCorrupted(pathImage, controller.getPathPrincipal());				
				}
				
			}	
		}
	
	
	
	
	
		
	
	
//			StringBuilder destinatioPath = new StringBuilder("/home/inpe/a/b/c/d");
		//	destinatioPath.setLength(destinatioPath.lastIndexOf("/"));
//			System.out.println(destinatioPath.substring(destinatioPath.lastIndexOf("/")));
//			ArrayList<String> imagesFits =	controller.getImages();
//		int cont = 0;
//		for(String image: imagesFits){
//			Fits fits = new Fits(image);
//			fits.getHDU(0).getHeader().addValue("SIZE", 1, "");
//			fits.write(new File("/home/inpe/Database/"+Integer.toString(cont)));
//			cont++;
//	//	System.out.println(fits.getHDU(0).getHeader().getIntValue("SIZE"));
//		}
		//String[] a = null;
		//Path path = Paths.get("/home/inpe/a.txt");
	//	List<String>lines = Files.readAllLines(path);
		//Files.write(path, );
		
	}

