package com.geet.concept_location.query_formulation;
import java.util.StringTokenizer;

import com.geet.concept_location.corpus_creation.StopWords;
import com.geet.concept_location.utils.StringUtils;
public class QueryManager {
	private String query ="";
	public QueryManager(String queryArg){
		query = queryArg;
	}
	private void processQuery(){
		/**
		 * process the query
		 */
		// remove all the stop words
		StringTokenizer stringTokenizer = new StringTokenizer(query," ",false);
		String tokens = query;
		while (stringTokenizer.hasMoreTokens()) {
			if (tokens.equals(query)) {
				tokens ="";
			}
			String token = stringTokenizer.nextToken();
			if (!StopWords.isStopword(token)) {
				tokens += token+" ";
			}
		}
		if (!tokens.equals(query)) {
			query = tokens;
		}
	}
	public String getProcessedQuery() {
		processQuery();
		return StringUtils.getIdentifierSeparations(query);
	}
	public static void main(String[] args) {
		System.out.println(new QueryManager("methodLove").getProcessedQuery());
	}
}
