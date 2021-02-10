/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
 * 
 * This file is part of Dayflower.
 * 
 * Dayflower is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Dayflower is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dayflower. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dayflower.utility;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;

/**
 * The class {@code Ints} contains methods for performing basic numeric operations.
 * <p>
 * You can think of this class as an extension of {@code Math}. It does what {@code Math} does for the {@code double}, {@code float} and {@code int} types, but for the {@code int} type only. In addition to this it also adds new methods.
 * <p>
 * This class does not contain all methods from {@code Math}. The methods in {@code Math} that deals with the {@code double} type can be found in the class {@link Doubles}. The methods in {@code Math} that deals with the {@code float} type can be found
 * in the class {@link Floats}.
 * <p>
 * The documentation in this class should be comprehensive. But some of the details covered in the documentation of the {@code Math} class may be missing. To get the full documentation for a particular method, you may want to look at the documentation
 * of the corresponding method in the {@code Math} class. This is, of course, only true if the method you're looking at exists in the {@code Math} class.
 * <p>
 * Not all methods in the {@code Math} class that should be added to this class, may have been added yet.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Ints {
	private static final int[] PRIME_NUMBERS = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997, 1009, 1013, 1019, 1021, 1031, 1033, 1039, 1049, 1051, 1061, 1063, 1069, 1087, 1091, 1093, 1097, 1103, 1109, 1117, 1123, 1129, 1151, 1153, 1163, 1171, 1181, 1187, 1193, 1201, 1213, 1217, 1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, 1283, 1289, 1291, 1297, 1301, 1303, 1307, 1319, 1321, 1327, 1361, 1367, 1373, 1381, 1399, 1409, 1423, 1427, 1429, 1433, 1439, 1447, 1451, 1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493, 1499, 1511, 1523, 1531, 1543, 1549, 1553, 1559, 1567, 1571, 1579, 1583, 1597, 1601, 1607, 1609, 1613, 1619, 1621, 1627, 1637, 1657, 1663, 1667, 1669, 1693, 1697, 1699, 1709, 1721, 1723, 1733, 1741, 1747, 1753, 1759, 1777, 1783, 1787, 1789, 1801, 1811, 1823, 1831, 1847, 1861, 1867, 1871, 1873, 1877, 1879, 1889, 1901, 1907, 1913, 1931, 1933, 1949, 1951, 1973, 1979, 1987, 1993, 1997, 1999, 2003, 2011, 2017, 2027, 2029, 2039, 2053, 2063, 2069, 2081, 2083, 2087, 2089, 2099, 2111, 2113, 2129, 2131, 2137, 2141, 2143, 2153, 2161, 2179, 2203, 2207, 2213, 2221, 2237, 2239, 2243, 2251, 2267, 2269, 2273, 2281, 2287, 2293, 2297, 2309, 2311, 2333, 2339, 2341, 2347, 2351, 2357, 2371, 2377, 2381, 2383, 2389, 2393, 2399, 2411, 2417, 2423, 2437, 2441, 2447, 2459, 2467, 2473, 2477, 2503, 2521, 2531, 2539, 2543, 2549, 2551, 2557, 2579, 2591, 2593, 2609, 2617, 2621, 2633, 2647, 2657, 2659, 2663, 2671, 2677, 2683, 2687, 2689, 2693, 2699, 2707, 2711, 2713, 2719, 2729, 2731, 2741, 2749, 2753, 2767, 2777, 2789, 2791, 2797, 2801, 2803, 2819, 2833, 2837, 2843, 2851, 2857, 2861, 2879, 2887, 2897, 2903, 2909, 2917, 2927, 2939, 2953, 2957, 2963, 2969, 2971, 2999, 3001, 3011, 3019, 3023, 3037, 3041, 3049, 3061, 3067, 3079, 3083, 3089, 3109, 3119, 3121, 3137, 3163, 3167, 3169, 3181, 3187, 3191, 3203, 3209, 3217, 3221, 3229, 3251, 3253, 3257, 3259, 3271, 3299, 3301, 3307, 3313, 3319, 3323, 3329, 3331, 3343, 3347, 3359, 3361, 3371, 3373, 3389, 3391, 3407, 3413, 3433, 3449, 3457, 3461, 3463, 3467, 3469, 3491, 3499, 3511, 3517, 3527, 3529, 3533, 3539, 3541, 3547, 3557, 3559, 3571, 3581, 3583, 3593, 3607, 3613, 3617, 3623, 3631, 3637, 3643, 3659, 3671, 3673, 3677, 3691, 3697, 3701, 3709, 3719, 3727, 3733, 3739, 3761, 3767, 3769, 3779, 3793, 3797, 3803, 3821, 3823, 3833, 3847, 3851, 3853, 3863, 3877, 3881, 3889, 3907, 3911, 3917, 3919, 3923, 3929, 3931, 3943, 3947, 3967, 3989, 4001, 4003, 4007, 4013, 4019, 4021, 4027, 4049, 4051, 4057, 4073, 4079, 4091, 4093, 4099, 4111, 4127, 4129, 4133, 4139, 4153, 4157, 4159, 4177, 4201, 4211, 4217, 4219, 4229, 4231, 4241, 4243, 4253, 4259, 4261, 4271, 4273, 4283, 4289, 4297, 4327, 4337, 4339, 4349, 4357, 4363, 4373, 4391, 4397, 4409, 4421, 4423, 4441, 4447, 4451, 4457, 4463, 4481, 4483, 4493, 4507, 4513, 4517, 4519, 4523, 4547, 4549, 4561, 4567, 4583, 4591, 4597, 4603, 4621, 4637, 4639, 4643, 4649, 4651, 4657, 4663, 4673, 4679, 4691, 4703, 4721, 4723, 4729, 4733, 4751, 4759, 4783, 4787, 4789, 4793, 4799, 4801, 4813, 4817, 4831, 4861, 4871, 4877, 4889, 4903, 4909, 4919, 4931, 4933, 4937, 4943, 4951, 4957, 4967, 4969, 4973, 4987, 4993, 4999, 5003, 5009, 5011, 5021, 5023, 5039, 5051, 5059, 5077, 5081, 5087, 5099, 5101, 5107, 5113, 5119, 5147, 5153, 5167, 5171, 5179, 5189, 5197, 5209, 5227, 5231, 5233, 5237, 5261, 5273, 5279, 5281, 5297, 5303, 5309, 5323, 5333, 5347, 5351, 5381, 5387, 5393, 5399, 5407, 5413, 5417, 5419, 5431, 5437, 5441, 5443, 5449, 5471, 5477, 5479, 5483, 5501, 5503, 5507, 5519, 5521, 5527, 5531, 5557, 5563, 5569, 5573, 5581, 5591, 5623, 5639, 5641, 5647, 5651, 5653, 5657, 5659, 5669, 5683, 5689, 5693, 5701, 5711, 5717, 5737, 5741, 5743, 5749, 5779, 5783, 5791, 5801, 5807, 5813, 5821, 5827, 5839, 5843, 5849, 5851, 5857, 5861, 5867, 5869, 5879, 5881, 5897, 5903, 5923, 5927, 5939, 5953, 5981, 5987, 6007, 6011, 6029, 6037, 6043, 6047, 6053, 6067, 6073, 6079, 6089, 6091, 6101, 6113, 6121, 6131, 6133, 6143, 6151, 6163, 6173, 6197, 6199, 6203, 6211, 6217, 6221, 6229, 6247, 6257, 6263, 6269, 6271, 6277, 6287, 6299, 6301, 6311, 6317, 6323, 6329, 6337, 6343, 6353, 6359, 6361, 6367, 6373, 6379, 6389, 6397, 6421, 6427, 6449, 6451, 6469, 6473, 6481, 6491, 6521, 6529, 6547, 6551, 6553, 6563, 6569, 6571, 6577, 6581, 6599, 6607, 6619, 6637, 6653, 6659, 6661, 6673, 6679, 6689, 6691, 6701, 6703, 6709, 6719, 6733, 6737, 6761, 6763, 6779, 6781, 6791, 6793, 6803, 6823, 6827, 6829, 6833, 6841, 6857, 6863, 6869, 6871, 6883, 6899, 6907, 6911, 6917, 6947, 6949, 6959, 6961, 6967, 6971, 6977, 6983, 6991, 6997, 7001, 7013, 7019, 7027, 7039, 7043, 7057, 7069, 7079, 7103, 7109, 7121, 7127, 7129, 7151, 7159, 7177, 7187, 7193, 7207, 7211, 7213, 7219, 7229, 7237, 7243, 7247, 7253, 7283, 7297, 7307, 7309, 7321, 7331, 7333, 7349, 7351, 7369, 7393, 7411, 7417, 7433, 7451, 7457, 7459, 7477, 7481, 7487, 7489, 7499, 7507, 7517, 7523, 7529, 7537, 7541, 7547, 7549, 7559, 7561, 7573, 7577, 7583, 7589, 7591, 7603, 7607, 7621, 7639, 7643, 7649, 7669, 7673, 7681, 7687, 7691, 7699, 7703, 7717, 7723, 7727, 7741, 7753, 7757, 7759, 7789, 7793, 7817, 7823, 7829, 7841, 7853, 7867, 7873, 7877, 7879, 7883, 7901, 7907, 7919, 7927, 7933, 7937, 7949, 7951, 7963, 7993, 8009, 8011, 8017, 8039, 8053, 8059, 8069, 8081, 8087, 8089, 8093, 8101, 8111, 8117, 8123, 8147, 8161};
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Ints() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the absolute version of {@code value}.
	 * <p>
	 * If the argument is not negative, the argument is returned. If the argument is negative, the negation of the argument is returned.
	 * <p>
	 * Note that if the argument is equal to the value of {@code Integer.MIN_VALUE}, the most negative representable {@code int} value, the result is that same value, which is negative.
	 * 
	 * @param value an {@code int} value
	 * @return the absolute version of {@code value}
	 * @see Math#abs(int)
	 */
	public static int abs(final int value) {
		return Math.abs(value);
	}
	
	/**
	 * Returns an {@code int} value in the interval of length {@code length}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code intPredicate} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param length the length of the interval
	 * @param intPredicate an {@code IntPredicate} instance
	 * @return an {@code int} value in the interval of length {@code length}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code intPredicate} is {@code null}
	 */
	public static int findInterval(final int length, final IntPredicate intPredicate) {
		ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length");
		
		Objects.requireNonNull(intPredicate, "intPredicate == null");
		
		int currentMinimum = 0;
		int currentLength = length;
		
		while(currentLength > 0) {
			int currentHalf = currentLength >> 1;
			int currentMiddle = currentMinimum + currentHalf;
			
			if(intPredicate.test(currentMiddle)) {
				currentMinimum = currentMiddle + 1;
				currentLength -= currentHalf + 1;
			} else {
				currentLength = currentHalf;
			}
		}
		
		return saturate(currentMinimum - 1, 0, length - 2);
	}
	
	/**
	 * Returns the prime number at {@code index} from the associated table.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than {@code 1023}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index of the prime number
	 * @return the prime number at {@code index} from the associated table
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than {@code 1023}
	 */
	public static int getPrimeNumberAt(final int index) {
		return PRIME_NUMBERS[ParameterArguments.requireRange(index, 0, getPrimeNumberCount() - 1, "index")];
	}
	
	/**
	 * Returns the number of prime numbers in the associated table.
	 * 
	 * @return the number of prime numbers in the associated table
	 */
	public static int getPrimeNumberCount() {
		return PRIME_NUMBERS.length;
	}
	
	/**
	 * Returns the greater value of {@code a} and {@code b}.
	 * <p>
	 * The result is the argument closer to the value of {@code Integer.MAX_VALUE}.
	 * <p>
	 * If the arguments have the same value, the result is that same value.
	 * 
	 * @param a a value
	 * @param b a value
	 * @return the greater value of {@code a} and {@code b}
	 * @see Math#max(int, int)
	 */
	public static int max(final int a, final int b) {
		return Math.max(a, b);
	}
	
	/**
	 * Returns the smaller value of {@code a} and {@code b}.
	 * <p>
	 * The result the argument closer to the value of {@code Integer.MIN_VALUE}.
	 * <p>
	 * If the arguments have the same value, the result is that same value.
	 * 
	 * @param a a value
	 * @param b a value
	 * @return the smaller value of {@code a} and {@code b}
	 * @see Math#min(int, int)
	 */
	public static int min(final int a, final int b) {
		return Math.min(a, b);
	}
	
	/**
	 * Packs the {@code int} values {@code a} and {@code b} into a single {@code int} value.
	 * <p>
	 * Returns the packed {@code int} value.
	 * <p>
	 * If either {@code a} or {@code b} are less than {@code 0} or greater than {@code 65535}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param a an {@code int} in the interval {@code [0, 65535]}
	 * @param b an {@code int} in the interval {@code [0, 65535]}
	 * @return the packed {@code int} value
	 * @throws IllegalArgumentException thrown if, and only if, either {@code a} or {@code b} are less than {@code 0} or greater than {@code 65535}
	 */
	public static int pack(final int a, final int b) {
		ParameterArguments.requireRange(a, 0, 65535, "a");
		ParameterArguments.requireRange(b, 0, 65535, "b");
		
		return (a << 16) | (b & 0xFFFF);
	}
	
	/**
	 * Returns the padding for {@code contentSize} given a block size of {@code 8}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Ints.padding(contentSize, 8);
	 * }
	 * </pre>
	 * 
	 * @param contentSize the size of the content
	 * @return the padding for {@code contentSize} given a block size of {@code 8}
	 */
	public static int padding(final int contentSize) {
		return padding(contentSize, 8);
	}
	
	/**
	 * Returns the padding for {@code contentSize} given a block size of {@code blockSize}.
	 * 
	 * @param contentSize the size of the content
	 * @param blockSize the size of the block
	 * @return the padding for {@code contentSize} given a block size of {@code blockSize}
	 */
	public static int padding(final int contentSize, final int blockSize) {
		return contentSize % blockSize == 0 ? 0 : blockSize - (contentSize % blockSize);
	}
	
	/**
	 * Performs a modulo operation on {@code x} and {@code y}.
	 * <p>
	 * Returns an {@code int} value.
	 * <p>
	 * The modulo operation performed by this method differs slightly from the modulo operator in Java.
	 * <p>
	 * If {@code x} is positive, the following occurs:
	 * <pre>
	 * {@code
	 * int z = x % y;
	 * }
	 * </pre>
	 * If {@code x} is negative, the following occurs:
	 * <pre>
	 * {@code
	 * int z = (x % y + y) % y;
	 * }
	 * </pre>
	 * 
	 * @param x an {@code int} value
	 * @param y an {@code int} value
	 * @return an {@code int} value
	 */
	public static int positiveModulo(final int x, final int y) {
		return x < 0 ? (x % y + y) % y : x % y;
	}
	
	/**
	 * Returns an {@code int} with the bits of {@code value} reversed.
	 * 
	 * @param value an {@code int} value
	 * @return an {@code int} with the bits of {@code value} reversed
	 */
	public static int reverseBits(final int value) {
		int currentValue = (value << 16) | (value >>> 16);
		
		currentValue = ((currentValue & 0x00FF00FF) << 8) | ((currentValue & 0xFF00FF00) >>> 8);
		currentValue = ((currentValue & 0x0F0F0F0F) << 4) | ((currentValue & 0xF0F0F0F0) >>> 4);
		currentValue = ((currentValue & 0x33333333) << 2) | ((currentValue & 0xCCCCCCCC) >>> 2);
		currentValue = ((currentValue & 0x55555555) << 1) | ((currentValue & 0xAAAAAAAA) >>> 1);
		
		return currentValue;
	}
	
	/**
	 * Returns a saturated (or clamped) value based on {@code value}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Ints.saturate(value, 0, 255);
	 * }
	 * </pre>
	 * 
	 * @param value the value to saturate (or clamp)
	 * @return a saturated (or clamped) value based on {@code value}
	 */
	public static int saturate(final int value) {
		return saturate(value, 0, 255);
	}
	
	/**
	 * Returns a saturated (or clamped) value based on {@code value}.
	 * <p>
	 * If {@code value} is less than {@code min(edgeA, edgeB)}, {@code min(edgeA, edgeB)} will be returned. If {@code value} is greater than {@code max(edgeA, edgeB)}, {@code max(edgeA, edgeB)} will be returned. Otherwise {@code value} will be
	 * returned.
	 * 
	 * @param value the value to saturate (or clamp)
	 * @param edgeA the minimum or maximum value
	 * @param edgeB the maximum or minimum value
	 * @return a saturated (or clamped) value based on {@code value}
	 */
	public static int saturate(final int value, final int edgeA, final int edgeB) {
		final int minimum = min(edgeA, edgeB);
		final int maximum = max(edgeA, edgeB);
		
		if(value < minimum) {
			return minimum;
		} else if(value > maximum) {
			return maximum;
		} else {
			return value;
		}
	}
	
	/**
	 * Returns an {@code int} representation of a {@code double} value.
	 * 
	 * @param value a {@code double} value
	 * @return an {@code int} representation of a {@code double} value
	 */
	public static int toInt(final double value) {
		return (int)(value);
	}
	
	/**
	 * Returns an {@code int} representation of a {@code float} value.
	 * 
	 * @param value a {@code float} value
	 * @return an {@code int} representation of a {@code float} value
	 */
	public static int toInt(final float value) {
		return (int)(value);
	}
	
	/**
	 * Returns an {@code int[]} with a length of {@code length} and is filled with {@code 0}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Ints.array(length, 0);
	 * }
	 * </pre>
	 * 
	 * @param length the length of the {@code int[]}
	 * @return an {@code int[]} with a length of {@code length} and is filled with {@code 0}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static int[] array(final int length) {
		return array(length, 0);
	}
	
	/**
	 * Returns an {@code int[]} with a length of {@code length} and is filled with {@code int} values from {@code intSupplier}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code intSupplier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param length the length of the {@code int[]}
	 * @param intSupplier the {@code IntSupplier} to fill the {@code int[]} with
	 * @return an {@code int[]} with a length of {@code length} and is filled with {@code int} values from {@code intSupplier}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code intSupplier} is {@code null}
	 */
	public static int[] array(final int length, final IntSupplier intSupplier) {
		final int[] array = new int[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Objects.requireNonNull(intSupplier, "intSupplier == null");
		
		for(int i = 0; i < array.length; i++) {
			array[i] = intSupplier.getAsInt();
		}
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with a length of {@code length} and is filled with {@code value}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param length the length of the {@code int[]}
	 * @param value the {@code int} value to fill the {@code int[]} with
	 * @return an {@code int[]} with a length of {@code length} and is filled with {@code value}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static int[] array(final int length, final int value) {
		final int[] array = new int[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Arrays.fill(array, value);
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} representation of {@code objects} using {@code arrayFunction}.
	 * <p>
	 * If either {@code objects}, at least one of its elements, {@code arrayFunction} or at least one of its results are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Ints.toArray(objects, arrayFunction, 0);
	 * }
	 * </pre>
	 * 
	 * @param <T> the generic type
	 * @param objects a {@code List} of type {@code T} with {@code Object} instances to convert into {@code int[]} instances
	 * @param arrayFunction a {@code Function} that maps {@code Object} instances of type {@code T} into {@code int[]} instances
	 * @return an {@code int[]} representation of {@code objects} using {@code arrayFunction}
	 * @throws NullPointerException thrown if, and only if, either {@code objects}, at least one of its elements, {@code arrayFunction} or at least one of its results are {@code null}
	 */
	public static <T> int[] toArray(final List<T> objects, final Function<T, int[]> arrayFunction) {
		return toArray(objects, arrayFunction, 0);
	}
	
	/**
	 * Returns an {@code int[]} representation of {@code objects} using {@code arrayFunction}.
	 * <p>
	 * If either {@code objects}, at least one of its elements, {@code arrayFunction} or at least one of its results are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code minimumLength} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param <T> the generic type
	 * @param objects a {@code List} of type {@code T} with {@code Object} instances to convert into {@code int[]} instances
	 * @param arrayFunction a {@code Function} that maps {@code Object} instances of type {@code T} into {@code int[]} instances
	 * @param minimumLength the minimum length of the returned {@code int[]} if, and only if, either {@code objects.isEmpty()} or the array has a length of {@code 0}
	 * @return an {@code int[]} representation of {@code objects} using {@code arrayFunction}
	 * @throws IllegalArgumentException thrown if, and only if, {@code minimumLength} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code objects}, at least one of its elements, {@code arrayFunction} or at least one of its results are {@code null}
	 */
	public static <T> int[] toArray(final List<T> objects, final Function<T, int[]> arrayFunction, final int minimumLength) {
		ParameterArguments.requireNonNullList(objects, "objects");
		
		Objects.requireNonNull(arrayFunction, "arrayFunction == null");
		
		ParameterArguments.requireRange(minimumLength, 0, Integer.MAX_VALUE, "minimumLength");
		
		if(objects.isEmpty()) {
			return array(minimumLength);
		}
		
		try(final IntArrayOutputStream intArrayOutputStream = new IntArrayOutputStream()) {
			for(final T object : objects) {
				intArrayOutputStream.write(arrayFunction.apply(object));
			}
			
			final int[] array = intArrayOutputStream.toIntArray();
			
			return array.length == 0 ? array(minimumLength) : array;
		}
	}
	
	/**
	 * Returns an {@code int[]} representation of {@code byteArray}.
	 * <p>
	 * If {@code byteArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param byteArray a {@code byte[]}
	 * @param isUnsigned {@code true} if, and only if, unsigned values should be used, {@code false} otherwise
	 * @return an {@code int[]} representation of {@code byteArray}
	 * @throws NullPointerException thrown if, and only if, {@code byteArray} is {@code null}
	 */
	public static int[] toArray(final byte[] byteArray, final boolean isUnsigned) {
		Objects.requireNonNull(byteArray, "byteArray == null");
		
		final int[] intArray = new int[byteArray.length];
		
		for(int i = 0; i < byteArray.length; i++) {
			intArray[i] = isUnsigned ? byteArray[i] & 0xFF : byteArray[i];
		}
		
		return intArray;
	}
	
	/**
	 * Unpacks the {@code int} value {@code packedValue} into two {@code int} values in an {@code int[]}.
	 * <p>
	 * Returns an {@code int[]} with the unpacked {@code int} values.
	 * 
	 * @param packedValue a packed {@code int} value
	 * @return an {@code int[]} with the unpacked {@code int} values
	 */
	public static int[] unpack(final int packedValue) {
		final int a = (packedValue >>> 16) & 0xFFFF;
		final int b = (packedValue >>>  0) & 0xFFFF;
		
		return new int[] {a, b};
	}
}