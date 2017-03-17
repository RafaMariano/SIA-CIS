package br.inpe.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.inpe.log.FileSystemResult;
import br.inpe.log.Log;

public class FileSystem {
	private static FileSystem fileSystem;

	private FileSystem(){}

	public static synchronized FileSystem getInstance() throws IOException {
		
		if (fileSystem == null){
			fileSystem = new FileSystem();
		}
		return fileSystem;
	}
	
	public String createDir(String imagePath, String destinationPath, String principalPath) throws IOException {
		
		if (imagePath.substring(0, principalPath.length()).equals(principalPath)){
			StringBuilder newPathImage = new StringBuilder(imagePath);
			newPathImage.replace(0, principalPath.length(), destinationPath);
			
			//arrumar bug
			Files.createDirectories(Paths.get(newPathImage.substring(0,newPathImage.lastIndexOf("/"))));
			
			return newPathImage.toString();
		}
		else{
			return null;
		}
	}

	public void moveFile(String pathImage, String pathDestination) throws IOException, DirectoryNotEmptyException{
		//fazer um teste forcando um erro. O erro que dispara no IOExcpetion é o mesmo do DirectoryNotEmpty?
		//É a mesma mensagem?

			Files.move(Paths.get(pathImage), Paths.get(pathDestination));
		
	}

	public String deletePath(String source, final String pathPrincipal) throws IOException {
			
		if (source.equals(pathPrincipal) == false) {
			
			File file = new File(source);
			if (file.list().length <= 0) {
				if (file.delete()) {
					// log
					return deletePath(file.getParent(), pathPrincipal);
				}
			}
		}
		return source;
	}
//	
//	public List<String> getLog() throws IOException{
//		return this.log.getLog();
//	}

}
