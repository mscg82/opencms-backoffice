/*
 * AbstractTransferStatisticHandler.h
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#ifndef ABSTRACTTRANSFERSTATISTICHANDLER_H_
#define ABSTRACTTRANSFERSTATISTICHANDLER_H_

#include "CurlClient.h"

class CurlClient;

class AbstractTransferStatisticHandler {
public:
	AbstractTransferStatisticHandler();
	virtual ~AbstractTransferStatisticHandler();

	virtual int updatedStatistics(CurlClient *client) = 0;
};

#endif /* ABSTRACTTRANSFERSTATISTICHANDLER_H_ */
