package voxpetrae.musicmetadata.services.nameorder;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import voxpetrae.musicmetadata.services.interfaces.NameOrderOperations;

public class NameOrderModifier implements voxpetrae.musicmetadata.services.interfaces.NameOrderModifier {
    @Inject private NameOrderOperations _nameOrderOperations;
    public String setNameOrder(String nameString, String desiredNameOrder) {
        String[] names = nameString.split(";");
        List<String> alteredNames = new ArrayList<>();
        for (String name : names) {
            String trimmedName = name.trim(); // Belt and suspenders
            String alteredName = "";
            if (_nameOrderOperations.detectNameOrder(trimmedName) == NameOrder.GIVENNAMESPACESURNAME
                    && desiredNameOrder.equals(NameOrder.SURNAMECOMMASPACEGIVENNAME.name())) {
                alteredName = _nameOrderOperations.changeNameOrderToSurnameCommaSpaceGivenName(trimmedName);
            } else if (_nameOrderOperations.detectNameOrder(trimmedName) == NameOrder.SURNAMECOMMASPACEGIVENNAME
                    && desiredNameOrder.equals(NameOrder.GIVENNAMESPACESURNAME.name())) {
                alteredName = _nameOrderOperations.changeNameOrderToGivenNameSpaceSurname(trimmedName);
            } else {
                alteredName = trimmedName;
            }
            alteredNames.add(alteredName);
        }
        String stringifiedList = _nameOrderOperations.convertListToSemicolonSeparatedString((alteredNames));
        return stringifiedList;
    }
}