package winterwell.jtwitter;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import winterwell.jtwitter.Twitter.ITweet;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.Twitter.User;
import winterwell.utils.StrUtils;
import winterwell.utils.Utils;

public class UserStreamTest {

	@Test
	public void testRead() {
		Twitter jtwit = TwitterTest.newTestTwitter();
		UserStream us = new UserStream(jtwit.getHttpClient());
		us.setPreviousCount(100);
		us.setWithFollowings(false); // no need to hear what JTwitTest2 has to say
		// -- unless it's too us
		us.connect();
		// Do some stuff		
		int salt = new Random().nextInt(1000);
		Twitter jtwit2 = TwitterTest.newTestTwitter2();
		if ( ! jtwit.isFollowing(jtwit2.getScreenName())) {
			jtwit.follow(jtwit2.getScreenName());
		}
		if ( ! jtwit2.isFollowing("jtwit")) {
			jtwit2.follow("jtwit");
		}
		jtwit2.setStatus("Public hello to @jtwit "+salt);
		jtwit2.setStatus("Public shout about monkeys: they're cute "+salt);
		jtwit2.sendMessage("jtwit", "Private hello to jtwit "+salt);
		
		jtwit.setStatus("@jtwittest2 Public hello from jtwit "+salt);
		jtwit.sendMessage("jtwittest2", "Private hello from jtwit "+salt);
		
		// Favorite
//		List<Status> ht = jtwit.getHomeTimeline();
//		Status s = ht.get(0);
//		jtwit.setFavorite(s, ! s.isFavorite());
				
		TwitterAccount ta = new TwitterAccount(jtwit);
//		ta.setProfile(null, null,
//				new String[]{
//				"UK", "Edinburgh", "I is in your twitters LOL", "Scotland"
//				}[(int)(System.currentTimeMillis() % 4)], null);
		Utils.sleep(2000);		
//		TwitterList tl = new TwitterList("StreamTest"+new Random().nextInt(1000), jtwit, true, "Just a test list");
//		tl.add(new User("winterstein"));
//		Utils.sleep(4000);
//		tl.remove(new User("winterstein"));		
		if (jtwit.isFollowing("winterstein")) {
			jtwit.stopFollowing("winterstein");
		} else {
			jtwit.follow("winterstein");
		}
		Utils.sleep(2000);
//		tl.delete();

		List<TwitterEvent> sys = us.popSystemEvents();
		List<ITweet> tweets = us.popTweets();
		List<TwitterEvent> evs = us.popEvents();
		if ( ! tweets.isEmpty()) System.out.println(StrUtils.join(tweets,"\n"));
		if ( ! evs.isEmpty()) System.out.println(evs);
		
		us.close();
	}

}
