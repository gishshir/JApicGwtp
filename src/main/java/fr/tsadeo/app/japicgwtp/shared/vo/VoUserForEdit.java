package fr.tsadeo.app.japicgwtp.shared.vo;

import fr.tsadeo.app.japicgwtp.shared.domain.UserProfile;

public class VoUserForEdit extends VoItemForEdit {
	
	private static final long serialVersionUID = 1L;
	
	private String login;
	private String pwd;
	
	private UserProfile profile;
	
	 //---------------------- constructor
    public VoUserForEdit() {super();}
    public VoUserForEdit(VoIdName voIdName) {
        super(TypeItem.user, voIdName);
    }
    //---------------------------------- accessors
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public UserProfile getProfile() {
		return profile;
	}
	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}



    
    
}
