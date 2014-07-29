/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufmt.harmonizacao.implementer;

import br.ufmt.harmonizacao.indexer.resources.analysis.PatenteeAnalyzer;
import br.ufmt.harmonizacao.interfaces.SearcherInterface;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author mattyws
 */
public class StandardSearcher implements SearcherInterface {

    private Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
    private String path;
    private String dirName = analyzer.getClass().getCanonicalName();
    private Directory dir;
    private IndexSearcher searcher;
    private IndexReader reader;

    public String getDirectoryName() {
        return dirName;
    }

    public void setPath(String path) {
        try {
            this.path = path + dirName;
            System.out.println(this.path);
            dir = new SimpleFSDirectory(new File(this.path));
            reader = DirectoryReader.open(dir);
            searcher = new IndexSearcher(reader);
        } catch (IOException ex) {
            Logger.getLogger(PatenteeSearcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> search(String field, String value) {
        try {
            long start = System.currentTimeMillis();
            value = value.trim().toLowerCase().replaceAll("[^A-Za-z0-9]", "");
            StringBuilder queryBuilder = new StringBuilder();
            String[] terms = value.split(" ", -2);
            for (String term : terms) {
                queryBuilder.append(term.trim() + "~ ");
                queryBuilder.append(term.trim() + "* ");
            }

            //queryBuilder.append("NOT \""+name+"\" ");
            queryBuilder.append("\"" + value + "\"~2 ");
            Query query;
            query = new QueryParser(Version.LUCENE_47, field, analyzer)
                    .parse(queryBuilder.toString());
            query.setBoost(0.1f);
            String queryTime = "Tempo de criação da query : " + (System.currentTimeMillis() - start) + "ms";
            start = System.currentTimeMillis();
            ScoreDoc[] hits = searcher.search(query,  10).scoreDocs;
            String searchTime = "Tempo para busca : " + (System.currentTimeMillis() - start) + "ms";
            List<String> result = new ArrayList<String>();
            result.add(queryBuilder.toString());
            if (hits.length > 0) {
                for (int i = 0; i < hits.length; i++) {
                    Document hitDoc = searcher.doc(hits[i].doc);
                    result.add(hitDoc.get(field));
                }
            }
            result.add(queryTime);
            result.add(searchTime);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(StandardSearcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
