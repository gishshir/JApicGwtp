package fr.tsadeo.app.japicgwtp.server.util;

import java.util.Objects;

import fr.tsadeo.app.japicgwtp.server.domain.IModelItem;
import fr.tsadeo.app.japicgwtp.server.domain.converter.IResourceConverter;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.IVo;
import fr.tsadeo.app.japicgwtp.shared.vo.IVoId;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdUtils;

public abstract class AbstractValidator {

    // FIXME a garder ??
    protected long itemsToLongValue(int[] items, int[] digits) {

        int length = items.length;
        long value = 0;

        byte exposant = 0;
        for (int i = 0; i < length; i++) {
            int digit = digits[i];

            value += items[length - (i + 1)] * ((long) Math.pow(10, exposant));
            exposant += digit;
        }

        return value;
    }

    public VoDatasValidation validateIds(IModelItem bddItem, IVoId voItem) {
        VoDatasValidation voValidation = new VoDatasValidation();
        if (voItem.isUndefined()) {
            this.validateNull(bddItem, "Bdd item", voValidation);
        } else {
            this.validateNotNull(bddItem, "Bdd item", voValidation);
        }

        return voValidation;
    }

    protected void validateUnicity(IModelItem bddItem, IVoId vo, String comment, VoDatasValidation voValidation) {

        if (bddItem != null && !Objects.equals(bddItem.getId(), vo.getId())) {
            voValidation.addMessage("The " + comment + " is already in use!");
        }
    }

    protected boolean validateNotTransient(long itemId, String comment, VoDatasValidation voValidation) {
        if (VoIdUtils.isIdUndefined(itemId)) {
            voValidation.addMessage("The id of " + comment + " is undefined!");
            return false;
        }
        return true;
    }

    protected String getRange(Number valueInf, Number valueSup) {
        return "[" + valueInf + ", " + valueSup + "]";
    }

    protected void validateInOutRange(boolean inRange, int value, int valueInf, int valueSup, String comment,
            String requiredRange) throws JApicException {

        String rangeMessage = this.getInOutRangeMessage(inRange, valueInf, valueSup, comment, requiredRange);

        // value in [valueInf, valueSup]
        if (inRange) {
            if (!(value >= valueInf && value <= valueSup)) {
                throw new JApicException(rangeMessage);
            }
        } // value out of [valueInf, valueSup]
        else if ((value >= valueInf && value <= valueSup)) {
            throw new JApicException(rangeMessage);
        }
    }

    protected void validateInOutRange(boolean inRange, long value, long valueInf, long valueSup, String comment,
            String requiredRange) throws JApicException {

        if (valueSup < 0 || valueInf < 0) {
            return;
        }

        requiredRange = (requiredRange == null) ? "[" + valueInf + ", " + valueSup + "]" : requiredRange;
        // value in [valueInf, valueSup]
        if (inRange) {
            if (!(value >= valueInf && value <= valueSup)) {
                throw new JApicException(comment + " must be in " + requiredRange + " range!");
            }
        } // value out of [valueInf, valueSup]
        else if ((value >= valueInf && value <= valueSup)) {
            throw new JApicException(comment + " must be out of " + requiredRange + " range!");
        }
    }

    /**
     *
     * @param toValidate
     * @param digits : count max of digit
     * @param comment
     * @return
     * @throws JApicException
     */
    protected int validateNumber(String toValidate, int digits, String comment) throws JApicException {

        int value = this.validateNumber(toValidate, comment);
        if (digits <= 0) {
            return value;
        }

        int maxValue = (int) Math.pow(10, digits);
        if (!(value < (maxValue))) {
            throw new JApicException("The " + comment + " must be < " + maxValue + "!");
        }
        return value;
    }

    protected int validateNumber(String toValidate, String comment) throws JApicException {

        int value = Integer.MAX_VALUE;
        try {
            value = Integer.parseInt(toValidate);
        } catch (NumberFormatException e) {
            throw new JApicException("The " + comment + " is not a number!");
        }
        return value;
    }

    protected void validateRequired(String toValidate, String comment, VoDatasValidation voValidation) {
        if (this.validateNotNull(toValidate, comment, voValidation) && toValidate.length() == 0) {
            voValidation.addMessage("The " + comment + " is required!");
        }
    }

    protected boolean validateNotNull(Object toValidate, String comment, VoDatasValidation voValidation) {
        if (toValidate == null) {
            voValidation.addMessage("The " + comment + " cannot be null!");
            return false;
        }
        return true;
    }

    protected void validateNull(Object toValidate, String comment, VoDatasValidation voValidation) {
        if (toValidate != null) {
            voValidation.addMessage("The " + comment + " must be null!");
        }
    }

    protected void validateIntValue(int toValidate, int minValue, String comment) throws JApicException {

        if (toValidate <= minValue) {
            String message = (minValue == 0) ? " cannot be null or negative!" : " must been > " + minValue;
            throw new JApicException(comment + message);
        }
    }

    protected void validateIdDefined(int idToValidate, String comment, VoDatasValidation voValidation) {

        if (idToValidate == IVo.ID_UNDEFINED) {
            voValidation.addMessage("The " + comment + " must be defined!");
        }
    }

    protected void validateString(String toValidate, int minLength, String comment, VoDatasValidation voValidation) {
        if (this.validateNotNull(toValidate, comment, voValidation) && toValidate.length() < minLength) {
            voValidation.addMessage("The " + comment + " must have more than " + minLength + " characters!");
        }
    }

    protected void validateUrl(String url, String comment, IResourceConverter converter, VoDatasValidation voValidation) {

        if (this.validateNotNull(url, comment, voValidation) && !converter.valideUrl(url)) {
            voValidation.addMessage("The " + comment + " is not a valid url!");
        }
    }

    private String getInOutRangeMessage(boolean inRange, Number valueInf, Number valueSup, String comment,
            String requiredRange) {

        if (valueSup == null || valueInf == null) {
            return null;
        }

        requiredRange = (requiredRange == null) ? this.getRange(valueInf, valueSup) : requiredRange;
        String rangeMessage = (inRange) ? comment + " must be in " + requiredRange + " range!" : comment
                + " must be out of " + requiredRange + " range!";

        return rangeMessage;
    }

}
