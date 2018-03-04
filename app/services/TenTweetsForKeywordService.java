package services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

import models.Tweet;

@Singleton
public class TenTweetsForKeywordService {

	public Map<String, List<Tweet>> getTenTweetsForKeyword(String searchString) {
		Map<String, List<Tweet>> result = new HashMap<>();

		for (String word : searchString.split(" ")) {

			Tweet t1 = new Tweet();
			t1.setCreated_at("Some Date for Word \""+ word +"\" tweet 1 like 2018 03 03");
			t1.setText("Some text for Word \""+ word +"\" tweet 1");
			Tweet t2 = new Tweet();
			t2.setCreated_at("Some Date for Word \""+ word +"\" tweet 2 like 2018 03 03");
			t2.setText("Some text for Word \""+ word +"\" tweet 2");
			result.put(word, Arrays.asList(new Tweet[] { t1, t2 }));
		}
		return result;
	}
}
