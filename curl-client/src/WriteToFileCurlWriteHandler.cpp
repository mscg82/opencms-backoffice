/*
 * WriteToFileCurlWriteHandler.cpp
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#include "WriteToFileCurlWriteHandler.h"

WriteToFileCurlWriteHandler::WriteToFileCurlWriteHandler(const string& filename) {
	fileStream = NULL;
	open(filename);
}

WriteToFileCurlWriteHandler::WriteToFileCurlWriteHandler(const string& filename, ios_base::openmode mode) {
	fileStream = NULL;
	open(filename, mode);
}

WriteToFileCurlWriteHandler::~WriteToFileCurlWriteHandler() {
	close();
}

void WriteToFileCurlWriteHandler::open(const string& filename, ios_base::openmode mode) {
	close();
	this->fileStream = new ofstream();
	this->fileStream->open(filename.c_str(), mode);
}

size_t WriteToFileCurlWriteHandler::writeData(void *buffer, size_t size, size_t nmemb, CurlClient *client) {
	size_t ret = 0;
	if(fileStream != NULL) {
		ret = size * nmemb;
		fileStream->write((char*)buffer, ret);
		if(fileStream->bad())
			ret = 0;
	}
	return ret;
}

void WriteToFileCurlWriteHandler::close() {
	if(fileStream != NULL) {
		if(fileStream->is_open()) {
			fileStream->flush();
			fileStream->close();
		}
		delete fileStream;
		fileStream = NULL;
	}
}
