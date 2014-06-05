package br.ufmt.harmonizacao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;

import br.ufmt.harmonizacao.custom.PatenteeAnalyzer;
import br.ufmt.harmonizacao.model.Depositante;
import br.ufmt.harmonizacao.model.Inventor;
import br.ufmt.harmonizacao.model.Nomes;
import br.ufmt.harmonizacao.process.Indexer;
import br.ufmt.harmonizacao.process.Searcher;
import br.ufmt.harmonizacao.reader.CSVReader;

/**
 * Irá realizar todo um processo teste para as classes cutomizadas do lucene
 * 
 * @author mattyws
 * 
 */
public class Main {

	public static void main(String[] args) throws IOException {

		System.out.println("Iniciou!!");
		/*Analyzer analyzer = new PatenteeAnalyzer(Version.LUCENE_47);
		// Cria uma stream de tokens com o analyzer
		TokenStream stream = analyzer.tokenStream("inventor",
				new StringReader("Tentativa! de,@$@#$3323$# limpar!@#433 pontuação!#@$%5394783#$#$,.><;/?"));
		// Passa os atributos da stream para que seja possível recuperar seu
		// valor puro de texto
		CharTermAttribute attr = stream.getAttribute(CharTermAttribute.class);
		// resetar a stream é necessário fazer, não sei o porque, mais se não o
		// fizer da erro
		stream.reset();
		// Recuperando o valor de texto da stream
		String valor = "";
		while (stream.incrementToken()) {
			valor = valor + attr.toString();
		}
		stream.end();
		stream.close();
		System.out.println(valor);*/

		// Pega nomes de um arquivo csv, o arquivo deve estar apenas com as
		// colunas de Inventor e Depositante
		Nomes nomes = Nomes
				.criarPorArquivoCsv("/home/mattyws/Documentos/results.csv");
		System.out.println("Criou os nomes");
		// Nomes nomes = new Nomes();
		// Vamos criar um teste para ver se está dando certo :)
		nomes.addDepositante(new Depositante("IBM", "US"));
		nomes.addDepositante(new Depositante("International Business Machine", "US"));
		
		// Criando o indexador do projeto, ele que vai criar os indices do
		// lucene
		// A pasta passada será a pasta onde será armazenado os arquivos de
		// indices do lucene
		Indexer indexer = new Indexer("/home/mattyws/Documentos/lucene");
		// Indexa todos os nomes do arquivo
		indexer.indexar(nomes);
		indexer.close();
		System.out.println("Indexou!");

		// pesquisa no diretório de indices do lucene, o diretório deve ser o
		// mesmo
		// onde foi indexado os nomes
		Searcher searcher = new Searcher("/home/mattyws/Documentos/lucene");
		// BufferedWriter é a classe que vai fazer o arquivo de teste, é nativa
		// do java, então só passe
		// o caminho do arquivo que ira armazenar os resultados obtidos pela
		// pesquisa
		BufferedWriter out = new BufferedWriter(new FileWriter(
				"/home/mattyws/Documentos/luceneResultado/resultado"));
		try {
			// Vamos fazer as pesquisas para todos os nomes que está nos
			// depositantes
			//for (Inventor d : nomes.getInventores()()) {
				// Realiza a pesquisa pelo método da classe Searcher
				//searcher.pesquisar("KAZAKIDIS PROKOPIOS NIKOLAOU", "inventor", out);
			searcher.pesquisar("Apple Inc", "depositante", out);
				searcher.pesquisar("International Business Machine", "depositante", out);
				searcher.pesquisar("Soft Machines Inc", "depositante", out);
				searcher.pesquisar("SAWCHENKO PAUL F", "depositante", out);
				searcher.pesquisar("GEN HOSPITAL CORP", "depositante", out);
				searcher.pesquisar("PROTEOLOGICS INC", "depositante", out);
				searcher.pesquisar("ECOPACIFIC EMPRESA COM DEL PACIFICO S A", "depositante", out);
				
				
			//}
			// Fechando todas as classes
			searcher.close();
			out.close();
			// Acabou!
			System.out.println("Acabou!");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
