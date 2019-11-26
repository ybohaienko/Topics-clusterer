package com.bohaienko.LR2.utils;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class Common {
	static <T> T[] appendToArray(T[] arr, T element) {
		final int N = arr.length;
		arr = Arrays.copyOf(arr, N + 1);
		arr[N] = element;
		return arr;
	}
}
