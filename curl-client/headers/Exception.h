/*
 * Exception.h
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#ifndef EXCEPTION_H_
#define EXCEPTION_H_

#include <string>

using namespace std;

class Exception {
protected:
	string message;
	Exception *cause;

public:
	Exception();
	Exception(const string& message);
	Exception(const string& message, Exception *cause);
	virtual ~Exception();

	const string& getMessage() const;

	const Exception* getCause() const;
};

#endif /* EXCEPTION_H_ */
