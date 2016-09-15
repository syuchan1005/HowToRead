package com.github.syuchan1005.howtoread;

import twitter4j.Status;
import twitter4j.UserStreamAdapter;

/**
 * Created by syuchan on 2016/09/15.
 */
public class Listener extends UserStreamAdapter {
	public static String userName = "how_to_read";
	private Twitters twitters;

	public Listener(Twitters twitters) {
		this.twitters = twitters;
	}

	@Override
	public void onStatus(Status status) {
		String text = status.getText().replaceAll("\n", " ");
		if (!text.startsWith("@" + userName)) return;
		text = Dictionary.tokenize(text.substring(userName.length() + 2));
		text = "@" + status.getUser().getScreenName() + "\n" + text;
		twitters.sendReply(text, status.getId());
	}
}
