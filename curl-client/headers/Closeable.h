/*
 * Closeable.h
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#ifndef CLOSEABLE_H_
#define CLOSEABLE_H_

class Closeable {
public:
	Closeable();
	virtual ~Closeable();

	virtual void close() = 0;
};

#endif /* CLOSEABLE_H_ */
