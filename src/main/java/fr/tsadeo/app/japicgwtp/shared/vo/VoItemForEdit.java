/*
 * encapsule les information d'un item to edit
 */
package fr.tsadeo.app.japicgwtp.shared.vo;

/**
 *
 * @author sylvie
 */
public class VoItemForEdit extends VoIdName {
	
	private static final long serialVersionUID = 1L;

    private boolean validationDone = false;
    private TypeItem type;

    //----------------------------- constructor
    public VoItemForEdit() {
        super();
        this.type = null;
    }

     protected VoItemForEdit(TypeItem typeItem, VoIdName voIdName) {
         this(typeItem, voIdName.getId(), voIdName.getName());
     }
    protected VoItemForEdit(TypeItem typeItem, Long id, String name) {
        super(id, name);
        this.type = typeItem;
    }

    //------------------------- accessors
    @Override
    public void setName(String name) {
        super.setName(name);
    }

    public TypeItem getType() {
        return this.type;
    }

    public boolean isValidationDone() {
        return validationDone;
    }

    public void setValidationDone(boolean validationDone) {
        this.validationDone = validationDone;
    }

}
