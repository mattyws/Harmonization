package br.ufmt.harmonizacao.custom;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.Version;

// CondenseFilter code was not analyzed yet, i take from a stack overflow answer
//Link : http://stackoverflow.com/questions/7384016/whats-wrong-with-this-lucene-tokenfilter

/**
 * CondenseFilter will put all tokens together and will create a acronym if : ->
 * The token has a lenght more then 1 -> The acronym has a lenght more then 2
 * 
 * @author mattyws
 * 
 */
public class CondenseTokenFilter extends TokenFilter {

	private final StringBuilder sb = new StringBuilder();
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private boolean consumed; // true if we already consumed

	protected CondenseTokenFilter(TokenStream input) {
		super(input);
	}

	@Override
	public final boolean incrementToken() throws IOException {
		if (consumed) {
			return false; // don't call input.incrementToken() after it returns
							// false
		}
		consumed = true;

		int startOffset = 0;
		int endOffset = 0;

		boolean found = false; // true if we actually consumed any tokens
		while (input.incrementToken()) {
			if (!found) {
				startOffset = offsetAtt.startOffset();
				found = true;
			}
			sb.append(termAtt);
			sb.append("_");
			endOffset = offsetAtt.endOffset();
		}

		if (found) {
			clearAttributes();
			String acronym = "";
			String name = sb.toString().replaceAll("[^A-Za-z0-9_]", "");
			if (name.length() > 0) {
				name = name.substring(0, name.length() - 1);

				String[] words = name.split("_");
				for (String word : words) {
					if (!word.isEmpty() && word.length() > 3) {
						acronym = acronym + word.charAt(0);
					}
				}
				//if (acronym.length() > 2) {
					//name = acronym + " " + name;
				//}
				name = name.replaceAll("_", "");
				sb.delete(0, sb.length());
				sb.append(name);
				termAtt.setEmpty();	
				if(acronym.length() > 2) {
					termAtt.append(acronym+" ");
				}
				termAtt.append(name);
				offsetAtt.setOffset(startOffset, endOffset);;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		sb.setLength(0);
		consumed = false;
	}

}
