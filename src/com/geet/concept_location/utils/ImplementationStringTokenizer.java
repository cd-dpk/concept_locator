package com.geet.concept_location.utils;
import java.util.StringTokenizer;
import com.geet.concept_location.corpus_creation.JavaLanguage;
public class ImplementationStringTokenizer extends StringTokenizer {
	public ImplementationStringTokenizer(String str, String delim,
			boolean returnDelims) {
		super(str, delim, returnDelims);
	}
	public ImplementationStringTokenizer(String str, String delim) {
		super(str, delim);
	}
	public ImplementationStringTokenizer(String str) {
		super(str);
	}
	@Override
	public String nextToken() {
		String largeToken = "";
		String token = super.nextToken();
		// if token has a substring of programming syntax then replace with " "
		// if token has a substring of operators then replace with " "
		StringTokenizer stringTokenizer = new StringTokenizer(token,
				JavaLanguage.getProgrammingLanguageSyntax()
						+ JavaLanguage.getOperators()
						, false);
		while (stringTokenizer.hasMoreTokens()) {
			String nestedToken = stringTokenizer.nextToken();
			// if token is equal to any keywords, or operators then replace with
			// " "
			if (StringUtils.hasStringInList(nestedToken, JavaLanguage.getKeywords())
					|| StringUtils.hasStringInList(nestedToken,
							JavaLanguage.getOperatorsContainedOnlyChar())
					|| StringUtils.hasStringInList(nestedToken,
							JavaLanguage.getLiterals())) {
			} else {
				// get identifier separation
				largeToken += StringUtils
						.getIdentifierSeparationsWithCamelCase(nestedToken)
						+ " ";
			}
		}
		return largeToken;
		// return super.nextToken();
	}
}
