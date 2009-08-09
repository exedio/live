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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.exedio.cope.Cope;
import com.exedio.cope.Feature;
import com.exedio.cope.IntegerField;
import com.exedio.cope.Item;
import com.exedio.cope.StringField;
import com.exedio.cope.pattern.MapField;
import com.exedio.cope.pattern.Media;
import com.exedio.cope.pattern.MediaFilter;
import com.exedio.cops.XMLEncoder;

final class LiveRequest
{
	static LiveRequest get(
			final HttpServletRequest request,
			final HttpServletResponse response)
	{
		final HttpSession session = request.getSession(false);
		if(session==null)
			return null;
		
		final Object anchor = session.getAttribute(Editor.ANCHOR);
		if(anchor==null)
			return null;
		
		return new LiveRequest(request, response, (Anchor)anchor);
	}
	
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final Anchor anchor;
	private HashMap<IntegerField, Item> positionItems = null;
	
	private LiveRequest(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final Anchor anchor)
	{
		this.request = request;
		this.response = response;
		this.anchor = anchor;
		
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
	
	private static final void checkEdit(final Feature feature, final Item item)
	{
		if(feature==null)
			throw new NullPointerException("feature");
		if(item==null)
			throw new NullPointerException("item");
		if(!feature.getType().isAssignableFrom(item.getCopeType()))
			throw new IllegalArgumentException("item " + item.getCopeID() + " does not belong to type of feature " + feature.getID());
	}
	
	private static final <K> Item getItem(final MapField<K, String> feature, final K key, final Item item)
	{
		return
				feature.getRelationType().searchSingletonStrict(
						feature.getKey().equal(key).and(
						Cope.equalAndCast(feature.getParent(), item)));
	}
	
	<K> String edit(final String content, final MapField<K, String> feature, final Item item, final K key)
	{
		checkEdit(feature, item);
		
		return edit(
				content,
				(StringField)feature.getValue(),
				getItem(feature, key, item));
	}
	
	String edit(final String content, final StringField feature, final Item item)
	{
		checkEdit(feature, item);
		if(feature.isFinal())
			throw new IllegalArgumentException("feature " + feature.getID() + " must not be final");
		
		if(!anchor.borders)
		{
			final String modification = anchor.getModification(feature, item);
			return (modification!=null) ? modification : content;
		}
		
		final boolean block = feature.getMaximumLength()>StringField.DEFAULT_LENGTH;
		final String savedContent = feature.get(item);
		final String pageContent;
		final String editorContent;
		final boolean modifiable;
		if(content!=null ? content.equals(savedContent) : (savedContent==null))
		{
			modifiable = true;
			final String modification = anchor.getModification(feature, item);
			if(modification!=null)
				pageContent = editorContent = modification;
			else
				pageContent = editorContent = savedContent; // equals content anyway
		}
		else
		{
			modifiable = false;
			pageContent = content;
			editorContent = savedContent;
		}
		
		final String tag = block ? "div" : "span";
		final String editorContentEncoded = XMLEncoder.encode(editorContent);
		final StringBuilder bf = new StringBuilder();
		bf.append('<').
			append(tag).
			append(
				" class=\"contentEditorLink\"" +
				" onclick=\"" +
					"return " + (block ? Editor.EDIT_METHOD_AREA : Editor.EDIT_METHOD_LINE) + "(this,'").
						append(feature.getID()).
						append("','").
						append(item.getCopeID()).
						append("','").
						append(block ? editorContentEncoded.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r") : editorContentEncoded).
					append("'," + modifiable + ");\"").
			append('>').
			append(pageContent).
			append("</").
			append(tag).
			append('>');
		
		return bf.toString();
	}
	
	String edit(final Media feature, final Item item)
	{
		return edit(feature, item, true);
	}
	
	private String edit(final Media feature, final Item item, final boolean modifiable)
	{
		checkEdit(feature, item);
		if(feature.isFinal())
			throw new IllegalArgumentException("feature " + feature.getID() + " must not be final");
		
		final String modificationURL = modifiable ? anchor.getModificationURL(feature, item, request, response) : null;
		final String onload =
			modificationURL!=null
				? (" onload=\"this.src='" + XMLEncoder.encode(response.encodeURL(modificationURL)) + "';\"")
				: "";
		
		if(!anchor.borders)
			return onload;
		
		final StringBuilder bf = new StringBuilder();
		bf.append(
				" class=\"contentEditorLink\"" +
				onload +
				" onclick=\"" +
					"return " + Editor.EDIT_METHOD_FILE + "(this,'").
						append(feature.getID()).
						append("','").
						append(item.getCopeID()).
						append("','").
						append(XMLEncoder.encode(feature.getURL(item))).
					append("'," + modifiable + ");\"");
		
		return bf.toString();
	}
	
	String edit(final MediaFilter feature, final Item item)
	{
		if(!anchor.borders)
			return "";
		
		checkEdit(feature, item);
		
		return edit(feature.getSource(), item, false);
	}
	
	String edit(final IntegerField feature, final Item item)
	{
		if(!anchor.borders)
			return "";
		
		return edit(feature, item, anchor.previousPositionButtonURL);
	}
	
	String edit(final IntegerField feature, final Item item, final String buttonURL)
	{
		if(!anchor.borders)
			return "";
		
		checkEdit(feature, item);
		if(feature.isFinal())
			throw new IllegalArgumentException("feature " + feature.getID() + " must not be final");
		
		final Item previousItem = registerPositionItem(feature, item);
		if(previousItem==null)
			return "";
		
		return
			"<form action=\"" + action(request, response) + "\" method=\"POST\" class=\"contentEditorPosition\">" +
				"<input type=\"hidden\" name=\"" + Editor.BAR_REFERER   + "\" value=\"" + referer(request)         + "\">" +
				"<input type=\"hidden\" name=\"" + Editor.BAR_FEATURE   + "\" value=\"" + feature.getID()          + "\">" +
				"<input type=\"hidden\" name=\"" + Editor.BAR_ITEM_FROM + "\" value=\"" + previousItem.getCopeID() + "\">" +
				"<input type=\"hidden\" name=\"" + Editor.BAR_ITEM      + "\" value=\"" + item.getCopeID()         + "\">" +
				(
					buttonURL!=null
					? ("<input type=\"image\" src=\"" + buttonURL + "\" alt=\"Swap with previous item\">")
					: ("<input type=\"submit\" value=\"Up\">")
				) +
			"</form>";
	}
	
	void writeHead(final PrintStream out)
	{
		final StringBuilder bf = new StringBuilder();
		writeHead(bf);
		out.print(bf);
	}
	
	void writeBar(final PrintStream out)
	{
		final StringBuilder bf = new StringBuilder();
		writeBar(bf);
		out.print(bf);
	}
	
	void writeHead(final StringBuilder out)
	{
		Bar_Jspm.writeHead(out, anchor.borders);
	}
	
	void writeBar(final StringBuilder out)
	{
		final ArrayList<Target> targets = new ArrayList<Target>();
		targets.add(TargetLive.INSTANCE);
		if(anchor.draftsEnabled)
		{
			final List<Draft> drafts = Draft.TYPE.search(null, Draft.date, true);
			for(final Draft draft : drafts)
				targets.add(new TargetDraft(draft));
			targets.add(TargetNewDraft.INSTANCE);
		}
		final boolean borders = anchor.borders;
		Bar_Jspm.write(out,
				anchor.getTarget(),
				targets,
				action(request, response),
				referer(request),
				borders,
				borders ? Editor.BAR_BORDERS_OFF : Editor.BAR_BORDERS_ON,
				borders ? anchor.borderDisableButtonURL : anchor.borderEnableButtonURL,
				anchor.hideButtonURL,
				anchor.closeButtonURL,
				anchor.getModificationsCount(),
				anchor.sessionName);
	}
	
	private static final String action(final HttpServletRequest request, final HttpServletResponse response)
	{
		return response.encodeURL(request.getContextPath() + request.getServletPath() + Editor.LOGIN_PATH_INFO);
	}
	
	private static final String referer(final HttpServletRequest request)
	{
		final String queryString = request.getQueryString();
		return queryString!=null ? (request.getPathInfo() + '?' + request.getQueryString()) : request.getPathInfo();
	}
}
