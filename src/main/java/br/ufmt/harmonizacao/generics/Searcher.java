/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufmt.harmonizacao.generics;

import br.ufmt.harmonizacao.interfaces.SearcherInterface;
import br.ufmt.harmonizacao.model.Nome;
import br.ufmt.harmonizacao.model.Nomes;
import br.ufmt.harmonizacao.model.Resources;
import br.ufmt.harmonizacao.process.NameIndexer;
import com.github.jmkgreen.morphia.Datastore;
import com.mongodb.Mongo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;

/**
 *
 * @author mattyws
 */
public class Searcher <T extends SearcherInterface> {
    
    private T searcher;
    private Datastore ds;
    private String resultWritePath = "/home/mattyws/Documentos/luceneResultado/";
    private String path = NameIndexer.indexPath;
//    BufferedWriter out = new BufferedWriter(new FileWriter(
//                "/home/mattyws/Documentos/luceneResultado/resultado"));
    
    public Searcher(T t) {
        searcher = t;
        searcher.setPath(path);
        resultWritePath = resultWritePath + searcher.getDirectoryName() + '/';
    }
    
    public void search(String field, Nomes nomes) throws IOException {
        File file = new File(resultWritePath);
        if(!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(resultWritePath));
        for(Nome nome : nomes.getInventores()) {
            List<String> results = searcher.search(field, nome.getNome());
            if( results != null && results.size() > 6) {
                out.write("Search: "+nome.getNome()+"\n");
                for(String result : results) {
                    out.write("-" + result+"\n");
                }
                out.write("\n");
            }
        }
        out.close();
    }

    
    public void close() {
        searcher.close();
    }
    
    
    
}
