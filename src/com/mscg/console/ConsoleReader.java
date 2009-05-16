package com.mscg.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader {
	private static BufferedReader in = null;

	static{
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	public static String readConsole() throws IOException {
        return in.readLine();
    }
}
