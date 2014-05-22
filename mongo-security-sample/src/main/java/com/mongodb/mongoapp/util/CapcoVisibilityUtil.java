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
 *    Overview:
 *    <br/> this makes it easy to encode security settings that you have in systems.
 *    Consider having to encode a Top Secret CAPCO setting, you can call this one call that will expand to
 *    lower levels as necessary:
 *    <tt>
 *        CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS"}) =>
 *           That call in fact under the covers expands to or generates:
 *
 *        [ { c:"TS" }, { c:"S" }, { c:"U" }, { c:"C" } ]   (( in javascript notation which is needed by the
 *                                                             reference FLAC implementation))
 *    </tt>
 *
 *
 * </p>
 *
 * <p> <b>See Examples below for more details:</b>
 * </p>
 * <p>
 *     <tt>
 *     CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS", "c:S"})
 *          note here we deal with S being contained in TS
 *     </tt>
 *     generates:
 *     <br/>
 *     <tt>
 *     "[ { c:\"TS\" }, { c:\"S\" }, { c:\"C\" }, { c:\"U\" } ]"
 *     </tt>
 * </p>
 * <p>
 *     <tt>
 *      CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS",  "sci:TK",  "sci:SI",  "sci:G",  "sci:HCS"})
 *     </tt>
 *     generates:
 *     <br/>
 *     <tt>
 *      "[ { c:\"TS\" }, { c:\"S\" }, { c:\"U\" }, { c:\"C\" }, { sci:\"TK\" }, { sci:\"SI\" }, { sci:\"G\" }, { sci:\"HCS\" } ]";
 *     </tt>
 * </p>
 *
 * <p> The above long strings are an encoded javascript like format that is used by our REDAC engine to efficiently support FLAC.</p>
 *
 */
public class CapcoVisibilityUtil {

    protected static final boolean EXPAND_CAPCO_AS_TREE_OF_VISIBILITY = true;

    /**
     * Convert java array of simple strings like: "c:TS"  into an appropriate CapcoVisibilityString.
     </tt>
     * <p> <b>See Examples below for more details:</b>
     * </p>
     * <p>
     *     <tt>
     *     CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS", "c:S"})
     *         note here we deal with S being contained in TS
     *     </tt>
     *     generates:
     *     <br/>
     *     <tt>
     *     "[ { c:\"TS\" }, { c:\"S\" }, { c:\"C\" }, { c:\"U\" } ]"
     *     </tt>
     * </p>
     * <p>
     *     <tt>
     *      CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS",  "sci:TK",  "sci:SI",  "sci:G",  "sci:HCS"})
     *     </tt>
     *     generates:
     *     <br/>
     *     <tt>
     *      "[ { c:\"TS\" }, { c:\"S\" }, { c:\"U\" }, { c:\"C\" }, { sci:\"TK\" }, { sci:\"SI\" }, { sci:\"G\" }, { sci:\"HCS\" } ]";
     *     </tt>
     * </p>
     *
     * <p> NOTES: we fully support generating lower level of TS S C and U  , for all others you need to expand yourself.</p>
     *
     * @param userCapcoVisibilityStrings
     * @return
     */
    public static String convertJavaToEncodeCapcoVisibility(String[] userCapcoVisibilityStrings) {

        return convertJavaToEncodeCapcoVisibility( ((userCapcoVisibilityStrings == null)
                ? (List<String> ) null
                : Arrays.asList(userCapcoVisibilityStrings)));

    }

    /**
     * Convert java List of simple strings like: "c:TS"  into an appropriate CapcoVisibilityString.
     </tt>
     * <p> <b>See Examples below for more details:</b>
     * </p>
     * <p>
     *     <tt>
     *     CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS", "c:S"})
     *         note here we deal with S being contained in TS
     *     </tt>
     *     generates:
     *     <br/>
     *     <tt>
     *     "[ { c:\"TS\" }, { c:\"S\" }, { c:\"C\" }, { c:\"U\" } ]"
     *     </tt>
     * </p>
     * <p>
     *     <tt>
     *      CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS",  "sci:TK",  "sci:SI",  "sci:G",  "sci:HCS"})
     *     </tt>
     *     generates:
     *     <br/>
     *     <tt>
     *      "[ { c:\"TS\" }, { c:\"S\" }, { c:\"U\" }, { c:\"C\" }, { sci:\"TK\" }, { sci:\"SI\" }, { sci:\"G\" }, { sci:\"HCS\" } ]";
     *     </tt>
     * </p>
     *
     * <p> NOTES: we fully support generating lower level of TS S C and U  , for all others you need to expand yourself.</p>
     *
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

    ////
    // In place of spring, we can role our own ThreadLocal CAPCO Credentials management, if you
    // were not using SPRING security.
    ////
    /*    private static ThreadLocal<String> userCapcoCredentials = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            // lookup user CAPCO level
            String userCAPCO1 = "";
            String userCAPCO2 = "";

            return new String();
        }
    };



    public static String getCurrentUserCapcoCredentials() {
        // this may fetch from SPRING the user credentials or from your DB

        return null;

    } */
}
