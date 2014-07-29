/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufmt.harmonizacao.generics;

import br.ufmt.harmonizacao.indexer.resources.analysis.PatenteeAnalyzer;
import br.ufmt.harmonizacao.interfaces.IndexerInterface;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author mattyws
 */
public class Indexer<A extends Analyzer> {

    private String path;
    private IndexWriter writer;
    private Directory dir;
    private Analyzer analyzer;
    private String dirName;

    public Indexer(Class<? extends Analyzer> cl, String path) throws Exception {
        Constructor<? extends Analyzer> c = cl.getConstructor(Version.class);
        Analyzer a = c.newInstance(new Object[]{Version.LUCENE_47});
        analyzer = a;
        dirName = analyzer.getClass().getCanonicalName();
        this.path = path + dirName;
        dir = FSDirectory.open(new File(this.path));
        IndexWriterConfig config = new IndexWriterConfig(
                Version.LUCENE_47, analyzer);
        writer = new IndexWriter(dir, config);
    }

    public void index(Document doc) {
        try {
            writer.deleteDocuments(new Term("id", doc.get("id")));
            writer.addDocument(doc);
        } catch (IOException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
