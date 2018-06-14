/*
 * #%L
 * Simmetrics Core
 * %%
 * Copyright (C) 2014 - 2018 Simmetrics Authors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.simmetrics.metrics.functions;

import static com.google.common.base.Preconditions.checkArgument;
import java.util.*;

/**
 * A substitution function that assigns one value to equal characters, another
 * value to unequal characters.
 * 
 * <p>
 * This class is immutable and thread-safe.
 *
 */
public class MatchMismatchList<T> {

	private final float matchValue;
	private final float mismatchValue;

	/**
	 * Constructs a new match-mismatch substitution function. When two
	 * characters are equal a score of <code>matchValue</code> is assigned. In
	 * case of a mismatch a score of <code>mismatchValue</code>. The
	 * <code>matchValue</code> must be strictly greater then
	 * <code>mismatchValue</code>
	 * 
	 * @param matchValue
	 *            value when characters are equal
	 * @param mismatchValue
	 *            value when characters are not equal
	 */
	public MatchMismatchList(float matchValue, float mismatchValue) {
		super();
		checkArgument(matchValue > mismatchValue);

		this.matchValue = matchValue;
		this.mismatchValue = mismatchValue;
	}

	
	public float compare(List<T> a, int aIndex, List<T> b, int bIndex) {
		return a.get(aIndex).equals(b.get(bIndex)) ? matchValue
				: mismatchValue;
	}

	public float max() {
		return matchValue;
	}

	public float min() {
		return mismatchValue;
	}

	public String toString() {
		return "MatchMismatch List [matchCost=" + matchValue + ", mismatchCost="
				+ mismatchValue + "]";
	}

}
