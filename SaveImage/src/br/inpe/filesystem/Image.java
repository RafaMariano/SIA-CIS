package br.inpe.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bson.Document;

import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.Header;
import nom.tam.fits.HeaderCard;
import nom.tam.util.Cursor;

public class Image {

	private Fits fits;
	private Document collection;
	private StringBuilder sb;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
	private SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
	private Calendar calendar = Calendar.getInstance();

	public Image(String image, int length, String newPath) throws ParseException {

		try{
			this.sb = new StringBuilder(image);
			this.fits = new Fits(image);
			this.collection = setDocument(new Document());
			this.collection.put("FileSystem", copyFile( image, createDir(this.sb, length, new StringBuilder(newPath)) ));
			deletePath(this.sb.substring(0, this.sb.lastIndexOf("/")), this.sb.substring(0, length-1));
			this.collection.put("_id", image.substring(image.lastIndexOf("/")+1));
			this.fits.close();

		}
		catch(StringIndexOutOfBoundsException a){
			System.out.println("Imagem em um caminho inválido:     " + image);
			//adicionar no log
			
		}catch (FitsException fits){
			System.out.println("Imagem fits com problema. Corrompido? É uma imagem Fits?   " + image);
			System.out.println(fits);
		}
		catch(IOException io){
			System.out.println("Error: "+ io);
			//adicionar no log
		}
	
	}

	private String createDir(StringBuilder path, int length, StringBuilder newPath) throws IOException{
		//Colocando o caminho da imagem sem o nome no /home/inpe/Database/
		newPath.append(path.substring(length, path.lastIndexOf("/")));
		
		//Criando os diretorios
		Files.createDirectories(Paths.get(newPath.toString()));
		
		//Adicionando o nome da imagem ao caminho
		newPath.append(path.substring(path.lastIndexOf("/")));

		return newPath.toString();
	}

	private String copyFile(String source, String destination) throws IOException {

		Files.move(Paths.get(source), Paths.get(destination));
		return destination;
	}
	
private String deletePath(String source, final String path) throws IOException{

		if(source.equals(path) == false){
			
			File file = new File(source);
			if (file.list().length <= 0){
				if (file.delete()){
					//log
					return deletePath (file.getParent(), path);
				}
			}
		}
		return source;
	}

	private Document setDocument(Document document) throws FitsException, IOException, ParseException{
		Header header = this.fits.getHDU(0).getHeader();

		Cursor<String, HeaderCard> c = header.iterator();

		while(c.hasNext() ){
			HeaderCard bb = c.next();

			if(bb.getKey().equals("END") == false){

				if (bb.getKey() != null){
					if (bb.getKey().isEmpty() == false){

						if (bb.getKey().equals("COMMENT")){
							document.put(bb.getKey(),bb.getComment());
						}
						else if(bb.getValue() != null) {
							document.put(bb.getKey(), bb.getValue());

						}
						else {
							document.put(bb.getKey(),"");

						}
					}
				}
			}

		}
		
		
		//essa parte será retirado em uma atualização futura, pois estou testando com imagens fits de duas keys diferentes
		if (document.containsKey("DATE-OBS")){
			
			calendar.setTime(dateFormat.parse(document.get("DATE-OBS").toString()));  
			document.append("DAY",calendar.get(Calendar.DAY_OF_MONTH));
			document.append("MONTH",calendar.get( Calendar.MONTH));
			document.append("YEAR", calendar.get(Calendar.YEAR));
			document.append("HOUR", calendar.get(Calendar.HOUR));
			document.append("MINUTE", calendar.get(Calendar.MINUTE));
			document.append("SECOND", calendar.get(Calendar.SECOND));
			document.append("MILLISECOND", calendar.get(Calendar.MILLISECOND));
			
		 
		
		}
		else if (document.containsKey("DATE") ){
			calendar.setTime(dateFormat2.parse(document.get("DATE").toString()));  
			document.append("DAY",calendar.get(Calendar.DAY_OF_MONTH));
			document.append("MONTH",calendar.get( Calendar.MONTH));
			document.append("YEAR", calendar.get(Calendar.YEAR));
			document.append("HOUR", calendar.get(Calendar.HOUR));
			document.append("MINUTE", calendar.get(Calendar.MINUTE));
			document.append("SECOND", calendar.get(Calendar.SECOND));
			document.append("MILLISECOND", calendar.get(Calendar.MILLISECOND));
		}
			
		
	
		return document;
	}

	public Document getDocument(){
		return this.collection;
	}

}