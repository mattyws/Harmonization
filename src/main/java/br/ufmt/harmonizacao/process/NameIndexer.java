/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufmt.harmonizacao.process;

import br.ufmt.harmonizacao.generics.Indexer;
import br.ufmt.harmonizacao.interfaces.IndexerInterface;
import br.ufmt.harmonizacao.model.Nome;
import br.ufmt.harmonizacao.model.Nomes;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.util.Version;

/**
 *
 * @author mattyws
 */
public class NameIndexer {

    private List<Class<? extends Analyzer>> analyzers;
    public final static String indexPath = "/home/mattyws/Documentos/lucene/";

    public NameIndexer(List<Class<? extends Analyzer>> analyzers) {
        this.analyzers = analyzers;
    }

    public void index(Nomes nomes) throws Exception {
        Integer aux = 0;
        for (Class<? extends Analyzer> cl : analyzers) {
            Indexer<Analyzer> indexer = new Indexer<Analyzer>(cl, indexPath);
            for (Nome nome : nomes.getInventores()) {
                Document doc = new Document();
                doc.add(new TextField("id", aux.toString(), Field.Store.YES));
                doc.add(new TextField("name", nome.getNome(), Field.Store.YES));
                aux += 1;
                indexer.index(doc);
            }
            indexer.close();
        }

    }

}
