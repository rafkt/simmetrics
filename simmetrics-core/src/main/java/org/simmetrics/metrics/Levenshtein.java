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

package org.simmetrics.metrics;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.max;
import static org.simmetrics.metrics.Math.min;

import org.simmetrics.*;

import java.util.Arrays;
import java.util.List;

/**
 * Calculates the Levenshtein distance and similarity over two strings.
 * <p>
 * Insert/delete and substitute operations can be weighted. When the cost for
 * substitution is zero Levenshtein does not satisfy the coincidence property.
 * <p>
 * This class is immutable and thread-safe.
 *
 * @see <a href=" http://en.wikipedia.org/wiki/Levenshtein_distance">Wikipedia -
 * Levenshtein distance</a>
 * @see DamerauLevenshtein
 */
public final class Levenshtein implements StringMetric, StringDistance {

	private final ForStrings impl;

	/**
	 * Constructs a new Levenshtein metric.
	 *
	 * @deprecated use {@link Levenshtein#forStrings()}
	 */
	@Deprecated
	public Levenshtein() {
		this(1.0f, 1.0f);
	}

	/**
	 * Constructs a new weighted Levenshtein metric. When the cost for
	 * substitution is zero Levenshtein does not satisfy the coincidence
	 * property.
	 *
	 * @param insertDelete positive non-zero cost of an insert or deletion operation
	 * @param substitute   positive cost of a substitute operation
	 * @deprecated use
	 * {@link Levenshtein#forStrings(float, float)}
	 */
	@Deprecated
	public Levenshtein(float insertDelete, float substitute) {
		impl = new ForStrings(insertDelete, substitute);
	}

	/**
	 * Returns a Levenshtein distance and similarity measure between
	 * Unicode code point arrays.
	 *
	 * @return a Levenshtein metric
	 */
	public static ForCodePoints forCodePoints() {
		return forCodePoints(1.0f, 1.0f);
	}

	/**
	 * Returns a weighted Levenshtein distance and similarity measure
	 * between Unicode code point arrays. When the cost for substitution and/or
	 * transposition are zero Levenshtein does not satisfy the
	 * coincidence property.
	 *
	 * @param insertDelete positive non-zero cost of an insert or deletion operation
	 * @param substitute   positive cost of a substitute operation
	 * @return weighted Levenshtein metric
	 */
	public static ForCodePoints forCodePoints(float insertDelete, float substitute) {
		return new ForCodePoints(insertDelete, substitute);
	}

	/**
	 * Returns a Levenshtein distance and similarity measure between
	 * lists.
	 *
	 * @return a Levenshtein metric
	 */
	public static <T> ForLists<T> forLists() {
		return forLists(1.0f, 1.0f);
	}

	/**
	 * Returns a weighted Levenshtein distance and similarity measure
	 * between lists. When the cost for substitution and/or transposition are
	 * zero Levenshtein does not satisfy the coincidence property.
	 *
	 * @param insertDelete positive non-zero cost of an insert or deletion operation
	 * @param substitute   positive cost of a substitute operation
	 * @return weighted Levenshtein metric
	 */
	public static <T> ForLists<T> forLists(float insertDelete, float substitute) {
		return new ForLists<>(insertDelete, substitute);
	}

	/**
	 * Returns a Levenshtein distance and similarity measure between
	 * strings.
	 * <p>
	 * Strings are compared using character values. To correctly compare strings
	 * containing surrogate pairs use {@link Levenshtein#forCodePoints()}
	 *
	 * @return a Levenshtein metric
	 */
	public static ForStrings forStrings() {
		return forStrings(1.0f, 1.0f);
	}

	/**
	 * Returns a weighted Levenshtein distance and similarity measure
	 * between strings. When the cost for substitution and/or transposition are
	 * zero Levenshtein does not satisfy the coincidence property.
	 * <p>
	 * Strings are compared using character values. To correctly compare strings
	 * containing surrogate pairs use
	 * {@link Levenshtein#forCodePoints(float, float)}
	 *
	 * @param insertDelete positive non-zero cost of an insert or deletion operation
	 * @param substitute   positive cost of a substitute operation
	 * @return weighted Levenshtein metric
	 */
	public static ForStrings forStrings(float insertDelete, float substitute) {
		return new ForStrings(insertDelete, substitute);
	}

	@Override
	public float compare(String a, String b) {
		return impl.compare(a, b);
	}

	@Override
	public float distance(String a, String b) {
		return impl.distance(a, b);
	}

	@Override
	public String toString() {
		return impl.toString();
	}

	/**
	 * Calculates the Levenshtein similarity and distance measure
	 * between Unicode code points arrays.
	 */
	public static final class ForCodePoints extends Impl
			implements CodePointMetric, CodePointDistance {

		ForCodePoints(float insertDelete, float substitute) {
			super(insertDelete, substitute);
		}

		@Override
		public float compare(int[] a, int[] b) {
			return compareCodePoint(a, b);
		}

		@Override
		public float distance(int[] a, int[] b) {
			return distanceCodePoint(a, b);
		}
	}

	/**
	 * Calculates the Levenshtein similarity and distance measure
	 * between lists.
	 * <p>
	 * The elements in the lists have to implement {@link Object#hashCode()} and
	 * {@link Object#equals(Object)}.
	 *
	 * @param <T> the type of elements contained in the lists
	 */
	public static final class ForLists<T> extends Impl
			implements ListMetric<T>, ListDistance<T> {

		ForLists(float insertDelete, float substitute) {
			super(insertDelete, substitute);
		}

		@Override
		public float compare(List<T> a, List<T> b) {
			return compareLists(a, b);
		}

		@Override
		public float distance(List<T> a, List<T> b) {
			return distanceLists(a, b);
		}

	}

