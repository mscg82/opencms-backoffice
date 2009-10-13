package com.mashfrog.backoffice.jsp.util;

import org.opencms.util.CmsStringUtil;

public class JspUtil {

	public static boolean isEmptyOrWhiteSpaceOnly(String orig){
		return CmsStringUtil.isEmptyOrWhitespaceOnly(orig);
	}

	public static boolean isNotEmptyOrWhiteSpaceOnly(String orig){
		return CmsStringUtil.isNotEmptyOrWhitespaceOnly(orig);
	}
}
