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

package com.exedio.cope.live.webtest;

import com.exedio.cope.IntegerField;
import com.exedio.cope.Item;
import com.exedio.cope.StringField;
import com.exedio.cope.misc.Computed;
import com.exedio.cope.pattern.History;
import com.exedio.cope.pattern.MapField;
import com.exedio.cope.pattern.Media;
import com.exedio.cope.pattern.MediaThumbnail;

final class EditedItem extends Item
{
	static final IntegerField position = new IntegerField();

	static final StringField field = new StringField();
	static final StringField fieldBlock = new StringField().lengthMax(StringField.DEFAULT_LENGTH+1);

	static final MapField<Integer, String> map = MapField.newMap(new IntegerField(), new StringField());
	static final MapField<Integer, String> mapBlock = MapField.newMap(new IntegerField(), new StringField().lengthMax(StringField.DEFAULT_LENGTH+1));
	
	static final Media image = new Media().contentType("image/jpeg", "image/png");
	static final MediaThumbnail thumbnail = new MediaThumbnail(image, 20, 20);
	
	@Computed() static final IntegerField computedPosition = new IntegerField();
	@Computed() static final StringField computedField = new StringField();
	@Computed() static final MapField<Integer, String> computedMap = MapField.newMap(new IntegerField(), new StringField());
	@Computed() static final Media computedImage = new Media().contentType("image/jpeg", "image/png");
	@Computed() static final MediaThumbnail computedThumbnail = new MediaThumbnail(image, 20, 20);
	