	/**
	 * Calculates the Levenshtein similarity and distance measure
	 * between strings.
	 */
	public static final class ForStrings extends Impl
			implements StringMetric, StringDistance {

		ForStrings(float insertDelete, float substitute) {
			super(insertDelete, substitute);
		}

		@Override
		public float compare(String a, String b) {
			return compareStrings(a, b);
		}

		@Override
		public float distance(String a, String b) {
			return distanceStrings(a, b);
		}

	}

	private static abstract class Impl {

		private final float maxCost;
		private final float insertDelete;
		private final float substitute;

		Impl(float insertDelete, float substitute) {
			checkArgument(insertDelete > 0);
			checkArgument(substitute >= 0);
			this.maxCost = max(insertDelete, substitute);
			this.insertDelete = insertDelete;
			this.substitute = substitute;
		}

		float compareStrings(final String a, final String b) {
			if (a.isEmpty() && b.isEmpty()) {
				return 1.0f;
			}

			return 1.0f - (distanceStrings(a, b) / (maxCost * max(a.length(), b.length())));
		}

		<T> float compareLists(final List<T> a, final List<T> b) {
			if (a.isEmpty() && b.isEmpty()) {
				return 1.0f;
			}

			return 1.0f - (distanceLists(a, b) / (maxCost * max(a.size(), b.size())));
		}

		<T> float distanceLists(final List<T> s, final List<T> t) {

			if (s.isEmpty())
				return t.size();
			if (t.isEmpty())
				return s.size();
			if (s.equals(t))
				return 0;

			final int tLength = t.size();
			final int sLength = s.size();

			float[] swap;
			float[] v0 = new float[tLength + 1];
			float[] v1 = new float[tLength + 1];

			// initialize v0 (the previous row of distances)
			// this row is A[0][i]: edit distance for an empty s
			// the distance is just the number of characters to delete from t
			for (int i = 0; i < v0.length; i++) {
				v0[i] = i * insertDelete;
			}

			for (int i = 0; i < sLength; i++) {

				// first element of v1 is A[i+1][0]
				// edit distance is delete (i+1) chars from s to match empty t
				v1[0] = (i + 1) * insertDelete;

				for (int j = 0; j < tLength; j++) {
					v1[j + 1] = min(v1[j] + insertDelete,
							v0[j + 1] + insertDelete,
							v0[j]
									+ (s.get(i) == t.get(j) ? 0.0f
									: substitute));
				}

				swap = v0;
				v0 = v1;
				v1 = swap;
			}

			// latest results was in v1 which was swapped with v0
			return v0[tLength];
		}

		float compareCodePoint(final int[] a, final int[] b) {
			if (a.length == 0 && b.length == 0) {
				return 1.0f;
			}

			return 1.0f - (distanceCodePoint(a, b) / (maxCost * max(a.length, b.length)));
		}

		float distanceCodePoint(final int[] s, final int[] t) {

			if (s.length == 0)
				return t.length;
			if (t.length == 0)
				return s.length;
			if (Arrays.equals(s, t))
				return 0;

			final int tLength = t.length;
			final int sLength = s.length;

			float[] swap;
			float[] v0 = new float[tLength + 1];
			float[] v1 = new float[tLength + 1];

			// initialize v0 (the previous row of distances)
			// this row is A[0][i]: edit distance for an empty s
			// the distance is just the number of characters to delete from t
			for (int i = 0; i < v0.length; i++) {
				v0[i] = i * insertDelete;
			}

			for (int i = 0; i < sLength; i++) {

				// first element of v1 is A[i+1][0]
				// edit distance is delete (i+1) chars from s to match empty t
				v1[0] = (i + 1) * insertDelete;

				for (int j = 0; j < tLength; j++) {
					v1[j + 1] = min(v1[j] + insertDelete,
							v0[j + 1] + insertDelete,
							v0[j]
									+ (s[i] == t[j] ? 0.0f
									: substitute));
				}

				swap = v0;
				v0 = v1;
				v1 = swap;
			}

			// latest results was in v1 which was swapped with v0
			return v0[tLength];
		}

		float distanceStrings(final String s, final String t) {

			if (s.isEmpty())
				return t.length();
			if (t.isEmpty())
				return s.length();
			if (s.equals(t))
				return 0;

			final int tLength = t.length();
			final int sLength = s.length();

			float[] swap;
			float[] v0 = new float[tLength + 1];
			float[] v1 = new float[tLength + 1];

			// initialize v0 (the previous row of distances)
			// this row is A[0][i]: edit distance for an empty s
			// the distance is just the number of characters to delete from t
			for (int i = 0; i < v0.length; i++) {
				v0[i] = i * insertDelete;
			}

			for (int i = 0; i < sLength; i++) {

				// first element of v1 is A[i+1][0]
				// edit distance is delete (i+1) chars from s to match empty t
				v1[0] = (i + 1) * insertDelete;

				for (int j = 0; j < tLength; j++) {
					v1[j + 1] = min(v1[j] + insertDelete,
							v0[j + 1] + insertDelete,
							v0[j]
									+ (s.charAt(i) == t.charAt(j) ? 0.0f
									: substitute));
				}

				swap = v0;
				v0 = v1;
				v1 = swap;
			}

			// latest results was in v1 which was swapped with v0
			return v0[tLength];
		}

		@Override
		public String toString() {
			return "Levenshtein [insertDelete=" + insertDelete + ", substitute="
					+ substitute + "]";
		}

	}

}