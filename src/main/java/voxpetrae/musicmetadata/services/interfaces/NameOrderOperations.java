package voxpetrae.musicmetadata.services.interfaces;

import java.util.List;
import voxpetrae.musicmetadata.services.nameorder.NameOrder;

public interface NameOrderOperations {
    NameOrder detectNameOrder(String name);
    String changeNameOrderToGivenNameSpaceSurname(String name);
    String changeNameOrderToSurnameCommaSpaceGivenName(String name);
    String convertListToSemicolonSeparatedString(List<String> listToConvert);
}