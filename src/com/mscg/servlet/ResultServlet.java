package com.mscg.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResultServlet extends HttpServlet {

	private static final long serialVersionUID = -5542958986316517841L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter pw = resp.getWriter();
		pw.write("<html>\n");
		pw.write("    <head>\n");
		pw.write("        <title>Struts 2 redirect test Jsp</title>\n");
		pw.write("    </head>\n");
		pw.write("    <body>\n");
		pw.write("        <h1>This is the content of the redirected jsp written by a servlet</h1>\n");
		pw.write("    </body>\n");
		pw.write("</html>\n");
		pw.flush();
		pw.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
