package up.fe.liacc.repacl.domain.FIPAAgentManagement;

import java.util.ArrayList;

public class DFAgentDescription {
	
	private String name;
	private ArrayList<String> services;
	private ArrayList<String> protocols;
	private ArrayList<String> ontologies;
	private ArrayList<String> languages;
	
	
	public DFAgentDescription() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getServices() {
		return services;
	}
	public void addService(String service) {
		this.services.add(service);
	}
	public void removeService(String service) {
		this.services.remove(service);
	}

	public ArrayList<String> getProtocols() {
		return protocols;
	}
	public void addProtocol(String protocol) {
		this.protocols.add(protocol);
	}
	public void removeProtocol(String protocol) {
		this.protocols.remove(protocol);
	}

	public ArrayList<String> getOntologies() {
		return ontologies;
	}
	public void addOntology(String ontology) {
		this.ontologies.add(ontology);
	}
	public void removeOntology(String ontology) {
		this.ontologies.remove(ontology);
	}

	public ArrayList<String> getLanguages() {
		return languages;
	}
	public void addLanguage(String language) {
		this.languages.add(language);
	}
	public void removeLanguage(String language) {
		this.languages.remove(language);
	}
	
}
