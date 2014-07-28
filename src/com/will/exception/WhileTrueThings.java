package com.will.exception;

public class WhileTrueThings {
	public static void main(String[] args) {
//		failedOne();
//		sucessOne();
	}

	private static void sucessOne() {
		while (true) {
			try {
				System.out.println("ddddddddddddffffffffffff");
				Thread.sleep(1000);
				throw new Exception();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void failedOne() {
		try {
			while (true) {
				System.out.println("dfffffffffffff");
				Thread.sleep(1000);
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
