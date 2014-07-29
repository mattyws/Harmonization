package br.ufmt.harmonizacao.model;

import java.net.UnknownHostException;
import java.util.logging.Logger;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.github.jmkgreen.morphia.mapping.lazy.DatastoreHolder;
import com.mongodb.Mongo;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence
 * context, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
public class Resources {

	//@Produces
	//@PersistenceContext
	//private EntityManager em;
	
	public Mongo createMongo() {
		try {			
			return new Mongo();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Morphia morphia = new Morphia();


	public Datastore mongoDs(Mongo mongo) {				
		Datastore ds = morphia.createDatastore(mongo, "test");		
		ds.ensureCaps();
		ds.ensureIndexes();
		morphia.mapPackage("br.ufmt.periscope.model");		
		DatastoreHolder.getInstance().set(ds);
		return ds;
	}	
	
}
