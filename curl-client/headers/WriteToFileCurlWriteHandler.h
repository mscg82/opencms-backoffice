/*
 * WriteToFileCurlWriteHandler.h
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#ifndef WRITETOFILECURLWRITEHANDLER_H_
#define WRITETOFILECURLWRITEHANDLER_H_

#include <string>
#include <fstream>
#include "AbstractCurlWriteHandler.h"
#include "Closeable.h"

using namespace std;

#define DEFAULT_OPEN_MODE ofstream::out | ofstream::binary | ofstream::trunc

class WriteToFileCurlWriteHandler : public AbstractCurlWriteHandler, Closeable {
private:
	ofstream *fileStream;

public:
	WriteToFileCurlWriteHandler(const string& filename);
	WriteToFileCurlWriteHandler(const string& filename, ios_base::openmode mode);
	virtual ~WriteToFileCurlWriteHandler();

	virtual size_t writeData(void *buffer, size_t size, size_t nmemb, CurlClient *client);
	virtual void close();
	void open(const string& filename, ios_base::openmode mode = DEFAULT_OPEN_MODE);
};

#endif /* WRITETOFILECURLWRITEHANDLER_H_ */
