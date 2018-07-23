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
package org.simmetrics.metrics;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.simmetrics.metrics.Math.max;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.functions.AffineGap;
import org.simmetrics.metrics.functions.Gap;
import org.simmetrics.metrics.functions.MatchMismatchList;
import org.simmetrics.metrics.functions.Substitution;

import org.simmetrics.SetMetric;
import java.util.*;


public final class SmithWatermanSetMetric<T>{
	private final Gap gap;
	private final MatchMismatchList substitution;
	private final int windowSize;
	private int a_local_index, b_local_index;

	public SmithWatermanSetMetric() {
		this(new AffineGap(-5.0f, -1.0f), new MatchMismatchList<T>(5.0f, 0.0f),
				Integer.MAX_VALUE);
	}


	public SmithWatermanSetMetric(Gap gap, MatchMismatchList<T> substitution, int windowSize) {
		checkNotNull(gap);
		checkNotNull(substitution);
		checkArgument(windowSize >= 0);
		this.gap = gap;
		this.substitution = substitution;
		this.windowSize = windowSize;
	}
	
	public float compare(List<T> a, List<T> b) {
		//return 0.0f;
		if (a.isEmpty() && b.isEmpty()) {
			return 1.0f;
		}
		if (a.isEmpty() || b.isEmpty()) {
			return 0.0f;
		}
		float maxDistance = min(a.size(), b.size())
				* max(substitution.max(), gap.min());
		return smithWaterman(a, b) / maxDistance;
	}


	private float smithWaterman(List<T> a, List<T> b) {
		//System.out.println(a + " " + b);
		
		final int n = a.size();
		final int m = b.size();

		final float[][] d = new float[n][m];

		//my code
			int max_i = 0;
			int max_j = 0;
			float old_max = 0;
		//end of my code

		// Initialize corner
		float max = d[0][0] = max(0, substitution.compare(a, 0, b, 0));

		//my code
					if (max > old_max){
						old_max = max;
						max_i = 0;
						max_j = 0;
					}

				//end of my code

		// Initialize edge
		for (int i = 0; i < n; i++) {

			// Find most optimal deletion
			float maxGapCost = 0;
			for (int k = max(1, i - windowSize); k < i; k++) {
				maxGapCost = max(maxGapCost, d[i - k][0] + gap.value(i - k, i));
			}

			d[i][0] = max(0, maxGapCost, substitution.compare(a, i, b, 0));

			max = max(max, d[i][0]);

			//my code
					if (max > old_max){
						old_max = max;
						max_i = i;
						max_j = 0;
					}

				//end of my code

		}

		// Initialize edge
		for (int j = 1; j < m; j++) {

			// Find most optimal insertion
			float maxGapCost = 0;
			for (int k = max(1, j - windowSize); k < j; k++) {
				maxGapCost = max(maxGapCost, d[0][j - k] + gap.value(j - k, j));
			}

			d[0][j] = max(0, maxGapCost, substitution.compare(a, 0, b, j));

			max = max(max, d[0][j]);

			//my code
					if (max > old_max){
						old_max = max;
						max_i = 0;
						max_j = j;
					}

				//end of my code

		}

		// Build matrix
		for (int i = 1; i < n; i++) {

			for (int j = 1; j < m; j++) {

				float maxGapCost = 0;
				// Find most optimal deletion
				for (int k = max(1, i - windowSize); k < i; k++) {
					maxGapCost = max(maxGapCost,
							d[i - k][j] + gap.value(i - k, i));
				}
				// Find most optimal insertion
				for (int k = max(1, j - windowSize); k < j; k++) {
					maxGapCost = max(maxGapCost,
							d[i][j - k] + gap.value(j - k, j));
				}

				// Find most optimal of insertion, deletion and substitution
				d[i][j] = max(0, maxGapCost,
						d[i - 1][j - 1] + substitution.compare(a, i, b, j));

				max = max(max, d[i][j]);
				//my code
					if (max > old_max){
						old_max = max;
						max_i = i;
						max_j = j;
					}

				//end of my code
			}

		}
		//System.out.println(max_i + " " + max_j);
		a_local_index = max_i;
		b_local_index = max_j;
		//System.out.println("Max: " + max);
		return max;

	}

	public int getFirstLocalIndex(){
		return a_local_index;
	}

	public int getSecondLocalIndex(){
		return b_local_index;
	}

	@Override
	public String toString() {
		return "SmithWatermanSetMetric";
	}

}
