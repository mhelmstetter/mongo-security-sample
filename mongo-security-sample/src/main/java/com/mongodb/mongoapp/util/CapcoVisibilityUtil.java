package com.mongodb.mongoapp.util;

import java.util.*;

/**
 * CapcoVisibilityString Utilities.
 *
 * <p>
 *    The goal of these utilities are to allow easy use of CAPCO specifications, and easy integration in other
 *    client software systems.  Our reference implementation needs a particular encoding of the CAPCO settings
 *    and we want to make it easy to convert into that format.
 *    Taking a simple case of TS or top-secret, a person with TS will have S C and U.  The lower level of this
 *    reference implementation of the FLAC utility needs to have all capabilities spelled out
 *    in order to make database operations fast.
 * </p>
 *
 * <p>
 *    By using the convertJavaToEncodeCapcoVisibility method you can pass in high level capabilities in a concise manner
 *    which might match what you have stored in other systems, that will then be properly recursively Expanded.
 * </p>
 *
 * <p>
 *    Consider having to encode a Top Secret CAPCO setting, you can call:
 *    <tt>
 *        CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS"}) =>
 *         generates
 *        [ { c:"TS" }, { c:"S" }, { c:"U" }, { c:"C" } ]   (( in javascript notation which is needed by the
 *                                                             reference FLAC implementation))
 *    </tt>
 * </p>
 */
public class CapcoVisibilityUtil {

    protected static final boolean EXPAND_CAPCO_AS_TREE_OF_VISIBILITY = true;

    /**
     * Convert java array of simple strings like: "c:TS" , "c:S"  into an appropriate CapcoVisibilityString like:
     * <tt>
     * [ { c:"TS" }, { c:"S" }, { c:"U" }, { c:"C" }, { sci:"TK" }, { sci:"SI" }, { sci:"G" }, { sci:"HCS" } ]
     </tt>
     * @param userCapcoVisibilityStrings
     * @return
     */
    public static String convertJavaToEncodeCapcoVisibility(String[] userCapcoVisibilityStrings) {

        return convertJavaToEncodeCapcoVisibility( ((userCapcoVisibilityStrings == null)
                ? (List<String> ) null
                : Arrays.asList(userCapcoVisibilityStrings)));

    }

    /**
     * Convert java array of simple strings like: "c:TS" , "c:S"  into an appropriate CapcoVisibilityString like:
     * <tt>
     * [ { c:"TS" }, { c:"S" }, { c:"U" }, { c:"C" }, { sci:"TK" }, { sci:"SI" }, { sci:"G" }, { sci:"HCS" } ]
     </tt>
     * @param userCapcoVisibilityStrings
     * @return
     */
    public static String convertJavaToEncodeCapcoVisibility(List<String> userCapcoVisibilityStrings) {

        StringBuilder stringBuilder = new StringBuilder();

        boolean first = true;
        stringBuilder.append("[ ");
        userCapcoVisibilityStrings = recusivelyExpandCapcoVisibility(userCapcoVisibilityStrings);
        if (userCapcoVisibilityStrings != null) {
            for (String s : userCapcoVisibilityStrings) {
                final String[] splitTerms = s.split(":");          // takes a term like c:TS
                if (!first) {
                    stringBuilder.append(", ");
                }
                first = false;
                stringBuilder.append(String.format("{ %s:\"%s\" }", splitTerms[0], splitTerms[1]));
            }
        }
        stringBuilder.append(" ]");

        return stringBuilder.toString();

    }

    /** Recursively Expand Capco Visibility, as c:TS implies c:S etc
     *
     * @param userCapcoVisibilityStrings that has only high level user CAPCO e.g. c:TS elements and may not have
     *        lower levels
     * @return  all user CAPCO entries with duplicates suppressed.
     */
    private static List<String> recusivelyExpandCapcoVisibility(List<String> userCapcoVisibilityStrings) {
        if (userCapcoVisibilityStrings == null) return null;

        HashSet<String> capco = new LinkedHashSet<String>();
        if (EXPAND_CAPCO_AS_TREE_OF_VISIBILITY) {

            for (String c : userCapcoVisibilityStrings) {

                if ("c:TS".equalsIgnoreCase(c)) {
                    capco.add("c:TS");
                    capco.add("c:S");
                    capco.add("c:C");
                    capco.add("c:U");
                } else if ("c:S".equalsIgnoreCase(c)) {
                    capco.add("c:S");
                    capco.add("c:C");
                    capco.add("c:U");
                } else if ("c:C".equalsIgnoreCase(c)) {
                    capco.add("c:C");
                    capco.add("c:U");
                } else if ("c:U".equalsIgnoreCase(c)) {
                    capco.add("c:U");
                } else {
                    capco.add(c);
                }
            }
        } else {
            capco.addAll(userCapcoVisibilityStrings);
        }
        return new ArrayList<String>( capco );
    }

}
