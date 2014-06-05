package br.ufmt.harmonizacao.custom;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharacterUtils;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

public final class ExtendedNameFilter extends TokenFilter {
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private PositionIncrementAttribute posIncAttr;
	private OffsetAttribute setOffsetAttr;
	private final int extendedWordCount;

	public ExtendedNameFilter(Version matchVersion, TokenStream in,
			int extendedWordCount) {
		super(in);
		CharacterUtils.getInstance(matchVersion);
		this.extendedWordCount = extendedWordCount;
		this.posIncAttr = addAttribute(PositionIncrementAttribute.class);
		this.setOffsetAttr = addAttribute(OffsetAttribute.class);
	}

	LinkedList<String> list = new LinkedList<String>();
	ArrayList<Integer> startOffsetList = new ArrayList<Integer>();
	int endOffset = 0;
	int count = 0;

	@Override
	public final boolean incrementToken() throws IOException {
		Iterator<String> iterator;
		int len = 0;

		while (input.incrementToken()) {
			list.add(termAtt.toString());
			startOffsetList.add(setOffsetAttr.startOffset());
			endOffset = setOffsetAttr.endOffset();
		}

		iterator = list.iterator();
		len = list.size();

		if (len > 0 && (extendedWordCount < 0 || count < extendedWordCount)) {
			generateToken(iterator);
			return true;
		} else {
			return false;
		}
	}

	public void generateToken(Iterator<String> iterator) {
		termAtt.setEmpty();
		while (iterator.hasNext()) {
			termAtt.append((CharSequence) iterator.next());
		}
		list.removeFirst();
		if (count == 0) {
			posIncAttr.setPositionIncrement(1);
		} else {
			posIncAttr.setPositionIncrement(0);
		}

		setOffsetAttr.setOffset(startOffsetList.get(count), endOffset);
		count++;
	}
}