package br.ufmt.harmonizacao;

import br.ufmt.harmonizacao.custom.PatenteeAnalyzer;
import br.ufmt.harmonizacao.model.Nomes;
import br.ufmt.harmonizacao.process.Indexer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import br.ufmt.harmonizacao.process.Searcher;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;

/**
 * Irá realizar todo um processo teste para as classes cutomizadas do lucene
 *
 * @author mattyws
 *
 */
public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Iniciou!!");
        Analyzer analyzer = new PatenteeAnalyzer(Version.LUCENE_47);

		// Pega nomes de um arquivo csv, o arquivo deve estar apenas com as
        // colunas de Inventor e Depositante
//		Nomes nomes = Nomes
//				.criarPorArquivoCsv("/home/mattyws/Documentos/results.csv");
//		System.out.println("Criou os nomes");
//		// Vamos criar um teste para ver se está dando certo :)
//		
//		// Criando o indexador do projeto, ele que vai criar os indices do
//		// lucene
//		// A pasta passada será a pasta onde será armazenado os arquivos de
//		// indices do lucene
//		Indexer indexer = new Indexer("/home/mattyws/Documentos/lucene");
//		// Indexa todos os nomes do arquivo
//		indexer.indexar(nomes);
//		indexer.close();
//        System.out.println("Indexou!");
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
        // Vamos fazer as pesquisas para todos os nomes que está nos
        // depositantes
        //for (Inventor d : nomes.getInventores()()) {
        // Realiza a pesquisa pelo método da classe Searcher
        //searcher.pesquisar("KAZAKIDIS PROKOPIOS NIKOLAOU", "inventor", out);

        try {
            searcher.pesquisar("ECOPACIFIC EMPRESA COM DEL PACIFICO S A", "depositante", out);
            searcher.pesquisar("Apple Inc", "depositante", out);
            searcher.pesquisar("International Business Machine", "depositante", out);
            searcher.pesquisar("Soft Machines Inc", "depositante", out);
            searcher.pesquisar("SAWCHENKO PAUL F", "depositante", out);
            searcher.pesquisar("GEN HOSPITAL CORP", "depositante", out);
            searcher.pesquisar("PROTEOLOGICS INC", "depositante", out);
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        //}
        // Fechando todas as classes
        searcher.close();
        out.close();
        // Acabou!
        System.out.println("Acabou!");
    }

}
