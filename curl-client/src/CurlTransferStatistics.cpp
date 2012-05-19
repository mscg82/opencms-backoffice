/*
 * CurlTransferStatistics.cpp
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#include "CurlTransferStatistics.h"

CurlTransferStatistics::CurlTransferStatistics(double downloadTotal, double downloadNow,
		                                       double uploadTotal, double uploadNow) {
	set(downloadTotal, downloadNow, uploadTotal, uploadNow);

}

CurlTransferStatistics::~CurlTransferStatistics() {

}

void CurlTransferStatistics::reset() {
	set(0.0, 0.0, 0.0, 0.0);
}

void CurlTransferStatistics::set(double downloadTotal, double downloadNow,
		                         double uploadTotal, double uploadNow) {
	this->downloadTotal = downloadTotal;
	this->downloadNow = downloadNow;
	this->uploadTotal = uploadTotal;
	this->uploadNow = uploadNow;
}

double CurlTransferStatistics::getDownloadNow() const {
	return downloadNow;
}

double CurlTransferStatistics::getDownloadTotal() const {
	return downloadTotal;
}

double CurlTransferStatistics::getUploadNow() const {
	return uploadNow;
}

double CurlTransferStatistics::getUploadTotal() const {
	return uploadTotal;
}
