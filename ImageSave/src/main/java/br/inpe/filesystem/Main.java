package br.inpe.filesystem;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import nom.tam.fits.FitsException;

public class Main {
	
	public static void main(String[] args) throws IOException, ParseException, FitsException {
		
		try{
			ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
			Controller controller = (Controller) ctx.getBean("controller");
			
			ArrayList<String> imagesList = controller.getImages();
			
			ImagesServiceImpl imagesService = (ImageServiceImpl) ctx.getBean("imagesService");
			//falta a anotacao @service na classe ImagesServiceImpl
			//corrigir nome dos metodos se tiver errado

			for (String pathImage: imagesList){
				
				String newPath = controller.getNewPath(pathImage);
				Image image = new Image(newPath);
				ImageCollection ima = new ImageCollection();
				ima.setDocument(image.getDocument());
				imagesService.save(ima);
				
			}	
		}
		catch(IOException io){
			
		}

	}

}