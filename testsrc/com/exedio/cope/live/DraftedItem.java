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

import com.exedio.cope.IntegerField;
import com.exedio.cope.Item;
import com.exedio.cope.StringField;
import com.exedio.cope.pattern.MapField;

public class DraftedItem extends Item
{
	static final StringField string = new StringField().optional();
	static final StringField string2 = new StringField().optional();
	static final MapField<Integer, String> map = MapField.newMap(new IntegerField(), new StringField().optional());

	/**

	 **
	 * Creates a new DraftedItem with all the fields initially needed.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tags <tt>@cope.constructor public|package|protected|private|none</tt> in the class comment and <tt>@cope.initial</tt> in the comment of fields.
	 */
	public DraftedItem()
	{
		this(new com.exedio.cope.SetValue<?>[]{
		});
	}/**

	 **
	 * Creates a new DraftedItem and sets the given fields initially.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.generic.constructor public|package|protected|private|none</tt> in the class comment.
	 */
	private DraftedItem(final com.exedio.cope.SetValue<?>... setValues)
	{
		super(setValues);
	}/**

	 **
	 * Returns the value of {@link #string}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getString()
	{
		return DraftedItem.string.get(this);
	}/**

	 **
	 * Sets a new value for {@link #string}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setString(final java.lang.String string)
			throws
				com.exedio.cope.StringLengthViolationException
	{
		DraftedItem.string.set(this,string);
	}/**

	 **
	 * Returns the value of {@link #string2}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getString2()
	{
		return DraftedItem.string2.get(this);
	}/**

	 **
	 * Sets a new value for {@link #string2}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setString2(final java.lang.String string2)
			throws
				com.exedio.cope.StringLengthViolationException
	{
		DraftedItem.string2.set(this,string2);
	}/**

	 **
	 * Returns the value mapped to <tt>k</tt> by the field map {@link #map}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final String getMap(final Integer k)
	{
		return DraftedItem.map.get(this,k);
	}/**

	 **
	 * Associates <tt>k</tt> to a new value in the field map {@link #map}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setMap(final Integer k,final String map)
	{
		DraftedItem.map.set(this,k,map);
	}/**

	 **
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getMap public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.util.Map<Integer,String> getMapMap()
	{
		return DraftedItem.map.getMap(this);
	}/**

	 **
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.setMap public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setMapMap(final java.util.Map<? extends Integer,? extends String> map)
	{
		DraftedItem.map.setMap(this,map);
	}/**

	 **
	 * Returns the parent field of the type of {@link #map}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.Parent public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	static final com.exedio.cope.ItemField<DraftedItem> mapParent()
	{
		return DraftedItem.map.getParent(DraftedItem.class);
	}/**

	 **
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 */
	private static final long serialVersionUID = 1l;/**

	 **
	 * The persistent type information for draftedItem.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.type public|package|protected|private|none</tt> in the class comment.
	 */
	public static final com.exedio.cope.Type<DraftedItem> TYPE = com.exedio.cope.TypesBound.newType(DraftedItem.class);/**

	 **
	 * Activation constructor. Used for internal purposes only.
	 * @see com.exedio.cope.Item#Item(com.exedio.cope.ActivationParameters)
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 */
	@SuppressWarnings("unused") private DraftedItem(final com.exedio.cope.ActivationParameters ap){super(ap);
}}
