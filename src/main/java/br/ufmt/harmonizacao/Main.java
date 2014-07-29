package br.ufmt.harmonizacao;

import br.ufmt.harmonizacao.generics.Searcher;
import br.ufmt.harmonizacao.implementer.PatenteeSearcher;
import br.ufmt.harmonizacao.implementer.StandardSearcher;
import br.ufmt.harmonizacao.process.NameIndexer;
import br.ufmt.harmonizacao.indexer.resources.analysis.PatenteeAnalyzer;
import br.ufmt.harmonizacao.model.Nomes;
import br.ufmt.harmonizacao.process.Indexer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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

        // Pega nomes de um arquivo csv, o arquivo deve estar apenas com as
        //colunas de Inventor e Depositante
        Nomes nomes = Nomes
                .criarPorArquivoCsv("/home/mattyws/Documentos/resultado.csv");
        
        System.out.println("Gerou os nomes");
//        List<Class<? extends Analyzer>> listAnalyzer = new ArrayList<Class<? extends Analyzer>>();
//        listAnalyzer.add(PatenteeAnalyzer.class);
//        listAnalyzer.add(StandardAnalyzer.class);
//        
//        NameIndexer indexer = new NameIndexer(listAnalyzer);
//        try {
//            indexer.index(nomes);
//        } catch (Exception ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//        
//        System.out.println("Indexou!");
        
        PatenteeSearcher patenteeSearch = new PatenteeSearcher();
        
        Searcher<PatenteeSearcher> searcher = new Searcher<PatenteeSearcher>(patenteeSearch);
        searcher.search("name", nomes);
        searcher.close();
        
        StandardSearcher stanSearcher = new StandardSearcher();
        
        Searcher<StandardSearcher> searcher2 = new Searcher<StandardSearcher>(stanSearcher);
        searcher2.search("name", nomes);
        searcher2.close();
        
        System.out.println("Buscou!");
        System.out.println("Acabou!");
    }

}
