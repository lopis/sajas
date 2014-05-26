package up.fe.liacc.sajas.domain.FIPAAgentManagement;

import java.util.ArrayList;

import up.fe.liacc.sajas.core.AID;

public class DFAgentDescription {
	
	private AID name;
	private ArrayList<ServiceDescription> services;
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

	public ArrayList<ServiceDescription> getServices() {
		if (services == null) {
			services = new ArrayList<ServiceDescription>();
		}
		return services;
	}
	public void addServices(ServiceDescription service) {
		getServices().add(service);
	}
	public void removeServices(ServiceDescription service) {
		getServices().remove(service);
	}

	public ArrayList<String> getProtocols() {
		if (protocols == null) {
			protocols = new ArrayList<String>();
		}
		return protocols;
	}
	public void addProtocols(String protocol) {
		getProtocols().add(protocol);
	}
	public void removeProtocols(String protocol) {
		getProtocols().remove(protocol);
	}

	public ArrayList<String> getOntologies() {
		if (ontologies == null) {
			ontologies = new ArrayList<String>();
		}
		return ontologies;
	}
	public void addOntologies(String ontology) {
		getOntologies().add(ontology);
	}
	public void removeOntologies(String ontology) {
		getOntologies().remove(ontology);
	}

	public ArrayList<String> getLanguages() {
		if (languages == null) {
			languages = new ArrayList<String>();
		}
		return languages;
	}
	public void addLanguages(String language) {
		getLanguages().add(language);
	}
	public void removeLanguages(String language) {
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
	
	@Override
	public String toString() {
		return "[DFD] " + getName() + ": services(" + getServices().size() + ")";
	}
}
