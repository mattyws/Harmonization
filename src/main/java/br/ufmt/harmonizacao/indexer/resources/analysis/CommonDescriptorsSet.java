package br.ufmt.harmonizacao.indexer.resources.analysis;

import br.ufmt.harmonizacao.model.Resources;
import com.github.jmkgreen.morphia.Datastore;
import com.mongodb.Mongo;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;

/**
 * This class will contains the commons company descriptors Now, it's just use a
 * ArrayList, but this may change in the future
 *
 * @author mattyws
 */

public class CommonDescriptorsSet {

    private IndexReader inReader;
    private  Datastore ds;

    private List<String> commonDescriptors = new ArrayList<String>();
    private BufferedReader reader;
    private IndexSearcher searcher;

    /**
     * Constructor for Lucene based CommonDescriptorsSet
     */
    public CommonDescriptorsSet() {
        Resources r = new Resources();
        Mongo mongo = r.createMongo();
        ds = r.mongoDs(mongo);
//        searcher = new IndexSearcher(inReader);
    }

    /**
     * The constructor
     *
     * @param caminho path for the file containing the commons descriptors
     */
//    public CommonDescriptorsSet(String caminho) {
//        try {
//            File file = new File(caminho);
//            reader = new BufferedReader(new FileReader(caminho));
//            this.readFile();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        
//    }
    /**
     * Read the file passed by parameter in the constructor
     *
     * @throws IOException
     */
//    private void readFile() throws IOException {
//        String linha;
//        while (reader.ready()) {
//            linha = reader.readLine();
//            commonDescriptors.add(linha);
//        }
//        reader.close();
//    }
    /**
     * See if a string exists in the list
     *
     * @param descriptor the text that will be searched
     * @return true if exists in list, false otherwise
     */
//    public boolean contains(String descriptor) {
//        return commonDescriptors.contains(descriptor);
//    }
    public boolean contains(String descriptor) {
//        Query query = new TermQuery(new Term("id", descriptor));

//        try {
//            ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;
//            for (int i = 0; i < hits.length; ++i) {
//                int docId = hits[i].doc;
//                Document d = searcher.doc(docId);
//                System.out.println(descriptor + " retornou : " + d.get("id"));
//            }
//            if(hits.length > 0) {
//                return true;
//            }
//        } catch (IOException ex) { 
//            Logger.getLogger(CommonDescriptorsSet.class.getName()).log(Level.SEVERE, null, ex);
//        }
        List<CommonDescriptor> descriptorSet = ds.find(CommonDescriptor.class).field("_id").equal(descriptor).asList();

        if (descriptorSet.size() > 0) {
            return true;
        }
        return false;
    }

}
