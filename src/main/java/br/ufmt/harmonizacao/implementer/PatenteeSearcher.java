/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufmt.harmonizacao.implementer;

import br.ufmt.harmonizacao.indexer.resources.analysis.PatenteeAnalyzer;
import br.ufmt.harmonizacao.indexer.resources.search.LengthQuery;
import br.ufmt.harmonizacao.interfaces.SearcherInterface;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author mattyws
 */
public class PatenteeSearcher implements SearcherInterface {

    private String path;
    private Analyzer analyzer = new PatenteeAnalyzer(Version.LUCENE_47);
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
            TokenStream stream = analyzer.tokenStream(field, new StringReader(
                    value));
            CharTermAttribute attr = stream
                    .getAttribute(CharTermAttribute.class);
            stream.reset();
            String valor = "";
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
                            acronymBq.add(new PrefixQuery(new Term(field, tokens[i])), BooleanClause.Occur.MUST);
                            bq.add(new PrefixQuery(new Term(field, tokens[i])),
                                    BooleanClause.Occur.SHOULD);
                            break;
                        case 1:
                            acronymBq.add(new FuzzyQuery(new Term(field, tokens[i])), BooleanClause.Occur.MUST_NOT);
                            bq.add(new FuzzyQuery(new Term(field, tokens[i])),
                                    BooleanClause.Occur.SHOULD);
                            bq.add(new LengthQuery(field, valor), BooleanClause.Occur.MUST_NOT);
                            break;
                        default:
                            break;
                    }
                } else {
                    if (tokens[i].length() > 3) {
                        bq.add(new FuzzyQuery(new Term(field, tokens[i])),
                                BooleanClause.Occur.MUST);
                    } else {
                        bq.add(new TermQuery(new Term(field, tokens[i])),
                                BooleanClause.Occur.MUST);
                    }
                }
            }
            
            stream.end();
            stream.close();
            // Aqui termina
            // Cria uma fuzzyquery, ela que fará a busca de aproximação
            
            wrapBq.add(bq, BooleanClause.Occur.MUST);
            if (acronymBq != null) {
                //new QueryParser(Version.LUCENE_47, field, new StandardAnalyzer(Version.LUCENE_47)).parse(query)
                wrapBq.add(acronymBq, BooleanClause.Occur.MUST_NOT);
            }
            String queryTime = "Tempo para construção da query : " + (System.currentTimeMillis() - start) + "ms";
            // Pegando os documentos encontrado na pesquisa
            start = System.currentTimeMillis();
            ScoreDoc[] hits = searcher.search(wrapBq, 10).scoreDocs;
            String searchTime = "Tempo para busca : " + (System.currentTimeMillis() - start) + "ms";
            List<String> result = new ArrayList<String>();
            result.add(valor);
            if(hits.length > 0) {
                for (int i = 0; i < hits.length; i++) {
                    Document hitDoc = searcher.doc(hits[i].doc);
                    result.add(hitDoc.get(field));
                }
            }
            result.add(queryTime);
            result.add(searchTime);
            return result;
        } catch (IOException ex) {
            Logger.getLogger(PatenteeSearcher.class.getName()).log(Level.SEVERE, null, ex);
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
