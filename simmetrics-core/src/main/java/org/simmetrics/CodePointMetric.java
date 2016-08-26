/*-
 * #%L
 * Simmetrics Core
 * %%
 * Copyright (C) 2014 - 2016 Simmetrics Authors
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

package org.simmetrics;

/**
 * Measures the similarity between two Unicode code point arrays. The
 * measurement results in a value between 0 and 1 (inclusive). A value of zero
 * indicates that the strings are dissimilar, a a value of 1 indicates they are
 * similar.
 * <p>
 * The similarity measure should be consistent with equals such that
 * {@code a.equals(b) => compare(a,b) == 1.0}.
 * <p>
 * The similarity measure should be reflexive such that
 * {@code compare(a,a) == 1.0}.
 * <p>
 * The similarity measure should be symmetric such that
 * {@code compare(a,b) == compare(b,a)}.
 * 
 */

public interface CodePointMetric extends Metric<int[]> {
	/**
	 * Measures the similarity between Unicode code point arrays a and b. The
	 * measurement results in a value between 0 and 1 (inclusive). A value of
	 * zero indicates that the strings are dissimilar, a value of 1 indicates
	 * they are similar.
	 * 
	 * @param a
	 *            array a to compare
	 * @param b
	 *            array b to compare
	 * @return a value between 0 and 1 inclusive indicating similarity
	 * @throws NullPointerException
	 *             when either a or b is null
	 */
	@Override
	public float compare(int[] a, int[] b);

}
