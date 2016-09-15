package com.github.syuchan1005.howtoread;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collector;

/**
 * Created by syuchan on 2016/03/05.
 */
public class Twitters {
	public static Scanner scanner = new Scanner(System.in);
	private Twitter twitter;
	private TwitterStream twitterStream;

	public Twitters(String ConsumerKey, String ConsumerSecret) throws TwitterException {
		createInstance(ConsumerKey, ConsumerSecret, null);
	}

	public Twitters(String ConsumerKey, String ConsumerSecret, String AccessToken, String AccessTokenSecret) throws TwitterException {
		this.createInstance(ConsumerKey, ConsumerSecret, new AccessToken(AccessToken, AccessTokenSecret));
	}

	public Twitters(String ConsumerKey, String ConsumerSecret, AccessToken accessToken) throws TwitterException {
		this.createInstance(ConsumerKey, ConsumerSecret, accessToken);
	}

	private Twitters createInstance(String ConsumerKey, String ConsumerSecret, AccessToken accessToken) throws TwitterException {
		this.twitter = new TwitterFactory().getInstance();
		this.twitterStream = new TwitterStreamFactory().getInstance();
		this.twitter.setOAuthConsumer(ConsumerKey, ConsumerSecret);
		this.twitterStream.setOAuthConsumer(ConsumerKey, ConsumerSecret);
		if (accessToken == null) {
			RequestToken requestToken = this.twitter.getOAuthRequestToken();
			while (null == accessToken) {
				System.out.println("Open the following URL and grant access to your account:");
				System.out.println(requestToken.getAuthorizationURL());
				System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
				String pin = scanner.nextLine();
				if (pin.length() > 0) {
					accessToken = this.twitter.getOAuthAccessToken(requestToken, pin);
				} else {
					accessToken = this.twitter.getOAuthAccessToken();
				}
			}
		}
		this.twitter.setOAuthAccessToken(accessToken);
		this.twitterStream.setOAuthAccessToken(accessToken);
		return this;
	}

	public Twitter getTwitter() {
		return this.twitter;
	}

	public TwitterStream getTwitterStream() {
		return this.twitterStream;
	}

	public void sendReply(String message, long tweetId) {
		try {
			this.Update(new StatusUpdate(message).inReplyToStatusId(tweetId));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ツイートできませんでした");
		}
	}

	public void tweet(String message) {
		try {
			this.Update(new StatusUpdate(message));
		} catch (Exception e) {
			System.out.println("ツイートできませんでした");
		}
	}

	public void sendReply(String message, long tweetId, File... picture) {
		try {
			if (picture.length > 5) {
				System.out.println("写真は5枚までです。");
				return;
			}
			long[] mediaIDs = new long[5];
			for (int i = 0; i < mediaIDs.length; i++) {
				if (picture[i] != null) mediaIDs[i] = twitter.uploadMedia(picture[i]).getMediaId();
			}
			StatusUpdate update = new StatusUpdate(message);
			update.setMediaIds(mediaIDs);
			this.Update(update.inReplyToStatusId(tweetId));
		} catch (Exception e) {
			System.out.println("ツイートできませんでした");
		}
	}

	public void tweet(String message, File... picture) {
		try {
			if (picture.length > 5) {
				System.out.println("写真は5枚までです。");
				return;
			}
			long[] mediaIDs = new long[5];
			for (int i = 0; i < mediaIDs.length; i++) {
				if (picture[i] != null) mediaIDs[i] = twitter.uploadMedia(picture[i]).getMediaId();
			}
			StatusUpdate update = new StatusUpdate(message);
			update.setMediaIds(mediaIDs);
			this.Update(update);
		} catch (Exception e) {
			System.out.println("ツイートできませんでした");
		}
	}

	public void sendReply(String msg, long tweetId, Map<String, InputStream> picture) {
		try {
			if (picture.size() > 5) {
				System.out.println("写真は5枚までです。");
				return;
			}
			List<Long> mediaIDs = new ArrayList<Long>();
			for (Map.Entry<String, InputStream> e : picture.entrySet())
				mediaIDs.add(twitter.uploadMedia(e.getKey(), e.getValue()).getMediaId());
			StatusUpdate update = new StatusUpdate(msg);
			update.setMediaIds(tolong(mediaIDs));
			update.inReplyToStatusId(tweetId);
			this.Update(update);
		} catch (Exception e) {
			System.out.println("ツイートできませんでした");
		}
	}

	public void tweet(String msg, Map<String, InputStream> picture) {
		try {
			if (picture.size() > 5) {
				System.out.println("写真は5枚までです。");
				return;
			}
			List<Long> mediaIDs = new ArrayList<Long>();
			for (Map.Entry<String, InputStream> e : picture.entrySet())
				mediaIDs.add(twitter.uploadMedia(e.getKey(), e.getValue()).getMediaId());
			if (msg.length() > 140) msg = msg.substring(0, 130) + "...";
			StatusUpdate update = new StatusUpdate(msg);
			update.setMediaIds(tolong(mediaIDs));
			this.Update(update);
		} catch (Exception e) {
			System.out.println("ツイートできませんでした");
		}
	}

	private static long[] tolong(List<Long> l) {
		long[] list = new long[l.size()];
		for (int i = 0; i < l.size(); i++) list[i] = l.get(i);
		return list;
	}

	private void Update(final StatusUpdate update) {
		new Thread() {
			@Override
			public void run() {
				try {
					twitter.updateStatus(update);
				} catch (TwitterException e) {
					e.printStackTrace();
					System.out.println("ツイートできませんでした");
				}
			}
		}.start();
	}

}
