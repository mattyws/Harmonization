package br.ufmt.harmonizacao.process;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import br.ufmt.harmonizacao.custom.PatenteeAnalyzer;
import br.ufmt.harmonizacao.model.Depositante;
import br.ufmt.harmonizacao.model.Inventor;
import br.ufmt.harmonizacao.model.Nomes;

/**
 * Faz a indexação dos nomes em um diretório
 * Utiliza a classe Nomes como base de dados para indexar
 * 
 * @author mattyws
 *
 */
public class Indexer {

	// A classe do lucene responsável por escrever todos os arquivo de indexação
	private IndexWriter writer;
	// Classe diretório do lucene, é o que irá manipular todos os diretórios
	private Directory dir;
	// Analyzer, é o responsável por criar os tokens que será indexado pelo lucene
	// PatenteeAnalyzer é customizado, ele está no pacote : br.ufmt.harmonizacao.custom
	private Analyzer analyzer = new PatenteeAnalyzer(Version.LUCENE_36);

	/**
	 * Construtor da classe
	 * @param caminho Diretório onde será escrita os arquivos de indexação do lucene
	 * @throws IOException
	 */
	public Indexer(String caminho) throws IOException {
		// Abre um diretório normal passado como parâmetro
		dir = FSDirectory.open(new File(caminho));
		// Faz as configuração do indexador, aqui se passa o analyzer como parâmetro
		IndexWriterConfig config = new IndexWriterConfig(
				Version.LUCENE_36, analyzer);
		// Inicia o indexador com a configuração e o diretório
		writer = new IndexWriter(dir, config);
	}

	/**
	 * Realiza a indexação dos nomes no caminho do diretório passado no contrutor
	 * @param nomes
	 * @throws IOException
	 */
	public void indexar(Nomes nomes) throws IOException {
		Integer aux = 1;
		// Vamos indexar todos os inventores
		for (Inventor i : nomes.getInventores()) {
			//Document é a base de dados do lucene, ele que armazena os textos a serem indexados
			Document doc = new Document();
			// Apenas um id para diferenciar todos os nomes
			doc.add(new Field("id", aux.toString(), Store.YES, Index.NOT_ANALYZED));
			// o nome do inventor
			doc.add(new Field("inventor", i.getNome(), Store.YES, Index.ANALYZED));
			// sua nacionalidade
			doc.add(new Field("nacionalidade", i.getNacionalidade(),
					Store.YES, Index.NOT_ANALYZED));
			// Deleta o documento passado
			writer.deleteDocuments(new Term("id", aux.toString()));			
			aux = aux + 1;
			// Adiciona o documento na pasta de indices do lucene
			// Aqui é feita toda a análise e tokenização do lucene
			writer.addDocument(doc);
		}
		// O mesmo para os depositantes
		for (Depositante d : nomes.getDepositantes()) {
			Document doc = new Document();
			doc.add(new Field("id", aux.toString(), Store.YES, Index.NOT_ANALYZED));
			doc.add(new Field("depositante", d.getNome(), Store.YES, Index.ANALYZED));
			doc.add(new Field("nacionalidade", d.getNacionalidade(),
					Store.YES, Index.NOT_ANALYZED));
			writer.deleteDocuments(new Term("id", aux.toString()));
			aux = aux + 1;
			writer.addDocument(doc);
		}

	}

	public void close() {
		try {
			writer.close();
			dir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
