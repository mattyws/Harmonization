package br.ufmt.harmonizacao.custom;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommonDescriptorsSet {
	
	private List<String> commonDescriptors = new ArrayList<String>();
	private BufferedReader reader;

	public CommonDescriptorsSet(String caminho) {
		try {
			reader = new BufferedReader(new FileReader(caminho));
			this.readFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void readFile() throws IOException {
		String linha;
		while(reader.ready()) {
			linha = reader.readLine();
			commonDescriptors.add(linha);
		}
		reader.close();
	}
	
	public boolean contains(String descriptor) {
		return commonDescriptors.contains(descriptor);
	}


}
