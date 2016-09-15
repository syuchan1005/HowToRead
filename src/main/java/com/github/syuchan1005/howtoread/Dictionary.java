package com.github.syuchan1005.howtoread;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

/**
 * Created by syuchan on 2016/09/15.
 */
public class Dictionary {
	public static Tokenizer tokenizer = new Tokenizer();

	public static String tokenize(String text) {
		String text1 = "";
		for (Token token : tokenizer.tokenize(text)) {
			text1 += token.getAllFeaturesArray()[7];
		}
		return HiraToKana(text1);
	}

	public static String HiraToKana(String s) {
		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (c >= 'ァ' && c <= 'ン') {
				sb.setCharAt(i, (char)(c - 'ァ' + 'ぁ'));
			} else if (c == 'ヵ') {
				sb.setCharAt(i, 'か');
			} else if (c == 'ヶ') {
				sb.setCharAt(i, 'け');
			} else if (c == 'ヴ') {
				sb.setCharAt(i, 'う');
				sb.insert(i + 1, '゛');
				i++;
			}
		}
		return sb.toString();
	}
}
