package br.ufmt.harmonizacao.reader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Testando classe para leitura de arquivos csv
 * Código tirado de : 
 * http://www.guj.com.br/13812-lendo-e-manipulando-arquivos-csv-
 * 
 * @author mattyws
 *
 */
public class CSVReader {

	    private String sBody;
	    private String caminho;
	    private FileInputStream arquivo;
	    private BufferedReader linhaArquivo;
	    private BufferedWriter sFilePath;

	    /**
	     * Construtor default
	     */
	    public CSVReader() {

	    }

	    /**
	     * 
	     * @param caminho Caminho completo do arquivo
	     */
	    public CSVReader(String caminho) {
	        this.caminho = caminho;
	    }

	    /**
	     * Abre o arquivo e inicia o interador de linhas
	     * @throws IOException
	     */
	    public void openTextFile() throws IOException {
	        arquivo = new FileInputStream(this.caminho);
	        linhaArquivo = new BufferedReader(new InputStreamReader(arquivo));
	    }

	    /**
	     * Escreve todo o conteudo do arquivo
	     * @throws IOException
	     */
	    public void openTextFileWriter() throws IOException {
	        arquivo = new FileInputStream(this.caminho);
	        linhaArquivo = new BufferedReader(new InputStreamReader(arquivo));
	        sBody = getContent();
	        System.out.println(sBody);
	    }

	    
	    /**
	     * Verifica se o arquivo ainda tem linhas a ser lida
	     * @return true se verdadeiro false caso contrário
	     * @throws IOException
	     */
	    public boolean next() throws IOException {
	        return linhaArquivo.ready();
	    }

	    /**
	     * Lê uma linha do arquivo
	     * @return a atual linha do ponteiro do arquivo
	     * @throws IOException
	     */
	    public final String readLine() throws IOException {
	        return linhaArquivo.readLine();
	    }

	    /**
	     * Fecha o ponteiro do arquivo
	     * @throws IOException
	     */
	    public void closeTextFile() throws IOException {
	        linhaArquivo.close();
	    }

	    /**
	     * Pega todo o conteúdo do arquivo
	     * @return todo o conteúdo do arquivo
	     * @throws IOException
	     */
	    public String getContent() throws IOException {
	        String sContent="";
	        while(linhaArquivo.ready()){
	            sContent += linhaArquivo.readLine()+"\r\n";
	        }
	        return sContent;
	    }

}
