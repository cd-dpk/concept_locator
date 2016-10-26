package com.geet.concept_location.corpus_creation;
import java.util.List;
import com.geet.concept_location.indexing_vsm.Term;
import com.geet.concept_location.query_formulation.QueryManager;
public class QueryDocument extends Document{
	String query;
	public QueryDocument(String query){
		super();
		this.query = query;
	}
	@Override
	public String getArticle() {
		return new QueryManager(query).getProcessedQuery();
	}
	@Override
	public List<Term> getTerms() {
		// TODO Auto-generated method stub
		return super.getTerms();
	}
}
