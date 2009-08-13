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

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.exedio.cope.Item;
import com.exedio.cope.StringField;
import com.exedio.cope.pattern.Media;

final class Anchor implements Serializable // for session persistence
{
	private static final long serialVersionUID = 1l;
	
	private final Target defaultTarget;
	final boolean draftsEnabled;
	
	final String borderDisableButtonURL;
	final String borderEnableButtonURL;
	final String hideButtonURL;
	final String closeButtonURL;
	final String previousPositionButtonURL;
	final String loginURL;
	
	final String user;
	final Session session;
	final String sessionName;
	
	private Target target;
	boolean borders = false;
	final GetterSet<Modification> modifications = new GetterSet<Modification>();
	
	Anchor(
			final Target defaultTarget,
			final boolean draftsEnabled,
			final Editor filter,
			final HttpServletRequest request,
			final HttpServletResponse response,
			final String user,
			final Session session,
			final String sessionName)
	{
		this.defaultTarget = defaultTarget;
		this.draftsEnabled = draftsEnabled;
		
		this.borderDisableButtonURL = filter!=null ? filter.getBorderButtonURL(request, response, true) : null;
		this.borderEnableButtonURL  = filter!=null ? filter.getBorderButtonURL(request, response, false) : null;
		this.hideButtonURL          = filter!=null ? filter.getHideButtonURL  (request, response) : null;
		this.closeButtonURL         = filter!=null ? filter.getCloseButtonURL (request, response) : null;
		this.previousPositionButtonURL = filter!=null ? filter.getPreviousPositionButtonURL(request, response) : null;
		this.loginURL = request!=null ? (request.getContextPath() + request.getServletPath() + '/') : null;
		
		this.user = user;
		this.session = session;
		this.sessionName = sessionName;
		this.target = defaultTarget;
		assert defaultTarget!=null;
		assert user!=null;
		assert session!=null;
	}
	
	Target getTarget()
	{
		if(!target.exists())
			target = defaultTarget;
		return target;
	}
	
	void setTarget(final Target target)
	{
		if(target==null)
			throw new NullPointerException();
		this.target = target;
	}
	
	int getModificationsCount()
	{
		return modifications.size();
	}
	
	String getModification(final StringField feature, final Item item)
	{
		if(!modifications.isEmpty()) // shortcut
		{
			final ModificationString m = modifications.get(new ModificationString(feature, item));
			if(m!=null)
				return m.value;
		}
		return target.get(feature, item);
	}
	
	FileItem getModification(final Media feature, final Item item)
	{
		if(!modifications.isEmpty()) // shortcut
		{
			final ModificationMedia fi = modifications.get(new ModificationMedia(feature, item));
			if(fi!=null)
				return fi.value;
		}
		return null;
	}
	
	String getModificationURL(final Media feature, final Item item, final HttpServletResponse response)
	{
		if(!modifications.isEmpty()) // shortcut
		{
			final ModificationMedia m = new ModificationMedia(feature, item);
			if(modifications.contains(m))
				return m.getURL(this, response);
		}
		return null;
	}
	
	Set<Modification> getModifications()
	{
		return Collections.unmodifiableSet(modifications);
	}
	
	void modify(final String content, final StringField feature, final Item item)
	{
		modifications.add(new ModificationString(feature, item, content));
	}
	
	void modify(final FileItem content, final Media feature, final Item item)
	{
		modifications.add(new ModificationMedia(feature, item, content));
	}
	
	void notifyPublished(final StringField feature, final Item item)
	{
		modifications.remove(new ModificationString(feature, item));
	}
	
	void notifyPublishedAll()
	{
		modifications.clear();
	}
	
	String getHistoryAuthor()
	{
		return (sessionName!=null ? sessionName : user) + " (CCE)";
	}
	
	@Override
	public String toString()
	{
		final StringBuilder bf = new StringBuilder();
		
		// must not call login#getName() here,
		// because this may require a transaction,
		// which may not be present,
		// especially when this method is called by lamdba probe.
		if(sessionName!=null)
			bf.append('"').append(sessionName).append('"');
		else
			bf.append(user);
		
		if(borders)
			bf.append(" bordered");
		
		final int modificationsCount = modifications.size();
		if(modificationsCount>0)
		{
			bf.append(" *");
			if(modificationsCount>1)
				bf.append(modificationsCount);
		}
		
		return bf.toString();
	}
}