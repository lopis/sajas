package up.fe.liacc.sajas.domain.FIPAAgentManagement;

import java.util.ArrayList;
import java.util.Iterator;

public class ServiceDescription {

	private String name;
	private String type;
	
	private ArrayList<String> protocols = new ArrayList<String>();
	private ArrayList<String> ontologies = new ArrayList<String>();
	private ArrayList<String> languages = new ArrayList<String>();
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void removeProtocols(String protocol) {
		protocols.remove(protocol);
	}
	
	public void addProtocols(String protocol) {
		protocols.add(protocol);
	}
	
	public Iterator<String> getAllProtocols() {
		return protocols.iterator();
	}

	public void removeOntologies(String ontology) {
		ontologies.remove(ontology);
	}
	
	public void addOntologies(String ontology) {
		ontologies.add(ontology);
	}
	
	public Iterator<String> getAllOntologies() {
		return ontologies.iterator();
	}

	public void removeLanguages(String language) {
		languages.remove(language);
	}
	
	public void addLanguages(String language) {
		languages.add(language);
	}
	
	public Iterator<String> getAllLanguages() {
		return languages.iterator();
	}


	@Override
	public boolean equals(Object obj) {
		return this.hashCode() == obj.hashCode();
	}
	
	@Override
	public int hashCode() {
		return (name + type).hashCode();
	}
	
}
