package br.ufmt.harmonizacao.process;

import br.ufmt.harmonizacao.indexer.resources.analysis.PatenteeAnalyzer;
import br.ufmt.harmonizacao.model.Nome;
import br.ufmt.harmonizacao.model.Nomes;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


/**
 * Faz a indexação dos nomes em um diretório
 * Utiliza a classe Nomes como base de dados para indexar
 * 
 * @author mattyws
 *
 */
public class Indexer {

	private IndexWriter writer;
	private Directory dir;
	private Analyzer analyzer = new PatenteeAnalyzer(Version.LUCENE_47);

	/**
	 * Construtor da classe
	 * @param caminho Diretório onde será escrita os arquivos de indexação do lucene
	 * @throws IOException
	 */
	public Indexer(String caminho) throws IOException {
		dir = FSDirectory.open(new File(caminho));
		IndexWriterConfig config = new IndexWriterConfig(
				Version.LUCENE_36, analyzer);
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
		for (Nome i : nomes.getInventores()) {
			Document doc = new Document();
			doc.add(new TextField("id", aux.toString(), Field.Store.YES));
			doc.add(new TextField("nome", i.getNome(), Field.Store.YES));
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
