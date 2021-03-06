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

import com.exedio.cope.Item;
import com.exedio.cope.StringField;

final class TargetLive implements Target
{
	// TODO serialization should not create multiple instances
	static final TargetLive INSTANCE = new TargetLive();
	static final String ID = "Live";

	private TargetLive()
	{
		// prevent public instantiation
	}

	@Override
	public String getID()
	{
		return ID;
	}

	@Override
	public boolean exists()
	{
		return true;
	}

	@Override
	public String getDescription()
	{
		return "Live Site";
	}

	@Override
	public boolean isLive()
	{
		return true;
	}

	@Override
	public String get(final StringField feature, final Item item)
	{
		return null;
	}

	@Override
	public void save(final Anchor anchor)
	{
		for(final Modification m : anchor.getModifications())
			m.publish();
		// TODO maintain history
	}

	@Override
	public int hashCode()
	{
		return getClass().hashCode();
	}

	@Override
	public boolean equals(final Object other)
	{
		return (other!=null) && (other instanceof TargetLive);
	}

	private static final long serialVersionUID = 1l;
}
