package br.ufmt.harmonizacao.custom;

import java.io.Reader;

import org.apache.lucene.analysis.ASCIIFoldingFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class PatenteeAnalyzer extends Analyzer {

	public Version matchVersion;
	String[] stopWords = { "" };

	public PatenteeAnalyzer(Version matchVersion) {
		this.matchVersion = matchVersion;
	}

	@Override
	public TokenStream tokenStream(String field, Reader reader) {
		// Iniciate a WhitespaceTokenizer, to remove the stop words in next step
		Tokenizer source = new WhitespaceTokenizer(Version.LUCENE_36, reader);

		/*
		 * This part will : Normalize all characters to lowercase Turn accented
		 * letters to they ASCII equivalents Remove all the stopwords from
		 * source Condense the string TODO: Remove the punctuation characters
		 */
		TokenStream sink =new WhitespaceTokenFilter(new CondenseTokenFilter(
				new CommonDescriptorsTokenFilter(
				new StopFilter(matchVersion,
				new ASCIIFoldingFilter(
						new LowerCaseFilter(matchVersion, source)), StandardAnalyzer.STOP_WORDS_SET) )));

		return sink;
	}

}
