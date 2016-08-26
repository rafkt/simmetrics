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

@SuppressWarnings("javadoc")
public abstract class CodePointDistanceTest extends DistanceTest<int[]> {

	protected static final class T extends TestCase<int[]> {
		public T(float similarity, String a, String b) {
			super(similarity, toCodePoints(a), toCodePoints(b));
		}

		private static int[] toCodePoints(String a) {

			int codePointCount = Character.codePointCount(a, 0, a.length());
			int[] codePoints = new int[codePointCount];
			int n = 0;
			for (int offset = 0; offset < a.length();) {
				int codePoint = a.codePointAt(offset);
				codePoints[n++] = codePoint;
				offset += Character.charCount(codePoint);
			}

			return codePoints;
		}
	}

	@Override
	protected abstract T[] getTests();

	@Override
	protected final int[] getEmpty() {
		return new int[0];
	}
}
