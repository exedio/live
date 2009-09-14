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

package com.exedio.cope.util;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exedio.cope.IntegerField;
import com.exedio.cope.Item;
import com.exedio.cope.StringField;
import com.exedio.cope.live.Session;
import com.exedio.cope.live.util.LiveFilter;
import com.exedio.cope.pattern.MapField;
import com.exedio.cope.pattern.Media;
import com.exedio.cope.pattern.MediaFilter;

final class Out
{
	private final PrintStream bf;
	private final HttpServletRequest request;
	
	Out(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		this.bf = new PrintStream(response.getOutputStream(), false, EditedServlet.ENCODING);
		this.request = request;
	}
	
	void write(final String s)
	{
		bf.print(s);
	}
	
	void write(final Date d)
	{
		bf.print(d);
	}
	
	void write(final Session s)
	{
		bf.print(s);
	}
	
	void write(final boolean b)
	{
		bf.print(b);
	}
	
	void write(final int i)
	{
		bf.print(i);
	}
	
	boolean isEditorLoggedIn()
	{
		return LiveFilter.isLoggedIn();
	}
	
	boolean isEditorBordersEnabled()
	{
		return LiveFilter.isBordersEnabled();
	}
	
	Session getEditorSession()
	{
		return LiveFilter.getSession();
	}
	
	void writeEditorHead()
	{
		LiveFilter.writeHead(bf);
	}
	
	void writeEditorBar()
	{
		LiveFilter.writeBar(bf);
	}
	
	void write(final String s, final StringField feature, final Item item)
	{
		bf.print(LiveFilter.edit(s, feature, item));
	}
	
	<K> void write(final String s, final MapField<K, String> feature, final Item item, final K key)
	{
		bf.print(LiveFilter.edit(s, feature, item, key));
	}
	
	void edit(final Media feature, final Item item)
	{
		bf.print(LiveFilter.edit(feature, item));
	}
	
	void edit(final MediaFilter feature, final Item item)
	{
		bf.print(LiveFilter.edit(feature, item));
	}
	
	void swapIcon(final IntegerField feature, final Item item)
	{
		bf.print(LiveFilter.edit(feature, item, request.getContextPath() + "/previous.png"));
	}
	
	void swapText(final IntegerField feature, final Item item)
	{
		bf.print(LiveFilter.edit(feature, item, null));
	}
	
	void close()
	{
		bf.close();
	}
}
