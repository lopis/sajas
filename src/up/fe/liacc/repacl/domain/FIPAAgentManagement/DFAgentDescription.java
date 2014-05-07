package up.fe.liacc.repacl.domain.FIPAAgentManagement;

import java.util.ArrayList;

import up.fe.liacc.repacl.lang.acl.AID;

public class DFAgentDescription {
	
	private AID name;
	private ArrayList<String> services;
	private ArrayList<String> protocols;
	private ArrayList<String> ontologies;
	private ArrayList<String> languages;
	
	
	public DFAgentDescription() {
	}
	
	public AID getName() {
		return name;
	}
	public void setName(AID name) {
		this.name = name;
	}

	public ArrayList<String> getServices() {
		if (services == null) {
			services = new ArrayList<String>();
		}
		return services;
	}
	public void addService(String service) {
		getServices().add(service);
	}
	public void removeService(String service) {
		getServices().remove(service);
	}

	public ArrayList<String> getProtocols() {
		if (protocols == null) {
			protocols = new ArrayList<String>();
		}
		return protocols;
	}
	public void addProtocol(String protocol) {
		getProtocols().add(protocol);
	}
	public void removeProtocol(String protocol) {
		getProtocols().remove(protocol);
	}

	public ArrayList<String> getOntologies() {
		if (ontologies == null) {
			ontologies = new ArrayList<String>();
		}
		return ontologies;
	}
	public void addOntology(String ontology) {
		getOntologies().add(ontology);
	}
	public void removeOntology(String ontology) {
		getOntologies().remove(ontology);
	}

	public ArrayList<String> getLanguages() {
		if (languages == null) {
			languages = new ArrayList<String>();
		}
		return languages;
	}
	public void addLanguage(String language) {
		getLanguages().add(language);
	}
	public void removeLanguage(String language) {
		getLanguages().remove(language);
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof DFAgentDescription) &&
			((DFAgentDescription) obj).getLanguages().equals(this.getLanguages()) &&
			((DFAgentDescription) obj).getName().equals(this.getName()) &&
			((DFAgentDescription) obj).getOntologies().equals(this.getProtocols()) &&
			((DFAgentDescription) obj).getServices().equals(this.getServices());
	}
	
}
