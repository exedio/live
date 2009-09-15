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

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.exedio.cope.Model;
import com.exedio.cope.NoSuchIDException;
import com.exedio.cope.Type;
import com.exedio.cope.util.ConnectToken;
import com.exedio.cope.util.ServletUtil;
import com.exedio.cops.Cop;
import com.exedio.cops.CopsServlet;
import com.exedio.cops.Resource;

public abstract class LiveServlet extends CopsServlet
{
	private static final long serialVersionUID = 1l;

	static final Resource logo = new Resource("logo.png");
	
	static final Resource borderDisable = new Resource("borderDisable.png");
	static final Resource borderEnable  = new Resource("borderEnable.png");
	static final Resource hide          = new Resource("hide.png");
	static final Resource close         = new Resource("close.png");
	
	private final Model model;
	private final Bar bar;
	private final Management management;
	private final MediaServlet media;
	
	/**
	 * Subclasses must define a public no-args constructor
	 * providing the model.
	 */
	protected LiveServlet(final Model model)
	{
		if(model==null)
			throw new NullPointerException("model");
		
		this.model = model;
		this.bar = new Bar(model, this);
		this.management = new Management(model, this);
		this.media = new MediaServlet(model, this);
	}
	
	private boolean draftsEnabled = false;
	private Target defaultTarget = TargetLive.INSTANCE;
	private ConnectToken connectToken = null;
	
	@Override
	public final void init(final ServletConfig config) throws ServletException
	{
		super.init(config);
		
		for(final Type<?> type : model.getTypes())
			if(type==DraftItem.TYPE) // DraftItem implies Draft because of the parent field
			{
				draftsEnabled = true;
				defaultTarget = TargetNewDraft.INSTANCE;
				break;
			}
		
		connectToken = ServletUtil.connect(model, config, getClass().getName());
		model.reviseIfSupported();
	}
	
	final void startTransaction(final String name)
	{
		model.startTransaction(getClass().getName() + '#' + name);
	}
	
	@Override
	public final void destroy()
	{
		if(connectToken!=null)
		{
			connectToken.returnIt();
			connectToken = null;
		}
		
		super.destroy();
	}
	
	protected abstract Session login(String user, String password);
	
	protected String getHome()
	{
		return "";
	}
	
	@Override
	public final void doRequest(
			final HttpServletRequest request,
			final HttpServletResponse response)
	throws IOException
	{
		request.setCharacterEncoding(UTF8);
		final HttpSession httpSession = request.getSession(true);
		final Object anchor = httpSession.getAttribute(ANCHOR);
		
		if(anchor==null)
			doLogin(request, httpSession, response);
		else
		{
			if(request.getParameter(Management.PREVIEW_OVERVIEW)!=null)
				management.doRequest(request, response, draftsEnabled, (Anchor)anchor);
			else if(request.getParameter(MediaServlet.MEDIA_FEATURE)!=null)
				media.doRequest(request, response, (Anchor)anchor);
			else
				bar.doRequest(request, httpSession, response, (Anchor)anchor);
		}
	}
	
	final void redirectHome(
			final HttpServletRequest request,
			final HttpServletResponse response)
	throws IOException
	{
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + '/' + getHome()));
	}
	
	final Target getTarget(final String id)
	{
		if(TargetLive.ID.equals(id))
		{
			return TargetLive.INSTANCE;
		}
		else if(TargetNewDraft.ID.equals(id))
		{
			return TargetNewDraft.INSTANCE;
		}
		else
		{
			try
			{
				startTransaction("findDraft");
				return new TargetDraft((Draft)model.getItem(id));
			}
			catch(NoSuchIDException e)
			{
				throw new RuntimeException(e);
			}
			finally
			{
				model.rollbackIfNotCommitted();
			}
		}
	}
	
	static final String LOGIN_SUBMIT   = "login.submit";
	static final String LOGIN_USER     = "login.user";
	static final String LOGIN_PASSWORD = "login.password";
	
	private final void doLogin(
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
				final Session session = login(user, password);
				if(session!=null)
				{
					httpSession.setAttribute(ANCHOR, new Anchor(defaultTarget, draftsEnabled, request, user, session, session.getName()));
					redirectHome(request, response);
				}
				else
				{
					final StringBuilder out = new StringBuilder();
					Login_Jspm.write(out, request, response.encodeURL(request.getContextPath() + request.getServletPath()), LiveServlet.class.getPackage(), user);
					writeBody(out, response);
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
			writeBody(out, response);
		}
	}
	
	static final String ANCHOR = Session.class.getName();
	
	static final void writeBody(
			final StringBuilder out,
			final HttpServletResponse response)
		throws IOException
	{
		ServletOutputStream outStream = null;
		try
		{
			outStream = response.getOutputStream();
			final byte[] outBytes = out.toString().getBytes(UTF8);
			outStream.write(outBytes);
		}
		finally
		{
			if(outStream!=null)
				outStream.close();
		}
	}
}
