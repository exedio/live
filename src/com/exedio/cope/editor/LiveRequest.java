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

package com.exedio.cope.editor;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exedio.cope.IntegerField;
import com.exedio.cope.Item;

final class LiveRequest
{
	final Editor filter;
	final HttpServletRequest request;
	final HttpServletResponse response;
	final Anchor anchor;
	private HashMap<IntegerField, Item> positionItems = null;
	
	LiveRequest(
			final Editor filter,
			final HttpServletRequest request,
			final HttpServletResponse response,
			final Anchor anchor)
	{
		this.filter = filter;
		this.request = request;
		this.response = response;
		this.anchor = anchor;
		
		assert filter!=null;
		assert request!=null;
		assert response!=null;
		assert anchor!=null;
	}
	
	Item registerPositionItem(final IntegerField feature, final Item item)
	{
		final Integer next = feature.get(item);
		if(next==null)
			return null;
		
		if(positionItems==null)
			positionItems = new HashMap<IntegerField, Item>();
		
		final Item result = positionItems.put(feature, item);
		if(result==null)
			return null;
		
		final Integer previous = feature.get(result);
		return (previous!=null && previous.intValue()<next.intValue()) ? result : null;
	}
	
	boolean isBordersEnabled()
	{
		return anchor.borders;
	}
	
	Session getSession()
	{
		return anchor.session;
	}
}
