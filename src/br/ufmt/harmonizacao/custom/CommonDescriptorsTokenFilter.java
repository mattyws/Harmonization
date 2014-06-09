package br.ufmt.harmonizacao.custom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

public class CommonDescriptorsTokenFilter extends TokenFilter {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private CommonDescriptorsSet descriptorsSet = new CommonDescriptorsSet(
			"files/descriptors");
	public List<String> tokens = new ArrayList<String>();
	public boolean consumed = false, removed = false, constructed = false;
	int pos, start;
	String text;

	public CommonDescriptorsTokenFilter(TokenStream input) {
		super(input);
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (!consumed) {
			consumed = true;
			while (input.incrementToken()) {
				tokens.add(termAtt.toString());
			}
			pos = 0;
			return true;
		} else {
			if (!removed) {
				while (pos < tokens.size()) {
					if (descriptorsSet.contains(tokens.get(pos))) {
						tokens.remove(pos);
					}
					pos++;
				}
				removed = true;
				return true;
			}
		}
		if (removed) {
			if (!constructed) {
				text = "";
				for (String token : tokens) {
					text = text + token + " ";
				}
				constructed = true;
				text = text.substring(0, text.length()-1);
				termAtt.setEmpty();
				start = 0;
			}
			if (start < text.length() && !text.isEmpty()) {
				int i;
				for (i = start; i < text.length(); i++) {
					if(Character.isWhitespace(text.charAt(i))) {
						offsetAtt.setOffset(start, i);
						termAtt.copyBuffer(text.toCharArray(), start, i - start);
						start = i + 1;
						return true;
					}
				}
				if(i == text.length()) {
					offsetAtt.setOffset(start, i);
					termAtt.copyBuffer(text.toCharArray(), start, i - start);
					start = i + 1;
					return true;
				}
			}
		}
		return false;
	}

}