	static final History history = new History();

	
	/**

	 **
	 * Creates a new EditedItem with all the fields initially needed.
	 * @param position the initial value for field {@link #position}.
	 * @param field the initial value for field {@link #field}.
	 * @param fieldBlock the initial value for field {@link #fieldBlock}.
	 * @param image the initial value for field {@link #image}.
	 * @param computedPosition the initial value for field {@link #computedPosition}.
	 * @param computedField the initial value for field {@link #computedField}.
	 * @param computedImage the initial value for field {@link #computedImage}.
	 * @throws com.exedio.cope.MandatoryViolationException if field, fieldBlock, image, computedField, computedImage is null.
	 * @throws com.exedio.cope.StringLengthViolationException if field, fieldBlock, computedField violates its length constraint.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tags <tt>@cope.constructor public|package|protected|private|none</tt> in the class comment and <tt>@cope.initial</tt> in the comment of fields.
	 */
	EditedItem(
				int position,
				java.lang.String field,
				java.lang.String fieldBlock,
				com.exedio.cope.pattern.Media.Value image,
				int computedPosition,
				java.lang.String computedField,
				com.exedio.cope.pattern.Media.Value computedImage)
			throws
				com.exedio.cope.MandatoryViolationException,
				com.exedio.cope.StringLengthViolationException
	{
		this(new com.exedio.cope.SetValue[]{
			EditedItem.position.map(position),
			EditedItem.field.map(field),
			EditedItem.fieldBlock.map(fieldBlock),
			EditedItem.image.map(image),
			EditedItem.computedPosition.map(computedPosition),
			EditedItem.computedField.map(computedField),
			EditedItem.computedImage.map(computedImage),
		});
	}/**

	 **
	 * Creates a new EditedItem and sets the given fields initially.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.generic.constructor public|package|protected|private|none</tt> in the class comment.
	 */
	private EditedItem(com.exedio.cope.SetValue... setValues)
	{
		super(setValues);
	}/**

	 **
	 * Activation constructor. Used for internal purposes only.
	 * @see com.exedio.cope.Item#Item(com.exedio.cope.ActivationParameters)
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 */
	@SuppressWarnings("unused") private EditedItem(com.exedio.cope.ActivationParameters ap)
	{
		super(ap);
	}/**

	 **
	 * Returns the value of {@link #position}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final int getPosition()
	{
		return EditedItem.position.getMandatory(this);
	}/**

	 **
	 * Sets a new value for {@link #position}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setPosition(int position)
	{
		EditedItem.position.set(this,position);
	}/**

	 **
	 * Returns the value of {@link #field}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getField()
	{
		return EditedItem.field.get(this);
	}/**

	 **
	 * Sets a new value for {@link #field}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setField(java.lang.String field)
			throws
				com.exedio.cope.MandatoryViolationException,
				com.exedio.cope.StringLengthViolationException
	{
		EditedItem.field.set(this,field);
	}/**

	 **
	 * Returns the value of {@link #fieldBlock}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getFieldBlock()
	{
		return EditedItem.fieldBlock.get(this);
	}/**

	 **
	 * Sets a new value for {@link #fieldBlock}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setFieldBlock(java.lang.String fieldBlock)
			throws
				com.exedio.cope.MandatoryViolationException,
				com.exedio.cope.StringLengthViolationException
	{
		EditedItem.fieldBlock.set(this,fieldBlock);
	}/**

	 **
	 * Returns the value mapped to <tt>k</tt> by the field map {@link #map}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final String getMap(Integer k)
	{
		return EditedItem.map.get(this,k);
	}/**

	 **
	 * Associates <tt>k</tt> to a new value in the field map {@link #map}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setMap(Integer k,String map)
	{
		EditedItem.map.set(this,k,map);
	}/**

	 **
	 * Returns the parent field of the type of {@link #map}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.Parent public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	static final com.exedio.cope.ItemField<EditedItem> mapParent()
	{
		return EditedItem.map.getParent(EditedItem.class);
	}/**

	 **
	 * Returns the value mapped to <tt>k</tt> by the field map {@link #mapBlock}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final String getMapBlock(Integer k)
	{
		return EditedItem.mapBlock.get(this,k);
	}/**

	 **
	 * Associates <tt>k</tt> to a new value in the field map {@link #mapBlock}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setMapBlock(Integer k,String mapBlock)
	{
		EditedItem.mapBlock.set(this,k,mapBlock);
	}/**

	 **
	 * Returns the parent field of the type of {@link #mapBlock}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.Parent public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	static final com.exedio.cope.ItemField<EditedItem> mapBlockParent()
	{
		return EditedItem.mapBlock.getParent(EditedItem.class);
	}/**

	 **
	 * Returns a URL the content of {@link #image} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURL public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getImageURL()
	{
		return EditedItem.image.getURL(this);
	}/**

	 **
	 * Returns the content type of the media {@link #image}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getContentType public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getImageContentType()
	{
		return EditedItem.image.getContentType(this);
	}/**

	 **
	 * Returns the last modification date of media {@link #image}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getLastModified public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final long getImageLastModified()
	{
		return EditedItem.image.getLastModified(this);
	}/**

	 **
	 * Returns the body length of the media {@link #image}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getLength public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final long getImageLength()
	{
		return EditedItem.image.getLength(this);
	}/**

	 **
	 * Returns the body of the media {@link #image}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getBody public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final byte[] getImageBody()
	{
		return EditedItem.image.getBody(this);
	}/**

	 **
	 * Writes the body of media {@link #image} into the given stream.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getBody public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void getImageBody(java.io.OutputStream body)
			throws
				java.io.IOException
	{
		EditedItem.image.getBody(this,body);
	}/**

	 **
	 * Writes the body of media {@link #image} into the given file.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getBody public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void getImageBody(java.io.File body)
			throws
				java.io.IOException
	{
		EditedItem.image.getBody(this,body);
	}/**

	 **
	 * Sets the content of media {@link #image}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setImage(com.exedio.cope.pattern.Media.Value image)
			throws
				java.io.IOException
	{
		EditedItem.image.set(this,image);
	}/**

	 **
	 * Sets the content of media {@link #image}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setImage(byte[] body,java.lang.String contentType)
	{
		EditedItem.image.set(this,body,contentType);
	}/**

	 **
	 * Sets the content of media {@link #image}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setImage(java.io.InputStream body,java.lang.String contentType)
			throws
				java.io.IOException
	{
		EditedItem.image.set(this,body,contentType);
	}/**

	 **
	 * Sets the content of media {@link #image}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setImage(java.io.File body,java.lang.String contentType)
			throws
				java.io.IOException
	{
		EditedItem.image.set(this,body,contentType);
	}/**

	 **
	 * Returns a URL the content of {@link #thumbnail} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURL public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbnailURL()
	{
		return EditedItem.thumbnail.getURL(this);
	}/**

	 **
	 * Returns the content type of the media {@link #thumbnail}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getContentType public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbnailContentType()
	{
		return EditedItem.thumbnail.getContentType(this);
	}/**

	 **
	 * Returns a URL the content of {@link #thumbnail} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURLWithFallbackToSource public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getThumbnailURLWithFallbackToSource()
	{
		return EditedItem.thumbnail.getURLWithFallbackToSource(this);
	}/**

	 **
	 * Returns the value of {@link #computedPosition}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final int getComputedPosition()
	{
		return EditedItem.computedPosition.getMandatory(this);
	}/**

	 **
	 * Sets a new value for {@link #computedPosition}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setComputedPosition(int computedPosition)
	{
		EditedItem.computedPosition.set(this,computedPosition);
	}/**

	 **
	 * Returns the value of {@link #computedField}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getComputedField()
	{
		return EditedItem.computedField.get(this);
	}/**

	 **
	 * Sets a new value for {@link #computedField}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setComputedField(java.lang.String computedField)
			throws
				com.exedio.cope.MandatoryViolationException,
				com.exedio.cope.StringLengthViolationException
	{
		EditedItem.computedField.set(this,computedField);
	}/**

	 **
	 * Returns the value mapped to <tt>k</tt> by the field map {@link #computedMap}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.get public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final String getComputedMap(Integer k)
	{
		return EditedItem.computedMap.get(this,k);
	}/**

	 **
	 * Associates <tt>k</tt> to a new value in the field map {@link #computedMap}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setComputedMap(Integer k,String computedMap)
	{
		EditedItem.computedMap.set(this,k,computedMap);
	}/**

	 **
	 * Returns the parent field of the type of {@link #computedMap}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.Parent public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	static final com.exedio.cope.ItemField<EditedItem> computedMapParent()
	{
		return EditedItem.computedMap.getParent(EditedItem.class);
	}/**

	 **
	 * Returns a URL the content of {@link #computedImage} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURL public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getComputedImageURL()
	{
		return EditedItem.computedImage.getURL(this);
	}/**

	 **
	 * Returns the content type of the media {@link #computedImage}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getContentType public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getComputedImageContentType()
	{
		return EditedItem.computedImage.getContentType(this);
	}/**

	 **
	 * Returns the last modification date of media {@link #computedImage}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getLastModified public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final long getComputedImageLastModified()
	{
		return EditedItem.computedImage.getLastModified(this);
	}/**

	 **
	 * Returns the body length of the media {@link #computedImage}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getLength public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final long getComputedImageLength()
	{
		return EditedItem.computedImage.getLength(this);
	}/**

	 **
	 * Returns the body of the media {@link #computedImage}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getBody public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final byte[] getComputedImageBody()
	{
		return EditedItem.computedImage.getBody(this);
	}/**

	 **
	 * Writes the body of media {@link #computedImage} into the given stream.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getBody public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void getComputedImageBody(java.io.OutputStream body)
			throws
				java.io.IOException
	{
		EditedItem.computedImage.getBody(this,body);
	}/**

	 **
	 * Writes the body of media {@link #computedImage} into the given file.
	 * Does nothing, if the media is null.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getBody public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void getComputedImageBody(java.io.File body)
			throws
				java.io.IOException
	{
		EditedItem.computedImage.getBody(this,body);
	}/**

	 **
	 * Sets the content of media {@link #computedImage}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setComputedImage(com.exedio.cope.pattern.Media.Value computedImage)
			throws
				java.io.IOException
	{
		EditedItem.computedImage.set(this,computedImage);
	}/**

	 **
	 * Sets the content of media {@link #computedImage}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setComputedImage(byte[] body,java.lang.String contentType)
	{
		EditedItem.computedImage.set(this,body,contentType);
	}/**

	 **
	 * Sets the content of media {@link #computedImage}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setComputedImage(java.io.InputStream body,java.lang.String contentType)
			throws
				java.io.IOException
	{
		EditedItem.computedImage.set(this,body,contentType);
	}/**

	 **
	 * Sets the content of media {@link #computedImage}.
	 * @throws java.io.IOException if accessing <tt>body</tt> throws an IOException.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.set public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final void setComputedImage(java.io.File body,java.lang.String contentType)
			throws
				java.io.IOException
	{
		EditedItem.computedImage.set(this,body,contentType);
	}/**

	 **
	 * Returns a URL the content of {@link #computedThumbnail} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURL public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getComputedThumbnailURL()
	{
		return EditedItem.computedThumbnail.getURL(this);
	}/**

	 **
	 * Returns the content type of the media {@link #computedThumbnail}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getContentType public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getComputedThumbnailContentType()
	{
		return EditedItem.computedThumbnail.getContentType(this);
	}/**

	 **
	 * Returns a URL the content of {@link #computedThumbnail} is available under.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getURLWithFallbackToSource public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.lang.String getComputedThumbnailURLWithFallbackToSource()
	{
		return EditedItem.computedThumbnail.getURLWithFallbackToSource(this);
	}/**

	 **
	 * Returns the events of the history {@link #history}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.getEvents public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final java.util.List<? extends com.exedio.cope.pattern.History.Event> getHistoryEvents()
	{
		return EditedItem.history.getEvents(this);
	}/**

	 **
	 * Creates a new event for the history {@link #history}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.createEvent public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	final com.exedio.cope.pattern.History.Event createHistoryEvent(java.lang.String author,boolean isNew)
	{
		return EditedItem.history.createEvent(this,author,isNew);
	}/**

	 **
	 * Returns the parent field of the event type of {@link #history}.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.EventParent public|package|protected|private|none|non-final</tt> in the comment of the field.
	 */
	static final com.exedio.cope.ItemField<EditedItem> historyEventParent()
	{
		return EditedItem.history.getEventParent(EditedItem.class);
	}/**

	 **
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 */
	private static final long serialVersionUID = 1l;/**

	 **
	 * The persistent type information for editedItem.
	 * @cope.generated This feature has been generated by the cope instrumentor and will be overwritten by the build process.
	 *       It can be customized with the tag <tt>@cope.type public|package|protected|private|none</tt> in the class comment.
	 */
	static final com.exedio.cope.Type<EditedItem> TYPE = com.exedio.cope.TypesBound.newType(EditedItem.class)
;}
