package br.inpe.filesystem;

import java.io.IOException;
import java.text.ParseException;

import br.inpe.database.Query;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {
		
//		try{
//			FileSystem f = new FileSystem();
//			f.sendToBD(f.getImages());
//		}
//		catch(IOException io){
//
//		}
		
		Query.findOne1(1);
	}

}
