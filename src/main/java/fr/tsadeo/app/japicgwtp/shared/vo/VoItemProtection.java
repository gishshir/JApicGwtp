package fr.tsadeo.app.japicgwtp.shared.vo;

public class VoItemProtection implements IVo {

	private static final long serialVersionUID = 1L;

	private boolean delete = false;
	private boolean edit = false;
	private boolean create = false;
	private boolean update = false;

	// --------------------- accessors

	public void setCanUpdate(boolean canUpdate) {
		this.update = canUpdate;
	}

	public boolean canUpdate() {
		return this.update;
	}

	public void setCanCreate(boolean canCreate) {
		this.create = canCreate;
	}

	public boolean canCreate() {
		return this.create;
	}

	public boolean canEdit() {
		return this.edit;
	}

	public void setCanEdit(boolean canEdit) {
		this.edit = canEdit;
	}

	public boolean canDelete() {
		return this.delete;
	}

	public void setCanDelete(boolean canDelete) {
		this.delete = canDelete;
	}

}
