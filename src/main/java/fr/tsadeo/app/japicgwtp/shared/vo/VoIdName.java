package fr.tsadeo.app.japicgwtp.shared.vo;

public class VoIdName extends AbstractVoId implements IVoId, Comparable<VoIdName> {

	private static final long serialVersionUID = 1L;

	private String name;

	// --------------------------------- accessor

	public String getName() {
		return name;
	}

	// ---------------------------- constructor
	public VoIdName() {
		this(IVoId.ID_UNDEFINED, null);
	}

	public VoIdName(VoIdName voIdName) {
		this(voIdName.getId(), voIdName.getName());
	}

	public VoIdName(Long id, String name) {
		super(id);
		this.name = name;
	}

	// ------------------------------- protected methods
	protected void setName(String name) {
		this.name = name;
	}

	// --------------------------------- overriding Comparable
	@Override
	public int compareTo(VoIdName o) {
		if (o == null) {
			return 1;
		}
		return this.getName().compareTo(o.getName());
	}

	// -------------------------------- overriding Object
	@Override
	public String toString() {

		return this.getId() + " - " + this.name;
	}

}
