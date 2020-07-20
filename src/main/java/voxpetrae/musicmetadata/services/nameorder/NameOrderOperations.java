package voxpetrae.musicmetadata.services.nameorder;

import java.util.List;

public class NameOrderOperations implements voxpetrae.musicmetadata.services.interfaces.NameOrderOperations {
    public NameOrder detectNameOrder(String name) {
        if (name.contains(" ") && !name.contains(",")) {
            return NameOrder.GIVENNAMESPACESURNAME;
        } else if (name.contains(",")) {
            return NameOrder.SURNAMECOMMASPACEGIVENNAME;
        } else {
            return NameOrder.AMBIGUOUS;
        }
    }

    public String changeNameOrderToGivenNameSpaceSurname(String name) {
        if (detectNameOrder(name) == NameOrder.SURNAMECOMMASPACEGIVENNAME) {
            String[] names = name.split(",");
            if (names.length == 2) {
                String surname = names[0].trim();
                String givenName = names[1].trim();
                return givenName + " " + surname;
            } else {
                System.out.println(name + " has wrong name format, should be '[surname], [given name]' ");
            }
        }
        return name;
    }

    public String changeNameOrderToSurnameCommaSpaceGivenName(String name) {
        if (detectNameOrder(name) == NameOrder.GIVENNAMESPACESURNAME) {
            String givenName = name.split(" ")[0];
            String restOfName = name.substring(name.indexOf(' ') + 1);
            return restOfName.trim() + ", " + givenName.trim();
        } else {
            System.out.println(name + " has wrong name format, should be '[given name] [surname]' ");
        }
        return name;
    }

    public String convertListToSemicolonSeparatedString(List<String> listToConvert) {
        StringBuilder sb = new StringBuilder();
        listToConvert.forEach((String word) -> {
            sb.append(word);
            if (listToConvert.indexOf(word) < listToConvert.size() - 1)
                sb.append("; ");
        });
        return sb.toString();
    }
}