package br.ufmt.harmonizacao.custom;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public final class WhitespaceTokenFilter extends TokenFilter {

	private char[] curTermBuffer;

	private int curTermLength;

	private int currentOffset;

	private int baseOffset;

	private CharTermAttribute termAtt;

	private OffsetAttribute offsetAtt;

	protected WhitespaceTokenFilter(TokenStream input) {
		super(input);
		this.termAtt = addAttribute(CharTermAttribute.class);
		this.offsetAtt = addAttribute(OffsetAttribute.class);
	}

	@Override
	public final boolean incrementToken() throws IOException {
		while (true) {
			if (curTermBuffer == null) {
				if (!input.incrementToken()) {
					return false;
				} else {
					curTermBuffer = (char[]) termAtt.buffer().clone();
					curTermLength = termAtt.length();
					currentOffset = 0;
					baseOffset = offsetAtt.startOffset();
				}
			}
			if (currentOffset < curTermLength) {
				for (int i = currentOffset; i < curTermLength - 1; i++) {

					if (Character.isWhitespace(curTermBuffer[i])) {
						int start = currentOffset;
						int end = i;
						offsetAtt.setOffset(baseOffset + start, baseOffset
								+ end);
						termAtt.copyBuffer(curTermBuffer, start, end - start);
						currentOffset = i + 1;
						return true;
					}
				}
				if (currentOffset < curTermLength) {
					int start = currentOffset;
					int end = curTermLength;
					offsetAtt.setOffset(baseOffset + start, baseOffset + end);
					termAtt.copyBuffer(curTermBuffer, start, end - start);
					currentOffset = curTermLength;
					return true;
				}
			}
			curTermBuffer = null;
		}
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		curTermBuffer = null;
	}
}