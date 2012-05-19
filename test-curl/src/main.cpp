#include <windows.h>
#include <iostream>
#include <sstream>
#include <string>
#include "CurlClient.h"
#include "WriteToFileCurlWriteHandler.h"
#include "ConsoleWriteStatisticsHandler.h"

using namespace std;

int WINAPI WinMain1(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow) {
	try {
		stringstream message;

		CurlClient client(true);

		client.setStatisticsHandler(new ConsoleWriteStatisticsHandler());

		client.setWriteHandler(new WriteToFileCurlWriteHandler("C:\\Users\\Giuseppe Miscione\\Desktop\\out1.html"));
		client.setUrl("https://www.google.it");
		CurlResult response = client.executeCall();
		if(response.isOkResponse())
			message << "First query was ok";
		else
			message << "First query failed: " << response.getCode() << " - " << response.getMessage();

		MessageBox(NULL, message.str().c_str(), "Test CURL - 1", MB_OK);
		client.cleanWriteHandler();

		message.str("");
		client.setWriteHandler(new WriteToFileCurlWriteHandler("C:\\Users\\Giuseppe Miscione\\Desktop\\out2.html"));
		client.setUrl("http://www.virgilio.it");
		response = client.executeCall();
		if(response.isOkResponse())
			message << "Second query was ok";
		else
			message << "Second query failed: " << response.getCode() << " - " << response.getMessage();
		MessageBox(NULL, message.str().c_str(), "Test CURL - 2", MB_OK);


		message.str("");
		client.setWriteHandler(new WriteToFileCurlWriteHandler("C:\\Users\\Giuseppe Miscione\\Desktop\\jMP3TagEditor.zip"));
		client.setUrl("http://jmp3-tag-editor.googlecode.com/svn/tags/jMP3TagEditor/1.2.1/dist/jMP3TagEditor.zip");
		response = client.executeCall();
		if(response.isOkResponse())
			message << "Third query was ok";
		else
			message << "Third query failed: " << response.getCode() << " - " << response.getMessage();

		MessageBox(NULL, message.str().c_str(), "Test CURL - 3", MB_OK);
		client.cleanWriteHandler();

		client.cleanStatisticsHandler();

	} catch(Exception e) {
		MessageBox(NULL, e.getMessage().c_str(), "An error occurred", MB_OK);
	}

    return 0;
}

int WINAPI WinMain2(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow) {
	WriteToFileCurlWriteHandler writeHandler("C:\\Users\\Giuseppe Miscione\\Desktop\\test.txt");
	string data("This is a test");
	writeHandler.writeData((void*)data.c_str(), data.length(), 1, NULL);

	return 0;
}

int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow) {
	return WinMain1(hInstance, hPrevInstance, lpCmdLine, nCmdShow);
}
