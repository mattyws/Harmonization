package br.ufmt.harmonizacao.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.SegmentReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import br.ufmt.harmonizacao.custom.PatenteeAnalyzer;

/**
 * Faz a busca em um deretório de indices do lucene
 * 
 * @author mattyws
 * 
 */
public class Searcher {

	// Diretório do lucene
	private Directory dir;
	// Leitor de diretórios do lucene
	private IndexSearcher searcher;
	private IndexReader reader;
	// O analyzer
	private Analyzer analyzer = new PatenteeAnalyzer(Version.LUCENE_36);
	String[] stopWords = { "inc", "machines", "ltd", "technology" };

	public Searcher(String caminho) throws IOException {
		// Diretório para os indices do lucene
		dir = new SimpleFSDirectory(new File(caminho));
		// instanciando o leitor do diretório
		reader = IndexReader.open(dir);
		// instanciando o pesquisador com o leitor de diretórios
		searcher = new IndexSearcher(reader);
	}

	/**
	 * Realiza a pesquisa sobre os indices
	 * 
	 * @param valor
	 *            valor a ser pesquisado
	 * @param campo
	 *            campo a realiza a pesquisa
	 * @param out
	 *            apenas o arquivo onde será imprimido o resultado de teste
	 * @throws ParseException
	 * @throws IOException
	 */
	public void pesquisar(String valor, String campo, BufferedWriter out)
			throws ParseException, IOException {

		// Reliza isto para ver se o valor não está apenas com espaços, isso dá
		// erro na pequisa do lucene
		if (!valor.trim().equals("")) {
			// Aqui inicia o processo de utilizar o analyzer para tornar o valor
			// passado
			// Do jeito que nós queremos. Isto não seria o correto, seria melhor
			// utilizar o
			// Query parser, mas ele está dando um pequeno problema, e não está
			// recuperando
			// Resultados semelhantes, como o SOFT e SOTF
			// TODO : Pesquisar sobre o parser, pois desse jeito deixa o código
			// lento

			// Cria uma stream de tokens com o analyzer
			TokenStream stream = analyzer.tokenStream(campo, new StringReader(
					valor));
			// Passa os atributos da stream para que seja possível recuperar seu
			// valor puro de texto
			CharTermAttribute attr = stream
					.getAttribute(CharTermAttribute.class);
			// resetar a stream é necessário fazer, não sei o porque, mais se
			// não o fizer da erro
			stream.reset();
			// Recuperando o valor de texto da stream
			valor = "";
			while (stream.incrementToken()) {
				valor = valor + attr.toString() + ' ';
			}

			BooleanQuery bq = new BooleanQuery();
			BooleanQuery acronymBq = null;
			String query = "";
			BooleanQuery wrapBq = new BooleanQuery();
			String[] tokens = valor.split(" ");
			for (int i = 0; i < tokens.length; i++) {
				if (tokens.length >= 2) {
					acronymBq = new BooleanQuery();
					switch (i) {
					case 0:
						query = query + '+' + tokens[i] + '*';
						acronymBq.add(new PrefixQuery(new Term(campo, tokens[i])), Occur.MUST);
						bq.add(new PrefixQuery(new Term(campo, tokens[i])),
								Occur.SHOULD);
						break;
					case 1:
						query = query + " -" + tokens[i] + '~';
						acronymBq.add(new FuzzyQuery(new Term(campo, tokens[i])), Occur.MUST_NOT);
						bq.add(new FuzzyQuery(new Term(campo, tokens[i])),
								Occur.SHOULD);
						break;
					default:
						break;
					}
				} else {
					if (tokens[i].length() > 3) {
						bq.add(new FuzzyQuery(new Term(campo, tokens[i])),
								Occur.MUST);
					} else {
						bq.add(new TermQuery(new Term(campo, tokens[i])),
								Occur.MUST);
					}
				}
			}

			stream.end();
			stream.close();
			// Aqui termina
			// Cria uma fuzzyquery, ela que fará a busca de aproximação

			wrapBq.add(bq, Occur.MUST);
			if(acronymBq != null) {
				System.out.println("FOIII");
				//new QueryParser(Version.LUCENE_36, campo, new StandardAnalyzer(Version.LUCENE_36)).parse(query)
				wrapBq.add(acronymBq, Occur.MUST_NOT);
			}
			// Pegando os documentos encontrado na pesquisa
			ScoreDoc[] hits = searcher.search(wrapBq, 8).scoreDocs;
			if (hits.length >= 0) {
				// Imprimindo a string que foi utilizada na busca
				out.write(valor);
				out.write("\n---------------------------------------\n");
				// Imprimindo todos os resultados no arquivo utilizando um
				// método do lucene que explica
				// Todos os resultados da busca
				for (int i = 0; i < hits.length; i++) {
					Document hitDoc = searcher.doc(hits[i].doc);
					out.write(hitDoc.get("id") + " - " + hitDoc.get(campo)
							+ " - " + hitDoc.get("nacionalidade") + "\n");
				}
				out.write("---------------------------------------\n");
			}
		}
	}

	public void close() {
		try {
			reader.close();
			dir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
