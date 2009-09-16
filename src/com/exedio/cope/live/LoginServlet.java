/*
 * Copyright (C) 2004-2009  exedio GmbH (www.exedio.com)
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.exedio.cope.live;

import static com.exedio.cops.CopsServlet.UTF8;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.exedio.cope.Model;
import com.exedio.cops.Cop;

final class LoginServlet
{
	private final Model model;
	private final LiveServlet servlet;
	
	LoginServlet(final Model model, final LiveServlet servlet)
	{
		this.model = model;
		this.servlet = servlet;
	}
	
	private final void startTransaction(final String name)
	{
		servlet.startTransaction(name);
	}
	
	static final String ANCHOR = Session.class.getName();
	
	static final String LOGIN_SUBMIT   = "login.submit";
	static final String LOGIN_USER     = "login.user";
	static final String LOGIN_PASSWORD = "login.password";
	
	final void doRequest(
			final boolean draftsEnabled,
			final Target defaultTarget,
			final HttpServletRequest request,
			final HttpSession httpSession,
			final HttpServletResponse response)
	throws IOException
	{
		assert httpSession!=null;
		response.setContentType("text/html; charset="+UTF8);
		if(Cop.isPost(request) && request.getParameter(LOGIN_SUBMIT)!=null)
		{
			final String user = request.getParameter(LOGIN_USER);
			final String password = request.getParameter(LOGIN_PASSWORD);
			try
			{
				startTransaction("login");
				final Session session = servlet.login(user, password);
				if(session!=null)
				{
					httpSession.setAttribute(ANCHOR, new Anchor(defaultTarget, draftsEnabled, request, user, session, session.getName()));
					servlet.redirectHome(request, response);
				}
				else
				{
					final StringBuilder out = new StringBuilder();
					Login_Jspm.write(out, request, response.encodeURL(request.getContextPath() + request.getServletPath()), LiveServlet.class.getPackage(), user);
					LiveServlet.writeBody(out, response);
				}
				model.commit();
			}
			finally
			{
				model.rollbackIfNotCommitted();
			}
		}
		else
		{
			final StringBuilder out = new StringBuilder();
			Login_Jspm.write(out, request, response.encodeURL(request.getContextPath() + request.getServletPath()), LiveServlet.class.getPackage(), null);
			LiveServlet.writeBody(out, response);
		}
	}
}
