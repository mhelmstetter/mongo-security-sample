package com.mongodb.mongoapp.domain;

import com.mongodb.mongoapp.util.CapcoVisibilityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * user SecurityAttributes needed for redaction pipeline to filter visibility of documents.
 *
 * <p>  This set of user SecurityAttributes is designed to model the federal / intelligence
 *      needs.
 * </p>
 *
 */
public class UserSecurityAttributes {
    private String clearance;
    private List<String> sci;
    private List<String> country;
    private String capcoUserString; // cached user string that encodes the proper security levels, null until computed

    public UserSecurityAttributes(String clearance, List<String> sci, List<String> country) {
        if (clearance == null || clearance.trim().length() == 0) { throw new IllegalArgumentException("clearance must be specified"); }
        this.clearance = clearance.trim();
        this.sci = (sci == null) ? new ArrayList<String>() : sci;
        this.country = (country == null) ? new ArrayList<String>() : country;
    }

    public String getCapcoUserString() {

        if (capcoUserString == null) {

            capcoUserString = generateEncodedCapcoUserAttrString();
        }

        return capcoUserString;
    }

    private String generateEncodedCapcoUserAttrString() {
        List<String> securityAttr = new ArrayList<String>();
        securityAttr.add(String.format("c:%s", this.getClearance()));

        for (String s : this.getSci()) {
            securityAttr.add(String.format("sci:%s", s));
        }

        for (String s : this.getCountry()) {
            securityAttr.add(String.format("relto:%s", s));
        }

        // at this stage:
        //  the encoding strings might look like:  {"c:TS", "sci:TK", "sci:SI", "sci:G", "sci:HCS"}
        // compute it based on settings
        String genCapcoUserString = EncodingUtils.expandCapcoVisibility(securityAttr);

        return genCapcoUserString;
    }

    public String getClearance() {
        return clearance;
    }

    public void setClearance(String clearance) {
        this.clearance = clearance;
    }

    public List<String> getSci() {
        return sci;
    }

    public void setSci(List<String> sci) {
        this.sci = sci;
    }

    public List<String> getCountry() {
        return country;
    }

    public void setCountry(List<String> country) {
        this.country = country;
    }


    static class EncodingUtils {
        /**
         * Convert java array of simple strings like: "c:TS"  into an appropriate CapcoVisibilityString.
         </tt>
         * <p> <b>See Examples below for more details:</b>
         * </p>
         * <p>
         *     <tt>
         *     UserSecurityAttributes.EncodingUtils.expandCapcoVisibility(new String[]{"c:TS", "c:S"})
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
         *      UserSecurityAttributes.EncodingUtils.expandCapcoVisibility(new String[]{"c:TS",  "sci:TK",  "sci:SI",  "sci:G",  "sci:HCS"})
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
        public static String expandCapcoVisibility(String[] userCapcoVisibilityStrings) {

            return expandCapcoVisibility(((userCapcoVisibilityStrings == null)
                    ? (List<String>) null
                    : Arrays.asList(userCapcoVisibilityStrings)));

        }

        /**
         * Convert java List of simple strings like: "c:TS"  into an appropriate CapcoVisibilityString.
         </tt>
         * <p> <b>See Examples below for more details:</b>
         * </p>
         * <p>
         *     <tt>
         *     UserSecurityAttributes.EncodingUtils.expandCapcoVisibility(new String[]{"c:TS", "c:S"})
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
         *      UserSecurityAttributes.EncodingUtils.expandCapcoVisibility(new String[]{"c:TS",  "sci:TK",  "sci:SI",  "sci:G",  "sci:HCS"})
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
        public static String expandCapcoVisibility(List<String> userCapcoVisibilityStrings) {

            StringBuilder stringBuilder = new StringBuilder();

            boolean first = true;
            stringBuilder.append("[ ");
            userCapcoVisibilityStrings = CapcoVisibilityUtil.recusivelyExpandCapcoVisibility(userCapcoVisibilityStrings);
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

    }
}


