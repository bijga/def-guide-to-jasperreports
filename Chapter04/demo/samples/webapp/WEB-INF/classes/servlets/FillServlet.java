/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import datasource.WebappDataSource;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: FillServlet.java 1236 2006-04-22 07:51:44Z teodord $
 */
public class FillServlet extends HttpServlet
{


	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		ServletContext context = this.getServletConfig().getServletContext();

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try
		{
			String reportFileName = context.getRealPath("/reports/WebappReport.jasper");
			File reportFile = new File(reportFileName);
			if (!reportFile.exists())
				throw new JRRuntimeException("File WebappReport.jasper not found. The report design must be compiled first.");

			Map parameters = new HashMap();
			parameters.put("ReportTitle", "Address Report");
			parameters.put("BaseDir", reportFile.getParentFile());
						
			JasperPrint jasperPrint = 
				JasperFillManager.fillReport(
					reportFileName, 
					parameters, 
					new WebappDataSource()
					);
						
			request.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
		}
		catch (JRException e)
		{
			out.println("<html>");
			out.println("<head>");
			out.println("<title>JasperReports - Web Application Sample</title>");
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
			out.println("</head>");
			
			out.println("<body bgcolor=\"white\">");

			out.println("<span class=\"bnew\">JasperReports encountered this error :</span>");
			out.println("<pre>");

			e.printStackTrace(out);

			out.println("</pre>");

			out.println("</body>");
			out.println("</html>");
		}

		out.println("<html>");
		out.println("<head>");
		out.println("<title>JasperReports - Web Application Sample</title>");
		out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
		out.println("</head>");
		
		out.println("<body bgcolor=\"white\">");

		out.println("<span class=\"bold\">The compiled report design was successfully filled with data.</span>");

		out.println("</body>");
		out.println("</html>");
	}


}
