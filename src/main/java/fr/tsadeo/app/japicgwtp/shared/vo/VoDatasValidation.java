package fr.tsadeo.app.japicgwtp.shared.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VoDatasValidation implements IVo {

    private static final long serialVersionUID = 1L;

    private List<String> errorMessages;

    // ---------------------------- accessor
    public boolean isValid() {
        return  Objects.isNull(this.errorMessages) || this.errorMessages.isEmpty();
    }

    public String[] getErrorMessages() {
        if ( Objects.isNull(this.errorMessages)) {

            return new String[0];
        }
        return this.listToTab(errorMessages);
    }
    
    
    
    public void addMessage(String message) {
        this.getListMessages().add(message);
    }

    public void setErrorMessages(String... errorMessages) {
        if ( Objects.nonNull(errorMessages)) {
            this.getListMessages().addAll(Arrays.asList(errorMessages));
        }
    }

    //----------------------------- private methods
    private List<String> getListMessages() {
        if (this.errorMessages == null) {
            this.errorMessages = new ArrayList<>();
        }
        return this.errorMessages;
    }

    private String[] listToTab(List<String> list) {

        if (list == null) {
            return null;
        }
        
        return (String[])list.toArray(new String[list.size()]);
    }
}
