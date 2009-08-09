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

import javax.servlet.http.HttpServletResponse;

import com.exedio.cope.IntegerField;
import com.exedio.cope.Item;
import com.exedio.cope.StringField;
import com.exedio.cope.editor.Editor;
import com.exedio.cope.editor.Session;
import com.exedio.cope.pattern.MapField;
import com.exedio.cope.pattern.Media;
import com.exedio.cope.pattern.MediaFilter;

final class Out
{
	private final PrintStream bf;
	
	Out(final HttpServletResponse response) throws IOException
	{
		this.bf = new PrintStream(response.getOutputStream(), false, EditedServlet.ENCODING);
	}
	
	void print(final String s)
	{
		bf.print(s);
	}
	
	void print(final Date d)
	{
		bf.print(d);
	}
	
	void print(final Session s)
	{
		bf.print(s);
	}
	
	void print(final boolean b)
	{
		bf.print(b);
	}
	
	void print(final int i)
	{
		bf.print(i);
	}
	
	boolean isEditorBordersEnabled()
	{
		return Editor.isBordersEnabled();
	}
	
	void writeEditorHead()
	{
		Editor.writeHead(bf);
	}
	
	void writeEditorBar()
	{
		Editor.writeBar(bf);
	}
	
	void print(final String s, final StringField feature, final Item item)
	{
		bf.print(Editor.edit(s, feature, item));
	}
	
	<K> void print(final String s, final MapField<K, String> feature, final Item item, final K key)
	{
		bf.print(Editor.edit(s, feature, item, key));
	}
	
	void edit(final Media feature, final Item item)
	{
		bf.print(Editor.edit(feature, item));
	}
	
	void edit(final MediaFilter feature, final Item item)
	{
		bf.print(Editor.edit(feature, item));
	}
	
	void swap(final IntegerField feature, final Item item)
	{
		bf.print(Editor.edit(feature, item));
	}
	
	void close()
	{
		bf.close();
	}
}
