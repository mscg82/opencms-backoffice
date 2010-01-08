/*
 * Exception.h
 *
 *  Created on: 16/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef EXCEPTION_H_
#define EXCEPTION_H_

#include <string>

using namespace std;

namespace cpp{
namespace lang{

class Exception{
public:
	Exception();
	Exception(const string &message);
	Exception(const string &message, Exception *cause);
	~Exception();

	string getMessage() const;
	Exception* getCause() const;
protected:
	string *message;
	Exception *cause;

	void setMessage(const string &message);
	void setCause(Exception *cause);
private:
	void init(const string &message, Exception *cause);
};

}
}
#endif /* EXCEPTION_H_ */
