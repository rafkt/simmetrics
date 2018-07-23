/*
 * #%L
 * Simmetrics Examples
 * %%
 * Copyright (C) 2014 - 2018 Simmetrics Authors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * #L%
 */
package org.simmetrics.example;

import static java.util.Arrays.asList;

import java.util.HashSet;
import java.util.*;
import java.util.Locale;
import java.util.Set;

import org.simmetrics.SetMetric;
import org.simmetrics.StringDistance;
import org.simmetrics.StringMetric;
import org.simmetrics.builders.StringDistanceBuilder;
import org.simmetrics.builders.StringMetricBuilder;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.metrics.EuclideanDistance;
import org.simmetrics.metrics.OverlapCoefficient;
import org.simmetrics.metrics.StringMetrics;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;

import org.simmetrics.metrics.SmithWaterman;
import org.simmetrics.metrics.SmithWatermanSetMetric;

/**
 * Examples from README.md
 */
@SuppressWarnings("javadoc")
public final class ReadMeExample {
	public static float example01() {

		String str1 = "This is a sentence. It is made of words";
		String str2 = "This sentence is similar. It has almost the same words";

		StringMetric metric = StringMetrics.cosineSimilarity();

		float result = metric.compare(str1, str2); // 0.4767

		return result;
	}

	public static float example02() {

		String str1 = "This is a sentence. It is made of words";
		String str2 = "This sentence is similar. It has almost the same words";

		StringMetric metric = StringMetricBuilder
				.with(new CosineSimilarity<String>())
				.simplify(Simplifiers.toLowerCase(Locale.ENGLISH))
				.simplify(Simplifiers.replaceNonWord())
				.tokenize(Tokenizers.whitespace()).build();

		float result = metric.compare(str1, str2); // 0.5720

		return result;
	}

	public static float example03() {

		String str1 = "This is a sentence. It is made of words";
		String str2 = "This sentence is similar. It has almost the same words";

		StringDistance metric = StringDistanceBuilder
				.with(new EuclideanDistance<String>())
				.simplify(Simplifiers.toLowerCase(Locale.ENGLISH))
				.simplify(Simplifiers.replaceNonWord())
				.tokenize(Tokenizers.whitespace()).build();

		float result = metric.distance(str1, str2); // 3.0000

		return result;
	}

	public static float example04() {

		Set<Integer> scores1 = new HashSet<>(asList(1, 1, 2, 3, 5, 8, 11, 19));
		Set<Integer> scores2 = new HashSet<>(asList(1, 2, 4, 8, 16, 32, 64));

		SetMetric<Integer> metric = new OverlapCoefficient<>();

		float result = metric.compare(scores1, scores2); // 0.4285

		return result;
	}

	public static void main(String[] args){
		System.out.println("TEEEEEEST");

		List<Integer> scores1 = new ArrayList<>(asList(1,9,9, 9,3,1,5, 9, 3));
		List<Integer> scores2 = new ArrayList<>(asList(4, 4, 9, 4));

		System.out.println(scores1);

		SmithWatermanSetMetric<Integer> swSet = new SmithWatermanSetMetric<>();
		SmithWaterman sw = new SmithWaterman();
		System.out.println(swSet.compare(scores1, scores2));
		System.out.println(swSet.getFirstLocalIndex());
		System.out.println(swSet.getSecondLocalIndex());

		System.out.println(sw.compare("axxxbxxxabxxxxa", "abc"));
	}

}
