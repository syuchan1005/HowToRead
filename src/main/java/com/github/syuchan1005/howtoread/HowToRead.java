package com.github.syuchan1005.howtoread;

import twitter4j.TwitterException;
import twitter4j.TwitterStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by syuchan on 2016/09/15.
 */
public class HowToRead {
	private static Twitters twitters;

	public static void main(String[] args) {
		twitters = getTwitters();
		if (twitters == null) {
			System.out.println("認証できませんでした");
			return;
		}
		TwitterStream twitterStream = twitters.getTwitterStream();
		twitterStream.addListener(new Listener(twitters));
		twitterStream.user();
	}

	protected static Twitters getTwitters() {
		if (twitters != null) {
			return twitters;
		}
		InputStream stream = HowToRead.class.getClassLoader().getResourceAsStream("api.properties");
		Properties properties = new Properties();
		try {
			properties.load(stream);
			return new Twitters(properties.getProperty("CKey"), properties.getProperty("CSecret"),
					properties.getProperty("AToken"), properties.getProperty("ATokenSec"));
		} catch (IOException | TwitterException e) {
			e.printStackTrace();
		}
		return null;
	}
}
