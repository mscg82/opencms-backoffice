/*
 * CurlTransferStatistics.h
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#ifndef CURLTRANSFERSTATISTICS_H_
#define CURLTRANSFERSTATISTICS_H_

class CurlTransferStatistics {
private:
	double downloadTotal;
	double downloadNow;
	double uploadTotal;
	double uploadNow;
public:
	CurlTransferStatistics(double downloadTotal = 0.0, double downloadNow = 0.0,
			               double uploadTotal = 0.0, double uploadNow = 0.0);
	virtual ~CurlTransferStatistics();

	void reset();
	void set(double downloadTotal, double downloadNow, double uploadTotal, double uploadNow);

	double getDownloadNow() const;
    double getDownloadTotal() const;
    double getUploadNow() const;
    double getUploadTotal() const;

};

#endif /* CURLTRANSFERSTATISTICS_H_ */
